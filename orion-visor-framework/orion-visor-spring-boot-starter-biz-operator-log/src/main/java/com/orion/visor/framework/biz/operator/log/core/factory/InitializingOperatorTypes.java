package com.orion.visor.framework.biz.operator.log.core.factory;

import com.orion.lang.utils.Arrays1;
import com.orion.visor.framework.biz.operator.log.core.annotation.Module;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import javax.annotation.PostConstruct;

/**
 * 操作类型初始化器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/10/13 17:45
 */
public abstract class InitializingOperatorTypes implements OperatorTypeDefinition {

    @PostConstruct
    public void init() {
        // 获取模块注解
        Module moduleDefinition = this.getClass().getAnnotation(Module.class);
        if (moduleDefinition == null) {
            return;
        }
        // 获取类型
        OperatorType[] types = this.types();
        if (Arrays1.isEmpty(types)) {
            return;
        }
        // 定义类型
        String module = moduleDefinition.value();
        for (OperatorType type : types) {
            type.setModule(module);
            OperatorTypeHolder.set(type);
        }
    }

}
