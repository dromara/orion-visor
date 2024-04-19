package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 执行模板 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-7 18:08
 */
@Module("asset:exec-template")
public class ExecTemplateOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "exec-template:create";

    public static final String UPDATE = "exec-template:update";

    public static final String DELETE = "exec-template:delete";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建执行模板 <sb>${name}</sb>"),
                new OperatorType(M, UPDATE, "更新执行模板 <sb>${name}</sb>"),
                new OperatorType(H, DELETE, "删除执行模板 <sb>${name}</sb>"),
        };
    }

}
