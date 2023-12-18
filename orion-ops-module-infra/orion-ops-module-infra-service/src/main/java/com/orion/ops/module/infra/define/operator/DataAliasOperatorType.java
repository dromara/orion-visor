package com.orion.ops.module.infra.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 数据别名 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-18 17:37
 */
@Module("infra:data-alias")
public class DataAliasOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "data-alias:create";

    public static final String UPDATE = "data-alias:update";

    public static final String DELETE = "data-alias:delete";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建数据别名"),
                new OperatorType(M, UPDATE, "更新数据别名"),
                new OperatorType(H, DELETE, "删除数据别名"),
        };
    }

}
