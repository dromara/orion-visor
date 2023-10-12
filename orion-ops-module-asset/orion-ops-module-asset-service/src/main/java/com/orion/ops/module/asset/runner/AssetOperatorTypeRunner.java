package com.orion.ops.module.asset.runner;

import com.orion.ops.module.asset.define.operator.HostIdentityOperatorType;
import com.orion.ops.module.asset.define.operator.HostKeyOperatorType;
import com.orion.ops.module.asset.define.operator.HostOperatorType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 资产模块 操作类型初始化
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 18:03
 */
@Component
public class AssetOperatorTypeRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        HostOperatorType.init();
        HostKeyOperatorType.init();
        HostIdentityOperatorType.init();
    }

}
