package com.orion.visor.module.infra.define.operator;

import com.orion.visor.framework.biz.operator.log.core.annotation.Module;
import com.orion.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.M;

/**
 * 系统设置 操作日志类型
 *
 * @author Jiahang Li
 * @version 3.0.0
 * @since 2024-9-27 18:52
 */
@Module("infra:system-setting")
public class SystemSettingOperatorType extends InitializingOperatorTypes {

    public static final String UPDATE_TEXT = "<sb>{}</sb> - <sb>{}</sb> - <sb>{}</sb>";

    public static final String UPDATE_PARTIAL_TEXT = "<sb>{}</sb>";

    public static final String UPDATE = "system-setting:update";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(M, UPDATE, "更新系统设置 ${text}"),
        };
    }

}
