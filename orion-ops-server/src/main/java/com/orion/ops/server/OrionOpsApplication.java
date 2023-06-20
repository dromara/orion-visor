package com.orion.ops.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * application 启动类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/19 16:55
 */
@SpringBootApplication(scanBasePackages = {"com.orion.ops.server", "com.orion.ops.module"})
public class OrionOpsApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(OrionOpsApplication.class).run(args);
    }

}
