/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.framework.common.constant;

import cn.orionsec.kit.lang.exception.ApplicationException;
import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;

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

    String PARAM_ERROR = "参数错误";

    String ID_MISSING = "id 不能为空";

    String INVALID_PARAM = "参数验证失败";

    String DATA_MODIFIED = "数据发生变更, 请刷新后重试";

    String DATA_ABSENT = "数据不存在";

    String KEY_ABSENT = "主机密钥不存在";

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

    String HOST_TYPE_ERROR = "主机类型错误";

    String HOST_NOT_ENABLED = "主机未启用";

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

    String ERROR_TYPE = "错误的类型";

    String FILE_ABSENT = "文件不存在";

    String FILE_ABSENT_CLEAR = "文件不存在 (可能已被清理)";

    String LOG_ABSENT = "日志不存在";

    String TASK_ABSENT = "任务不存在";

    String CONNECT_ERROR = "连接失败";

    String AUTH_ERROR = "认证失败";

    String FILE_UPLOAD_ERROR = "文件上传失败";

    String SCRIPT_UPLOAD_ERROR = "脚本上传失败";

    String EXEC_ERROR = "执行失败";

    String ILLEGAL_STATUS = "当前状态不支持此操作";

    String CHECK_AUTHORIZED_HOST = "请选择已授权的主机";

    String FILE_READ_ERROR = "文件读取失败";

    String FILE_READ_ERROR_CLEAR = "文件读取失败 (可能已被清理)";

    String PLEASE_CHECK_HOST_SSH = "请检查主机 {} 是否存在/权限/SSH配置";

    String CLIENT_ABORT = "手动中断";

    String UNABLE_DOWNLOAD_FOLDER = "无法下载文件夹";

    /**
     * 是否为业务异常
     *
     * @param ex ex
     * @return biz exception
     */
    static boolean isBizException(Exception ex) {
        if (ex == null) {
            return false;
        }
        return ex instanceof InvalidArgumentException
                || ex instanceof IllegalArgumentException
                || ex instanceof ApplicationException;
    }

    /**
     * 获取错误信息
     *
     * @param ex         ex
     * @param defaultMsg defaultMsg
     * @return message
     */
    static String getErrorMessage(Exception ex, String defaultMsg) {
        if (ex == null) {
            return null;
        }
        String message = ex.getMessage();
        if (message == null) {
            return defaultMsg;
        }
        // 业务异常
        if (isBizException(ex)) {
            return message;
        }
        return defaultMsg;
    }

}
