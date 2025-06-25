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
package org.dromara.visor.module.terminal.handler.guacd.constant;

import java.util.Arrays;
import java.util.List;

/**
 * guacd tunnel 常量
 * <p>
 * <a href="https://guacamole.apache.org/doc/gug/configuring-guacamole.html#vnc">docs</a>
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/3/31 13:04
 */
public interface GuacdConst {

    // -------------------- common --------------------

    /**
     * 主机
     */
    String HOSTNAME = "hostname";

    /**
     * 端口
     */
    String PORT = "port";

    /**
     * 用户名
     */
    String USERNAME = "username";

    /**
     * 密码
     */
    String PASSWORD = "password";

    /**
     * 颜色深度 8/16/24
     */
    String COLOR_DEPTH = "color-depth";

    /**
     * 无损压缩 boolean
     */
    String FORCE_LOSSLESS = "force-lossless";

    /**
     * 禁用复制 boolean
     */
    String DISABLE_COPY = "disable-copy";

    /**
     * 禁用粘贴 boolean
     */
    String DISABLE_PASTE = "disable-paste";

    /**
     * 屏幕录制文件的目录
     */
    String RECORDING_PATH = "recording-path";

    /**
     * 是否自动创建录屏目录 boolean
     */
    String CREATE_RECORDING_PATH = "create-recording-path";

    // -------------------- rdp --------------------

    /**
     * 域
     */
    String DOMAIN = "domain";

    /**
     * 安全模式 默认: any any/nla/nla-ext/tls/vmconnect/rdp
     */
    String SECURITY = "security";

    /**
     * 是否忽略证书 boolean
     */
    String IGNORE_CERT = "ignore-cert";

    /**
     * 是否禁用身份验证 boolean
     */
    String DISABLE_AUTH = "disable-auth";

    /**
     * 剪切板尾行规范化 默认: preserve  preserve/unix/windows
     */
    String NORMALIZE_CLIPBOARD = "normalize-clipboard";

    /**
     * 客户端名称 默认为 hostname
     */
    String CLIENT_NAME = "client-name";

    /**
     * 宽度 像素点
     */
    String WIDTH = "width";

    /**
     * 高度 像素点
     */
    String HEIGHT = "height";

    /**
     * 分辨率 dpi
     */
    String DPI = "dpi";

    /**
     * 时区
     */
    String TIMEZONE = "timezone";

    /**
     * 是否连接到控制台会话 boolean
     */
    String CONSOLE = "console";

    /**
     * 立即运行的程序完整路径
     */
    String INITIAL_PROGRAM = "initial-program";

    /**
     * 键盘布局
     */
    String SERVER_LAYOUT = "server-layout";

    /**
     * 重置大小时机 display-update/reconnect
     */
    String RESIZE_METHOD = "resize-method";

    /**
     * 禁用声音输出 boolean
     */
    String DISABLE_AUDIO_OUTPUT = "disable-audio";

    /**
     * 启用麦克风 boolean
     */
    String ENABLE_AUDIO_INPUT = "enable-audio-input";

    /**
     * 启用多点触控 boolean
     */
    String ENABLE_TOUCH = "enable-touch";

    /**
     * 启用打印机 boolean
     */
    String ENABLE_PRINTING = "enable-printing";

    /**
     * 打印机名称 依赖 enable-printing
     */
    String PRINTER_NAME = "printer-name";

    /**
     * 启用文件传输驱动 boolean
     */
    String ENABLE_DRIVE = "enable-drive";

    /**
     * 禁用下载 boolean 依赖 enable-drive
     */
    String DISABLE_DOWNLOAD = "disable-download";

    /**
     * 禁用上传 boolean 依赖 enable-drive
     */
    String DISABLE_UPLOAD = "disable-upload";

    /**
     * 文件系统驱动名称 依赖 enable-drive
     */
    String DRIVE_NAME = "drive-name";

    /**
     * 驱动路径 string 依赖 enable-drive
     */
    String DRIVE_PATH = "drive-path";

    /**
     * 自动创建驱动 boolean 依赖 enable-drive
     */
    String CREATE_DRIVE_PATH = "create-drive-path";

    /**
     * 启动控制台音频 boolean 依赖 console
     */
    String CONSOLE_AUDIO = "console-audio";

