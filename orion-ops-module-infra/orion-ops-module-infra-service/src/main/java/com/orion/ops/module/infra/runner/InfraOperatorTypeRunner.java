package com.orion.ops.module.infra.runner;

import com.orion.ops.module.infra.define.operator.AuthenticationOperatorType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 操作类型 初始化
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 18:03
 */
@Component
public class InfraOperatorTypeRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        AuthenticationOperatorType.init();
    }

}
