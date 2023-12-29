package com.orion.ops.framework.biz.operator.log.core.uitls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.orion.lang.able.Gettable;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Refs;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.json.matcher.ReplacementFormatters;
import com.orion.ops.framework.biz.operator.log.core.config.OperatorLogConfig;
import com.orion.ops.framework.biz.operator.log.core.enums.ReturnType;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorLogModel;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;
import com.orion.ops.framework.common.entity.RequestIdentity;
import com.orion.ops.framework.common.enums.BooleanBit;
import com.orion.ops.framework.common.meta.TraceIdHolder;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.utils.Requests;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 操作日志填充器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/29 11:01
 */
public class OperatorLogFiller implements Gettable<OperatorLogModel> {

    private static SerializeFilter[] serializeFilters;

    private static OperatorLogConfig operatorLogConfig;

    private final OperatorLogModel model;

    public OperatorLogFiller(OperatorLogModel model) {
        this.model = model;
    }

    /**
     * 创建填充器
     *
     * @return filler
     */
    public static OperatorLogFiller create() {
        return new OperatorLogFiller(new OperatorLogModel());
    }

    /**
     * 创建填充器
     *
     * @param model model
     * @return filler
     */
    public static OperatorLogFiller create(OperatorLogModel model) {
        return new OperatorLogFiller(model);
    }

    /**
     * 填充使用时间
     *
     * @param start start
     * @return this
     */
    public OperatorLogFiller fillUsedTime(long start) {
        long end = System.currentTimeMillis();
        model.setDuration((int) (end - start));
        model.setStartTime(new Date(start));
        model.setEndTime(new Date(end));
        return this;
    }

    /**
     * 填充用户信息
     *
     * @param userId   userId
     * @param username username
     * @return this
     */
    public OperatorLogFiller fillUserInfo(Long userId, String username) {
        model.setUserId(userId);
        model.setUsername(username);
        return this;
    }

    /**
     * 填充用户信息
     *
     * @param user user
     * @return this
     */
    public OperatorLogFiller fillUserInfo(LoginUser user) {
        model.setUserId(user.getId());
        model.setUsername(user.getUsername());
        return this;
    }

    /**
     * 填充 traceId
     *
     * @param traceId traceId
     * @return this
     */
    public OperatorLogFiller fillTraceId(String traceId) {
        model.setTraceId(traceId);
        return this;
    }

    /**
     * 填充请求留痕信息
     *
     * @param identity identity
     * @return this
     */
    public OperatorLogFiller fillIdentity(RequestIdentity identity) {
        model.setAddress(identity.getAddress());
        model.setLocation(identity.getLocation());
        model.setUserAgent(identity.getUserAgent());
        // 填充请求信息
        Optional.ofNullable(model.getUserAgent())
                .map(s -> Strings.retain(s, operatorLogConfig.getUserAgentLength()))
                .ifPresent(model::setUserAgent);
        return this;
    }

    /**
     * 填充请求留痕信息
     *
     * @return this
     */
    public OperatorLogFiller fillRequest() {
        model.setTraceId(TraceIdHolder.get());
        // 填充请求信息
        Requests.fillIdentity(model);
        return this.fillIdentity(model);
    }

    /**
     * 填充结果
     *
     * @param ret ret
     * @return this
     */
    public OperatorLogFiller fillResult(Object ret) {
        return this.fillResult(ReturnType.JSON, ret, null);
    }

    /**
     * 填充结果
     *
     * @param exception exception
     * @return this
     */
    public OperatorLogFiller fillException(Throwable exception) {
        return this.fillResult(null, null, exception);
    }

    /**
     * 填充结果
     *
     * @param retType   retType
     * @param ret       ret
     * @param exception exception
     * @return this
     */
    public OperatorLogFiller fillResult(ReturnType retType, Object ret, Throwable exception) {
        if (exception == null) {
            model.setResult(BooleanBit.TRUE.getValue());
            if (ret != null) {
                if (ReturnType.JSON.equals(retType)) {
                    // 脱敏
                    String returnValue = JSON.toJSONString(ret, serializeFilters);
                    if (JSON.isValidObject(returnValue)) {
                        // json object 对象
                        model.setReturnValue(Refs.json(JSON.parseObject(returnValue)));
                    } else if (JSON.isValidArray(returnValue)) {
                        // json array
                        model.setReturnValue(Refs.json(JSON.parseArray(returnValue)));
                    } else {
                        // 普通文本
                        model.setReturnValue(Refs.json(returnValue));
                    }
                } else if (ReturnType.TO_STRING.equals(retType)) {
                    // 文本
                    model.setReturnValue(Refs.json(Objects.toString(ret)));
                }
            }
        } else {
            model.setResult(BooleanBit.FALSE.getValue());
            // 错误信息
            String errorMessage = Strings.retain(exception.getMessage(), operatorLogConfig.getErrorMessageLength());
            model.setErrorMessage(errorMessage);
        }
        return this;
    }

    /**
     * 填充拓展信息
     *
     * @param extra extra
     * @return this
     */
    public OperatorLogFiller fillExtra(Map<String, Object> extra) {
        model.setExtra(JSON.toJSONString(extra));
        return this;
    }

    /**
     * 填充日志信息
     *
     * @param extra extra
     * @param type  type
     * @return this
     */
    public OperatorLogFiller fillLogInfo(Map<String, Object> extra, OperatorType type) {
        model.setRiskLevel(type.getRiskLevel().name());
        model.setModule(type.getModule());
        model.setType(type.getType());
        model.setLogInfo(ReplacementFormatters.format(type.getTemplate(), extra));
        return this;
    }

    @Override
    public OperatorLogModel get() {
        return model;
    }

    public static void setSerializeFilters(SerializeFilter[] serializeFilters) {
        if (OperatorLogFiller.serializeFilters != null) {
            // unmodified
            throw Exceptions.state();
        }
        OperatorLogFiller.serializeFilters = serializeFilters;
    }

    public static void setOperatorLogConfig(OperatorLogConfig operatorLogConfig) {
        if (OperatorLogFiller.operatorLogConfig != null) {
            // unmodified
            throw Exceptions.state();
        }
        OperatorLogFiller.operatorLogConfig = operatorLogConfig;
    }

}
