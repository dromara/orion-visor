package com.orion.visor.framework.common.utils;

import com.orion.lang.utils.Objects1;
import com.orion.lang.utils.Systems;
import com.orion.lang.utils.io.Files1;
import com.orion.visor.framework.common.constant.AppConst;
import com.orion.visor.framework.common.constant.Const;

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
