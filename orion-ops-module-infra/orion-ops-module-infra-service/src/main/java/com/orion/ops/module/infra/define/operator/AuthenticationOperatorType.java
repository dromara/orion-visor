package com.orion.ops.module.infra.define.operator;

import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.L;
import static com.orion.ops.framework.biz.operator.log.core.holder.OperatorTypeHolder.set;

/**
 * 认证服务 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 19:06
 */
public class AuthenticationOperatorType {

    private static final String MODULE = "infra:authentication";

    public static final String LOGIN = "authentication:login";

    public static final String LOGOUT = "authentication:logout";

    public static final String UPDATE_PASSWORD = "authentication:update-password";

    public static void init() {
        set(new OperatorType(L, MODULE, LOGIN, "登陆系统"));
        set(new OperatorType(L, MODULE, LOGOUT, "登出系统"));
        set(new OperatorType(L, MODULE, UPDATE_PASSWORD, "修改密码"));
    }

}
