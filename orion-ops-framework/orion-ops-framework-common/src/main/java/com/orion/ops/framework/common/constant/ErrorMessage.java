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

    String DATA_MODIFIED = "数据发生变更, 请刷新后重试";

    String DATA_ABSENT = "数据不存在";

    String KEY_ABSENT = "主机秘钥不存在";

    String IDENTITY_ABSENT = "主机身份不存在";

    String CONFIG_ABSENT = "配置不存在";

    String CONFIG_PRESENT = "配置已存在";

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

    String MAX_LOGIN_FAILED = "登录失败次数已上限";

    String HISTORY_ABSENT = "历史值不存在";

    String USER_ABSENT = "用户不存在";

    String HOST_ABSENT = "主机不存在";

    String GROUP_ABSENT = "分组不存在";

    String UNABLE_OPERATE_ADMIN_ROLE = "无法操作管理员账号";

    String UNSUPPORTED_CHARSET = "不支持的编码 [{}]";

    String PASSWORD_MISSING = "请输入密码";

    String BEFORE_PASSWORD_ERROR = "原密码错误";

    String DATA_NO_PERMISSION = "数据无权限";

    String ANY_NO_PERMISSION = "{}无权限";

    String SESSION_PRESENT = "会话已存在";

    String SESSION_ABSENT = "会话不存在";

    String PATH_NOT_NORMALIZE = "路径不合法";

    String OPERATE_ERROR = "操作失败";

    String UNKNOWN_TYPE = "未知类型";

    String FILE_ABSENT = "文件不存在";

    String LOG_ABSENT = "日志不存在";

    String ILLEGAL_STATUS = "当前状态不支持此操作";

}
