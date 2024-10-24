/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.framework.common.utils;

import com.orion.lang.utils.time.Dates;
import com.orion.visor.framework.common.constant.Const;

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
