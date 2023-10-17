package com.orion.ops.module.infra.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

/**
 * 字典配置值 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-16 16:33
 */
@Module("infra:dict-value")
public class DictValueOperatorType extends InitializingOperatorTypes {

    public static final String CREATE = "dict-value:create";

    public static final String UPDATE = "dict-value:update";

    public static final String DELETE = "dict-value:delete";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, CREATE, "创建字典配置值 <sb>${key}</sb> <sb>${label}=${value}</sb>"),
                new OperatorType(M, UPDATE, "更新字典配置值 <sb>${key}</sb> <sb>${label}=${value}</sb>"),
                new OperatorType(H, DELETE, "删除字典配置值 <sb>${value}</sb>"),
        };
    }

}
