/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.framework.biz.operator.log.core.aspect;

import cn.orionsec.kit.lang.define.thread.ExecutorBuilder;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Strings;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.dromara.visor.framework.biz.operator.log.core.annotation.IgnoreParameter;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.biz.operator.log.core.factory.OperatorTypeHolder;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorLogModel;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;
import org.dromara.visor.framework.biz.operator.log.core.service.OperatorLogFrameworkService;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogFiller;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.common.constant.BeanOrderConst;
import org.dromara.visor.framework.common.security.LoginUser;
import org.dromara.visor.framework.common.security.SecurityHolder;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

/**
 * 操作日志切面
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 10:47
 */
@Aspect
@Slf4j
@Order(BeanOrderConst.OPERATOR_LOG_ASPECT)
public class OperatorLogAspect {

    private static final ExecutorService LOG_SAVER = ExecutorBuilder.create()
            .corePoolSize(1)
            .maxPoolSize(1)
            .useLinkedBlockingQueue()
            .allowCoreThreadTimeout()
            .useLinkedBlockingQueue()
            .build();

    private final OperatorLogFrameworkService operatorLogFrameworkService;

    @Resource
    private SecurityHolder securityHolder;

    public OperatorLogAspect(OperatorLogFrameworkService operatorLogFrameworkService) {
        this.operatorLogFrameworkService = operatorLogFrameworkService;
    }

    @Around("@annotation(o)")
    public Object around(ProceedingJoinPoint joinPoint, OperatorLog o) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            // 初始化参数
            this.initExtra(joinPoint, o);
            // 执行
            Object result = joinPoint.proceed();
            // 记录日志
            this.saveLog(start, o, result, null);
            return result;
        } catch (Throwable exception) {
            // 记录日志
            this.saveLog(start, o, null, exception);
            throw exception;
        } finally {
            // 清空上下文
            OperatorLogs.clear();
        }
    }

    /**
     * 初始化参数
     *
     * @param joinPoint joinPoint
     */
    private void initExtra(ProceedingJoinPoint joinPoint, OperatorLog o) {
        // 清空上下文
        OperatorLogs.clear();
        if (!o.parameter()) {
            return;
        }
        // 获取方法注解
        Annotation[][] methodAnnotations = Optional.ofNullable(joinPoint.getSignature())
                .filter(s -> (s instanceof MethodSignature))
                .map(s -> ((MethodSignature) s).getMethod())
                .map(Method::getParameterAnnotations)
                .orElse(null);
        if (Arrays1.isEmpty(methodAnnotations)) {
            return;
        }
        // 获取参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < methodAnnotations.length; i++) {
            Annotation[] annotations = methodAnnotations[i];
            if (Arrays1.isEmpty(annotations)) {
                continue;
            }
            // 检查是否有 IgnoreParameter 注解
            boolean ignore = Arrays.stream(annotations)
                    .anyMatch(s -> s instanceof IgnoreParameter);
            if (ignore) {
                continue;
            }
            // 获取需要记录的参数
            for (Annotation annotation : annotations) {
                if (annotation instanceof RequestBody) {
                    OperatorLogs.add(args[i]);
                    break;
                } else if (annotation instanceof RequestParam) {
                    Object arg = args[i];
                    if (arg instanceof MultipartFile) {
                        break;
                    }
                    OperatorLogs.add(
                            Strings.ifBlank(((RequestParam) annotation).value(),
                                    ((RequestParam) annotation).name()),
                            args[i]);
                    break;
                } else if (annotation instanceof RequestHeader) {
                    OperatorLogs.add(
                            Strings.ifBlank(((RequestHeader) annotation).value(),
                                    ((RequestHeader) annotation).name()),
                            args[i]);
                    break;
                } else if (annotation instanceof PathVariable) {
                    OperatorLogs.add(
                            Strings.ifBlank(((PathVariable) annotation).value(),
                                    ((PathVariable) annotation).name()),
                            args[i]);
                    break;
                }
            }
        }
    }

    /**
     * 保存日志
     *
     * @param start     start
     * @param exception exception
     */
    private void saveLog(long start, OperatorLog o, Object ret, Throwable exception) {
        try {
            // 获取日志类型
            OperatorType type = OperatorTypeHolder.get(o.value());
            if (type == null) {
                return;
            }
            // 获取当前用户
            LoginUser user = this.getUser();
            if (user == null) {
                return;
            }
            // 检查是否保存
            if (!OperatorLogs.isSave()) {
                return;
            }
            // 填充请求
            Map<String, Object> extra = OperatorLogs.get();
            OperatorLogModel model = OperatorLogFiller.create()
                    // 填充使用时间
                    .fillUsedTime(start)
                    // 填充用户信息
                    .fillUserInfo(user)
                    // 填充请求信息
                    .fillRequest()
                    // 填充结果信息
                    .fillResult(o.ret(), ret, exception)
                    // 填充拓展信息
                    .fillExtra(extra)
                    // 填充日志
                    .fillLogInfo(extra, type)
                    .get();
            // 插入日志
            this.asyncSaveLog(model);
        } catch (Exception e) {
            log.error("操作日志保存失败", e);
        }
    }

    /**
     * 获取当前用户
     *
     * @return user
     */
    private LoginUser getUser() {
        LoginUser user = OperatorLogs.getUser();
        if (user != null) {
            return user;
        }
        // 登录上下文获取
        return securityHolder.getLoginUser();
    }

    /**
     * 异步保存日志
     *
     * @param model model
     */
    private void asyncSaveLog(OperatorLogModel model) {
        LOG_SAVER.submit(() -> operatorLogFrameworkService.insert(model));
    }

}
