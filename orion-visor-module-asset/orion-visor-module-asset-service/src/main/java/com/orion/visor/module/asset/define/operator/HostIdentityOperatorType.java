package com.orion.visor.module.asset.define.operator;

import com.orion.visor.framework.biz.operator.log.core.annotation.Module;
import com.orion.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;
import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.L;

/**
 * 主机身份 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("asset:host-identity")
public class HostIdentityOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "host-identity:create";

    public static final String UPDATE = "host-identity:update";

    public static final String DELETE = "host-identity:delete";

    public static final String GRANT = "host-identity:grant";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建主机身份 <sb>${name}</sb>"),
                new OperatorType(L, UPDATE, "修改主机身份 <sb>${name}</sb>"),
                new OperatorType(H, DELETE, "删除主机身份 <sb>${name}</sb>"),
                new OperatorType(H, GRANT, "将主机身份权限授予 <sb>${grantType}</sb> <sb>${grantName}</sb>"),
        };
    }

}
