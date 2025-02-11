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
package org.dromara.visor.module.infra.utils;

import cn.orionsec.kit.ext.process.ProcessAwaitExecutor;
import cn.orionsec.kit.lang.support.Attempt;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.crypto.Signatures;
import cn.orionsec.kit.lang.utils.io.Streams;
import org.dromara.visor.common.constant.Const;

import java.io.ByteArrayOutputStream;

/**
 * 系统 UUID 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/1/16 11:07
 */
public class SystemUuidUtils {

    private static String uuid;

    private SystemUuidUtils() {
    }

    /**
     * 获取系统 uuid
     *
     * @return uuid
     */
    public static String getSystemUuid() {
        if (SystemUuidUtils.uuid != null) {
            return SystemUuidUtils.uuid;
        }
        String[][] cmd = new String[][]{
                new String[]{"/bin/sh", "-c", "cat /sys/class/dmi/id/product_serial"},
                new String[]{"/bin/bash", "-c", "cat /sys/class/dmi/id/product_serial"},
                new String[]{"/bin/sh", "-c", "dmidecode -s system-uuid"},
                new String[]{"/bin/bash", "-c", "dmidecode -s system-uuid"},
                new String[]{"cmd", "/c", "wmic csproduct get uuid"}
        };
        for (String[] s : cmd) {
            try {
                String uuid = SystemUuidUtils.getCommandOutput(s);
                if (Strings.isBlank(uuid)) {
                    continue;
                }
                // 去除符号并且转为大写
                uuid = uuid.replaceAll(Const.DASHED, Const.EMPTY)
                        .toUpperCase()
                        .trim();
                // 去除 \n
                String extraUuid = Arrays1.last(uuid.trim().split(Const.LF));
                if (!Strings.isBlank(extraUuid)) {
                    uuid = extraUuid.trim();
                }
                // 去除 :
                extraUuid = Arrays1.last(uuid.trim().split(Const.COLON));
                if (!Strings.isBlank(extraUuid)) {
                    uuid = extraUuid.trim();
                }
                return SystemUuidUtils.uuid = Signatures.md5(uuid);
            } catch (Exception e) {
                // IGNORED
            }
        }
        return SystemUuidUtils.uuid = Const.UNKNOWN;
    }

    /**
     * 获取输出结果
     *
     * @param command command
     * @return result
     */
    public static String getCommandOutput(String[] command) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ProcessAwaitExecutor executor = new ProcessAwaitExecutor(command);
        try {
            executor.streamHandler(i -> Attempt.uncheck(Streams::transfer, i, out))
                    .waitFor()
                    .sync()
                    .exec();
            return out.toString();
        } finally {
            Streams.close(out);
            Streams.close(executor);
        }
    }

}
