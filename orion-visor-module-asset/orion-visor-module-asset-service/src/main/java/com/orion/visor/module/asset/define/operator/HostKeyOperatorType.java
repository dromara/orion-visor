package com.orion.visor.module.asset.define.operator;

import com.orion.visor.framework.biz.operator.log.core.annotation.Module;
import com.orion.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;
import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.L;

/**
 * 主机密钥 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("asset:host-key")
public class HostKeyOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "host-key:create";

    public static final String UPDATE = "host-key:update";

    public static final String DELETE = "host-key:delete";

    public static final String GRANT = "host-key:grant";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建主机密钥 <sb>${name}</sb>"),
                new OperatorType(L, UPDATE, "修改主机密钥 <sb>${name}</sb>"),
                new OperatorType(H, DELETE, "删除主机密钥 <sb>${name}</sb>"),
                new OperatorType(H, GRANT, "将主机密钥权限授予 <sb>${grantType}</sb> <sb>${grantName}</sb>"),
        };
    }

}
