package com.orion.ops.framework.banner.core;

import com.orion.lang.utils.ansi.AnsiCode;
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

    @Override
    public void run(ApplicationArguments args) {
        String line = AnsiCode.GLOSS_GREEN.stain(":: orion-ops-launch v" + version + " 服务已启动(" + env + ") ::\n") +
                AnsiCode.GLOSS_GREEN.stain(":: swagger 文档      ") +
                AnsiCode.GLOSS_BLUE.stain("http://127.0.0.1:" + port + "/doc.html\n") +
                AnsiCode.GLOSS_GREEN.stain(":: druid console    ") +
                AnsiCode.GLOSS_BLUE.stain("http://127.0.0.1:" + port + "/druid/index.html\n") +
                AnsiCode.GLOSS_GREEN.stain(":: server 心跳检测    ") +
                AnsiCode.GLOSS_BLUE +
                "curl -X GET --location \"http://127.0.0.1:" + port + apiPrefix + "/server/bootstrap/health\"" +
                AnsiCode.SUFFIX;
        System.out.println(line);
    }

}
