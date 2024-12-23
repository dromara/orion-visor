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
package org.dromara.visor.framework.banner.core.runner;

import cn.orionsec.kit.lang.utils.Threads;
import cn.orionsec.kit.lang.utils.ansi.AnsiAppender;
import cn.orionsec.kit.lang.utils.ansi.style.color.AnsiForeground;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * banner printer
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/15 16:18
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Value("${spring.profiles.active}")
    private String env;

    @Value("${orion.version}")
    private String version;

    @Value("${server.port}")
    private String port;

    @Value("${orion.api.prefix}")
    private String apiPrefix;

    @Value("${spring.boot.admin.context-path:''}")
    private String adminSeverContextPath;

    @Value("${management.endpoints.web.base-path:''}")
    private String managementEndpoints;

    @Value("${springdoc.api-docs.enabled}")
    private Boolean apiDocsEnabled;

    @Value("${spring.datasource.druid.stat-view-servlet.enabled}")
    private Boolean druidConsoleEnabled;

    @Value("#{'${management.endpoints.web.exposure.include}' != 'shutdown'}")
    private Boolean springBootActuatorEnabled;

    @Value("${spring.boot.admin.client.enabled}")
    private Boolean springBootAdminClientEnabled;

    @Override
    public void run(ApplicationArguments args) {
        AnsiAppender appender = AnsiAppender.create()
                .append(AnsiForeground.BRIGHT_GREEN, ":: orion-visor v" + version + " 服务已启动(" + env + ") ::\n");
        // swagger 地址
        if (apiDocsEnabled) {
            appender.append(AnsiForeground.BRIGHT_GREEN, ":: swagger 文档       ")
                    .append(AnsiForeground.BRIGHT_BLUE, "http://127.0.0.1:" + port + "/doc.html\n");
        }
        // druid 控制台
        if (druidConsoleEnabled) {
            appender.append(AnsiForeground.BRIGHT_GREEN, ":: druid console     ")
                    .append(AnsiForeground.BRIGHT_BLUE, "http://127.0.0.1:" + port + "/druid/index.html\n");
        }
        // admin actuator 端点
        if (springBootActuatorEnabled) {
            appender.append(AnsiForeground.BRIGHT_GREEN, ":: actuator endpoint ")
                    .append(AnsiForeground.BRIGHT_BLUE, "http://127.0.0.1:" + port + managementEndpoints + "\n");
        }
        // admin server 控制台
        if (springBootAdminClientEnabled) {
            appender.append(AnsiForeground.BRIGHT_GREEN, ":: admin console     ")
                    .append(AnsiForeground.BRIGHT_BLUE, "http://127.0.0.1:" + port + adminSeverContextPath + "\n");
        }
        appender.append(AnsiForeground.BRIGHT_GREEN, ":: server 健康检测    ")
                .append(AnsiForeground.BRIGHT_BLUE, "curl -X GET --location \"http://127.0.0.1:" + port + apiPrefix + "/server/bootstrap/health\"");
        Threads.start(() -> {
            Threads.sleep(1000L);
            System.out.println(appender);
        });
    }

}
