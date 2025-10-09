/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.common.constant;

/**
 * 配置项常量
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/14 16:15
 */
public interface ConfigKeys {

    /**
     * SFTP 文件预览大小
     */
    String SFTP_PREVIEW_SIZE = "sftp.preview-size";

    /**
     * SFTP 重复文件备份
     */
    String SFTP_UPLOAD_PRESENT_BACKUP = "sftp.upload-present-backup";

    /**
     * SFTP 备份文件名称
     */
    String SFTP_UPLOAD_BACKUP_FILE_NAME = "sftp.upload-backup-file-name";

    /**
     * 加密公钥
     */
    String ENCRYPT_PUBLIC_KEY = "encrypt.public-key";

    /**
     * 加密私钥
     */
    String ENCRYPT_PRIVATE_KEY = "encrypt.private-key";

    /**
     * 日志前端显示行数
     */
    String LOG_WEB_SCROLL_LINES = "log.web-scroll-lines";

    /**
     * 日志加载偏移行
     */
    String LOG_TRACKER_LOAD_LINES = "log.tracker-load-lines";

    /**
     * 日志加载间隔毫秒
     */
    String LOG_TRACKER_LOAD_INTERVAL = "log.tracker-load-interval";

    /**
     * 是否生成详细的执行日志
     */
    String LOG_EXEC_DETAIL_ENABLED = "log.exec-detail.enabled";

    /**
     * 凭证有效期 分
     */
    String LOGIN_LOGIN_SESSION_TIME = "login.login-session-time";

    /**
     * 允许多端登录
     */
    String LOGIN_ALLOW_MULTI_DEVICE = "login.allow-multi-device";

    /**
     * 允许凭证续签
     */
    String LOGIN_ALLOW_REFRESH = "login.allow-refresh";

    /**
     * 凭证续签最大次数
     */
    String LOGIN_MAX_REFRESH_COUNT = "login.max-refresh-count";

    /**
     * 凭证续签间隔分
     */
    String LOGIN_REFRESH_INTERVAL = "login.refresh-interval";

    /**
     * 登录失败锁定
     */
    String LOGIN_LOGIN_FAILED_LOCK = "login.login-failed-lock";

    /**
     * 登录失败锁定阈值
     */
    String LOGIN_LOGIN_FAILED_LOCK_THRESHOLD = "login.login-failed-lock-threshold";

    /**
     * 登录失败锁定时间 分
     */
    String LOGIN_LOGIN_FAILED_LOCK_TIME = "login.login-failed-lock-time";

    /**
     * 登录失败发信
     */
    String LOGIN_LOGIN_FAILED_SEND = "login.login-failed-send";

    /**
     * 登录失败发信阈值
     */
    String LOGIN_LOGIN_FAILED_SEND_THRESHOLD = "login.login-failed-send-threshold";

    /**
     * 是否开启自动清理命令记录
     */
    String AUTO_CLEAR_EXEC_LOG_ENABLED = "auto-clear.exec-log.enabled";

    /**
     * 自动清理命令记录保留天数
     */
    String AUTO_CLEAR_EXEC_LOG_KEEP_DAYS = "auto-clear.exec-log.keep-days";

    /**
     * 是否开启自动清理终端连接记录
     */
    String AUTO_CLEAR_TERMINAL_LOG_ENABLED = "auto-clear.terminal-log.enabled";

    /**
     * 自动清理终端连接记录保留天数
     */
    String AUTO_CLEAR_TERMINAL_LOG_KEEP_DAYS = "auto-clear.terminal-log.keep-days";

}
