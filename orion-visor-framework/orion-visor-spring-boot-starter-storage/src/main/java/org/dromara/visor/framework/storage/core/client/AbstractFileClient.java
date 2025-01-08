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
package org.dromara.visor.framework.storage.core.client;

import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.lang.utils.time.Dates;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.interfaces.FileClient;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * 文件客户端 基类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 17:24
 */
public abstract class AbstractFileClient<Config extends FileClientConfig> implements FileClient {

    protected Config config;

    public AbstractFileClient(Config config) {
        this.config = config;
        // 设置默认文件客户端
        if (config.isPrimary()) {
            PrimaryFileClient.setDelegate(this);
        }
    }

    @Override
    public String upload(String path, byte[] content) throws Exception {
        return this.doUpload(path, Streams.toInputStream(content), true, true);
    }

    @Override
    public String upload(String path, byte[] content, boolean overrideIfExist) throws Exception {
        return this.doUpload(path, Streams.toInputStream(content), true, overrideIfExist);
    }

    @Override
    public String upload(String path, InputStream in) throws Exception {
        return this.doUpload(path, in, false, true);
    }

    @Override
    public String upload(String path, InputStream in, boolean autoClose) throws Exception {
        return this.doUpload(path, in, autoClose, true);
    }

    @Override
    public String upload(String path, InputStream in, boolean autoClose, boolean overrideIfExist) throws Exception {
        return this.doUpload(path, in, autoClose, overrideIfExist);
    }

    @Override
    public byte[] getContent(String path) throws Exception {
        try (InputStream in = this.getContentInputStream(path)) {
            return Streams.toByteArray(in);
        }
    }

    @Override
    public OutputStream getContentOutputStream(String path) throws Exception {
        return this.getContentOutputStream(path, false);
    }

    /**
     * 执行上传操作
     *
     * @param path            path
     * @param in              in
     * @param autoClose       autoClose
     * @param overrideIfExist 文件存在是否覆盖
     * @return path
     * @throws Exception Exception
     */
    protected abstract String doUpload(String path, InputStream in, boolean autoClose, boolean overrideIfExist) throws Exception;

    /**
     * 获取文件路径 拼接前缀
     *
     * @param path 路径
     * @return 文件名称
     */
    protected String getFilePath(String path) {
        // 文件名称
        String name = Files1.getFileName(path);
        // 名称前缀
        String prefix = Const.EMPTY;
        long current = System.currentTimeMillis();
        if (config.isTimestampPrefix()) {
            prefix = current + "_";
        }
        // 时间文件夹
        String dateDir = Const.EMPTY;
        if (config.isDateDirectory()) {
            dateDir = Dates.format(new Date(current), config.getDatePattern()) + Const.SLASH;
        }
        if (name.equals(path)) {
            // 文件名称
            return dateDir + prefix + name;
        } else {
            // 包含路径
            String parentPath = Files1.getParentPath(path);
            return dateDir + parentPath + prefix + name;
        }
    }

}
