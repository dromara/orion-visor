package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 主机分组 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/10 17:30
 */
@Module("asset:host-group")
public class HostGroupOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "host-group:create";

    public static final String RENAME = "host-group:rename";

    public static final String MOVE = "host-group:move";

    public static final String DELETE = "host-group:delete";

    public static final String UPDATE_REL = "host-group:update-rel";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建主机分组 <sb>${name}</sb>"),
                new OperatorType(L, RENAME, "重命名主机分组 <sb>${before}</sb> -> <sb>${name}</sb>"),
                new OperatorType(L, MOVE, "移动主机分组 <sb>${source}</sb> 到 <sb>${target}(${position})</sb>"),
                new OperatorType(H, DELETE, "删除主机分组 <sb>${name}</sb>"),
                new OperatorType(M, UPDATE_REL, "修改分组内主机 <sb>${name}</sb>"),
        };
    }

}
