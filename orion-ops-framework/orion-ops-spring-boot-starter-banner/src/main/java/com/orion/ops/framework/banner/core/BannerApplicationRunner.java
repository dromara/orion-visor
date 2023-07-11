package com.orion.ops.framework.banner.core;

import com.orion.lang.utils.Threads;
import com.orion.lang.utils.ansi.AnsiColor;
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

    @Override
    public void run(ApplicationArguments args) {
        String line = AnsiColor.GLOSS_GREEN.color(":: orion-ops-launch v" + version + " 服务已启动(" + env + ") ::\n") +
                AnsiColor.GLOSS_GREEN.color(":: swagger 文档       ") +
                AnsiColor.GLOSS_BLUE.color("http://127.0.0.1:" + port + "/doc.html\n") +
                AnsiColor.GLOSS_GREEN.color(":: druid console     ") +
                AnsiColor.GLOSS_BLUE.color("http://127.0.0.1:" + port + "/druid/index.html\n") +
                AnsiColor.GLOSS_GREEN.color(":: actuator endpoint ") +
                AnsiColor.GLOSS_BLUE.color("http://127.0.0.1:" + port + managementEndpoints + "\n") +
                AnsiColor.GLOSS_GREEN.color(":: admin console     ") +
                AnsiColor.GLOSS_BLUE.color("http://127.0.0.1:" + port + adminSeverContextPath + "\n") +
                AnsiColor.GLOSS_GREEN.color(":: server 健康检测    ") +
                AnsiColor.GLOSS_BLUE +
                "curl -X GET --location \"http://127.0.0.1:" + port + apiPrefix + "/server/bootstrap/health\"" +
                AnsiColor.SUFFIX;
        Threads.start(() -> {
            Threads.sleep(1000L);
            System.out.println(line);
        });
    }

}
