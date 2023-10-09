package com.orion.ops.framework.common.utils;

import com.orion.lang.utils.time.Dates;
import com.orion.ops.framework.common.constant.Const;

/**
 * 文件名称
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/8/31 17:57
 */
public class FileNames {

    private FileNames() {
    }

    /**
     * 导出文件名称
     *
     * @param title title
     * @return name
     */
    public static String exportName(String title) {
        return title + "-" + Dates.current(Dates.YMD_HMS2) + "." + Const.SUFFIX_XLSX;
    }

}
