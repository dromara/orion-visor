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
package org.dromara.visor.module.terminal.utils;

import org.dromara.visor.module.terminal.handler.terminal.model.response.SftpFileVO;
import cn.orionsec.kit.lang.utils.io.FileType;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.net.host.sftp.SftpFile;

import java.util.Optional;

/**
 * sftp 文件工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/12 20:49
 */
public class SftpFileUtils {

    private SftpFileUtils() {
    }

    /**
     * 转为文件
     *
     * @param file file
     * @return file
     */
    public static SftpFileVO toFile(SftpFile file) {
        SftpFileVO vo = new SftpFileVO();
        vo.setName(file.getName());
        vo.setPath(file.getPath());
        vo.setSuffix(Files1.getSuffix(file.getName()));
        vo.setSize(file.getSize());
        vo.setPermission(file.getPermission());
        vo.setUid(file.getUid());
        vo.setGid(file.getGid());
        vo.setAttr(file.getPermissionString());
        vo.setModifyTime(file.getModifyTime());
        Boolean isDir = Optional.ofNullable(FileType.of(vo.getAttr()))
                .map(FileType.DIRECTORY::equals)
                .orElse(false);
        vo.setIsDir(isDir);
        return vo;
    }

}
