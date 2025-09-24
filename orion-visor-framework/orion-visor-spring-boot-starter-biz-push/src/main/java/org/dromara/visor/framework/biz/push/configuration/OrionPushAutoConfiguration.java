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
package org.dromara.visor.framework.biz.push.configuration;

import cn.orionsec.kit.lang.function.Functions;
import org.dromara.visor.common.constant.AutoConfigureOrderConst;
import org.dromara.visor.framework.biz.push.core.enums.PushChannelEnum;
import org.dromara.visor.framework.biz.push.core.framework.service.PushTemplateFrameworkService;
import org.dromara.visor.framework.biz.push.core.framework.service.PushTemplateFrameworkServiceDelegate;
import org.dromara.visor.framework.biz.push.core.framework.service.WebsiteMessageFrameworkService;
import org.dromara.visor.framework.biz.push.core.listener.PushMessageEventListener;
import org.dromara.visor.framework.biz.push.core.message.PushMessage;
import org.dromara.visor.framework.biz.push.core.service.*;
import org.dromara.visor.framework.biz.push.core.utils.MessageChannelUtils;
import org.dromara.visor.framework.biz.push.core.utils.PushUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 推送自动配置类
 *
 * @author Shihao Lv
 * @version 1.0.0
 * @since 2025/9/17 23:04
 */
@AutoConfiguration
@AutoConfigureOrder(AutoConfigureOrderConst.FRAMEWORK_BIZ_PUSH)
public class OrionPushAutoConfiguration {

    /**
     * @return 钉钉推送服务
     */
    @Bean
    public DingPushService dingPushService() {
        return new DingPushService();
    }

    /**
     * @return 飞书推送服务
     */
    @Bean
    public FeiShuPushService feiShuPushService() {
        return new FeiShuPushService();
    }

    /**
     * @return 企业微信推送服务
     */
    @Bean
    public WeComPushService weComPushService() {
        return new WeComPushService();
    }

    /**
     * @param websiteMessageFrameworkService websiteMessageFrameworkService
     * @return 站内信推送服务
     */
    @Bean
    public WebsitePushService websitePushService(WebsiteMessageFrameworkService websiteMessageFrameworkService) {
        return new WebsitePushService(websiteMessageFrameworkService);
    }

    /**
     * @param impl impl
     * @return 推送模板服务
     */
    @Bean
    @Primary
    @ConditionalOnBean(PushTemplateFrameworkService.class)
    public PushTemplateFrameworkService pushTemplateFrameworkService(PushTemplateFrameworkService impl) {
        PushTemplateFrameworkServiceDelegate delegate = new PushTemplateFrameworkServiceDelegate(impl);
        // 设置到工具类
        PushUtils.setPushTemplateFrameworkService(delegate);
        return delegate;
    }

    /**
     * @param pushServices 推送服务
     * @return 消息推送事件监听器
     */
    @Bean
    public PushMessageEventListener pushMessageEventListener(List<IPushService<? extends PushMessage>> pushServices) {
        // 服务列表
        Map<PushChannelEnum, IPushService<? extends PushMessage>> serviceMap = pushServices.stream()
                .collect(Collectors.toMap(
                        MessageChannelUtils::getPushChannel,
                        Function.identity(),
                        Functions.right()));
        // 创建监听器
        return new PushMessageEventListener(serviceMap);
    }

}
