package com.orion.ops.module.infra.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 系统菜单 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("infra:system-menu")
public class SystemMenuOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "system-menu:create";

    public static final String UPDATE = "system-menu:update";

    public static final String UPDATE_STATUS = "system-menu:update-status";

    public static final String DELETE = "system-menu:delete";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建菜单 <sb>${name}</sb>"),
                new OperatorType(L, UPDATE, "修改菜单 <sb>${name}</sb>"),
                new OperatorType(M, UPDATE_STATUS, "修改菜单状态 <sb>${name}</sb> - <sb>${label}</sb>"),
                new OperatorType(H, DELETE, "删除菜单 <sb>${name}</sb>"),
        };
    }

}
