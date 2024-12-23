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
package org.dromara.visor.framework.common.utils;

import cn.orionsec.kit.lang.utils.Objects1;
import cn.orionsec.kit.lang.utils.Systems;
import cn.orionsec.kit.lang.utils.io.Files1;
import org.dromara.visor.framework.common.constant.AppConst;
import org.dromara.visor.framework.common.constant.Const;

/**
 * 路径工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/17 10:28
 */
public class PathUtils {

    private PathUtils() {
    }

    /**
     * 获取用户根目录
     *
     * @param isWindows isWindows
     * @param username  用户名
     * @return 用户目录
     */
    public static String getHomePath(boolean isWindows, String username) {
        return getHomePath(isWindows, username, false);
    }

    /**
     * 获取用户根目录
     *
     * @param isWindows        isWindows
     * @param username         用户名
     * @param prependSeparator 是否在头部添加分隔符
     * @return 用户目录
     */
    public static String getHomePath(boolean isWindows, String username, boolean prependSeparator) {
        if (isWindows) {
            // windows
            if (prependSeparator) {
                return "/C:/Users/" + username;
            } else {
                return "C:/Users/" + username;
            }
        } else {
            // linux
            if (Const.ROOT.equals(username)) {
                return "/" + Const.ROOT;
            } else {
                return "/home/" + username;
            }
        }
    }

    /**
     * 获取应用路径
     *
     * @param isWindows isWindows
     * @param username  username
     * @return path
     */
    public static String getAppPath(boolean isWindows, String username) {
        return getHomePath(isWindows, username)
                + "/" + AppConst.ORION
                + "/" + AppConst.APP_NAME;
    }

    /**
     * 构建应用路径
     *
     * @param isWindows isWindows,
     * @param username  username
     * @param paths     paths
     * @return path
     */
    public static String buildAppPath(boolean isWindows, String username, Object... paths) {
        StringBuilder path = new StringBuilder(getAppPath(isWindows, username));
        for (Object o : paths) {
            path.append("/").append(Objects1.toString(o));
        }
        return path.toString();
    }

    /**
     * 头部添加分隔符
     *
     * @param path path
     * @return path
     */
    public static String prependSeparator(String path) {
        if (path.startsWith("/")) {
            return path;
        }
        return "/" + path;
    }

    /**
     * 获取 orion path
     *
     * @param path path
     * @return path
     */
    public static String getOrionPath(String path) {
        path = Systems.HOME_DIR
                + Files1.SEPARATOR
                + AppConst.ORION
                + Files1.SEPARATOR
                + AppConst.APP_NAME
                + Files1.SEPARATOR
                + path;
        return Files1.getPath(path);
    }

}
