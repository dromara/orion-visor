package com.orion.ops.framework.common.utils;

import com.orion.lang.utils.Objects1;
import com.orion.ops.framework.common.constant.AppConst;
import com.orion.ops.framework.common.constant.Const;

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
        if (isWindows) {
            // windows
            return "C:/Users/" + username;
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
                + "/" + AppConst.ORION_OPS_PRO;
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

}
