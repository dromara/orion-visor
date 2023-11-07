package com.orion.ops.module.infra.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 数据分组关联 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Module("infra:data-group-rel")
public class DataGroupRelOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "data-group-rel:create";

    public static final String UPDATE = "data-group-rel:update";

    public static final String DELETE = "data-group-rel:delete";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建数据分组关联"),
                new OperatorType(M, UPDATE, "更新数据分组关联"),
                new OperatorType(H, DELETE, "删除数据分组关联"),
        };
    }

}
