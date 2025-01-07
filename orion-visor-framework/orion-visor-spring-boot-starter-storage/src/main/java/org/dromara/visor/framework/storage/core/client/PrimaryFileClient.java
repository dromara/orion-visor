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

import cn.orionsec.kit.lang.utils.Exceptions;
import org.dromara.visor.common.file.FileClient;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 默认文件客户端
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/14 11:01
 */
public class PrimaryFileClient implements FileClient {

    private static FileClient delegate;

    @Override
    public String upload(String path, byte[] content) throws Exception {
        return delegate.upload(path, content);
    }

    @Override
    public String upload(String path, byte[] content, boolean overrideIfExist) throws Exception {
        return delegate.upload(path, content, overrideIfExist);
    }

    @Override
    public String upload(String path, InputStream in) throws Exception {
        return delegate.upload(path, in);
    }

    @Override
    public String upload(String path, InputStream in, boolean autoClose) throws Exception {
        return delegate.upload(path, in, autoClose);
    }

    @Override
    public String upload(String path, InputStream in, boolean autoClose, boolean overrideIfExist) throws Exception {
        return delegate.upload(path, in, autoClose, overrideIfExist);
    }

    @Override
    public boolean isExists(String path) {
        return delegate.isExists(path);
    }

    @Override
    public boolean delete(String path) throws Exception {
        return delegate.delete(path);
    }

    @Override
    public byte[] getContent(String path) throws Exception {
        return delegate.getContent(path);
    }

    @Override
    public InputStream getContentInputStream(String path) throws Exception {
        return delegate.getContentInputStream(path);
    }

    @Override
    public OutputStream getContentOutputStream(String path) throws Exception {
        return delegate.getContentOutputStream(path);
    }

    @Override
    public OutputStream getContentOutputStream(String path, boolean append) throws Exception {
        return delegate.getContentOutputStream(path, append);
    }

    @Override
    public String getReturnPath(String path) {
        return delegate.getReturnPath(path);
    }

    @Override
    public String getAbsolutePath(String returnPath) {
        return delegate.getAbsolutePath(returnPath);
    }

    public static void setDelegate(FileClient delegate) {
        if (PrimaryFileClient.delegate != null) {
            // unmodified
            throw Exceptions.state();
        }
        PrimaryFileClient.delegate = delegate;
    }

}
