package com.orion.ops.framework.common.constant;

/**
 * 错误信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 18:51
 */
public interface ErrorMessage {

    String MISSING = "{} 不能为空";

    String PARAM_MISSING = "参数不能为空";

    String ID_MISSING = "id 不能为空";

    String INVALID_PARAM = "参数验证失败";

    String DATA_ABSENT = "数据不存在";

    String DATA_PRESENT = "数据已存在";

    String NAME_PRESENT = "名称已存在";

    String CODE_PRESENT = "编码已存在";

    String NICKNAME_PRESENT = "花名已存在";

    String USERNAME_PRESENT = "用户名已存在";

    String ROLE_ABSENT = "角色不存在";

    String ROLE_CODE_ABSENT = "角色 [{}] 不存在";

    String INVALID_PARENT_MENU = "所选父菜单不合法";

    String PARENT_MENU_ABSENT = "父菜单不存在";

    String USERNAME_PASSWORD_ERROR = "用户名或密码错误";

    String MAX_LOGIN_FAILED = "登陆失败次数已上限";

    String USER_ABSENT = "用户不存在";

    String UNABLE_OPERATE_ADMIN_ROLE = "无法操作管理员账号";

}