    /**
     * 静态管道名称 最大7个字符 多个用,分割
     */
    String STATIC_CHANNELS = "static-channels";

    /**
     * 预连接 id 依赖 security 为 security
     */
    String PRE_CONNECTION_ID = "preconnection-id";

    /**
     * 预连接信息 依赖 security 为 security
     */
    String PRE_CONNECTION_BLOB = "preconnection-blob";

    /**
     * 网关主机名
     */
    String GATEWAY_HOSTNAME = "gateway-hostname";

    /**
     * 网关端口 默认 443
     */
    String GATEWAY_PORT = "gateway-port";

    /**
     * 网关用户名
     */
    String GATEWAY_USERNAME = "gateway-username";

    /**
     * 网关密码
     */
    String GATEWAY_PASSWORD = "gateway-password";

    /**
     * 网关域
     */
    String GATEWAY_DOMAIN = "gateway-domain";

    /**
     * 负载均衡信息
     */
    String LOAD_BALANCE_INFO = "load-balance-info";

    /**
     * 启用壁纸 boolean
     */
    String ENABLE_WALLPAPER = "enable-wallpaper";

    /**
     * 启用主题 boolean
     */
    String ENABLE_THEMING = "enable-theming";

    /**
     * 启动平滑字体 boolean
     */
    String ENABLE_FONT_SMOOTHING = "enable-font-smoothing";

    /**
     * 启用窗口拖动 boolean
     */
    String ENABLE_FULL_WINDOW_DRAG = "enable-full-window-drag";

    /**
     * 启用桌面合成 boolean
     */
    String ENABLE_DESKTOP_COMPOSITION = "enable-desktop-composition";

    /**
     * 启用菜单动画 boolean
     */
    String ENABLE_MENU_ANIMATIONS = "enable-menu-animations";

    /**
     * 禁用位图缓存 boolean
     */
    String DISABLE_BITMAP_CACHING = "disable-bitmap-caching";

    /**
     * 禁用离屏缓存 boolean
     */
    String DISABLE_OFFSCREEN_CACHING = "disable-offscreen-caching";

    /**
     * 禁用字形缓存 boolean 默认禁用
     */
    String DISABLE_GLYPH_CACHING = "disable-glyph-caching";

    /**
     * 远程应用名称
     */
    String REMOTE_APP = "remote-app";

    /**
     * 远程应用程序的工作目录
     */
    String REMOTE_APP_DIR = "remote-app-dir";

    /**
     * 远程应用程序的命令行参数
     */
    String REMOTE_APP_ARGS = "remote-app-args";

    // -------------------- vnc --------------------

    /**
     * 重试次数
     */
    String AUTO_RETRY = "autoretry";

    /**
     * 红蓝交换 boolean
     */
    String SWAP_RED_BLUE = "swap-red-blue";

    /**
     * 鼠标指针 remote/local
     */
    String CURSOR = "cursor";

    /**
     * 编码方式
     */
    String ENCODINGS = "encodings";

    /**
     * 是否只读 boolean
     */
    String READ_ONLY = "read-only";

    /**
     * 代理主机
     */
    String DEST_HOST = "dest-host";

    /**
     * 代理端口
     */
    String DEST_PORT = "dest-port";

    /**
     * 反向连接 boolean
     */
    String REVERSE_CONNECT = "reverse-connect";

    /**
     * 监听超时时间 ms
     */
    String LISTEN_TIMEOUT = "listen-timeout";

    /**
     * 是否开启音频 boolean
     */
    String ENABLE_AUDIO = "enable-audio";

    /**
     * 音频服务器 通常为 hostname
     */
    String AUDIO_SERVERNAME = "audio-servername";

    /**
     * 剪贴板编码
     */
    String CLIPBOARD_ENCODING = "clipboard-encoding";

    // -------------------- const --------------------

    String RESIZE_METHOD_DISPLAY_UPDATE = "display-update";

    String RESIZE_METHOD_RECONNECT = "reconnect";

    String DRIVE_DRIVE_NAME = "Drive";

    String SECURITY_VMCONNECT = "vmconnect";

    List<String> AUDIO_MIMETYPES = Arrays.asList("audio/L8", "audio/L16");

}
