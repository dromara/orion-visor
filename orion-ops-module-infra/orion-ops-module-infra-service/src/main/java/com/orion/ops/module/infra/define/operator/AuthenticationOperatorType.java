package com.orion.ops.module.infra.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.L;

/**
 * 认证服务 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 19:06
 */
@Module("infra:authentication")
public class AuthenticationOperatorType extends InitializingOperatorTypes {

    public static final String LOGIN = "authentication:login";

    public static final String LOGOUT = "authentication:logout";

    public static final String UPDATE_PASSWORD = "authentication:update-password";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(L, LOGIN, "登录系统"),
                new OperatorType(L, LOGOUT, "登出系统"),
                new OperatorType(L, UPDATE_PASSWORD, "修改密码"),
        };
    }

}
