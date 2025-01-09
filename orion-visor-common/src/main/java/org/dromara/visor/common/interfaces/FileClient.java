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
package org.dromara.visor.common.interfaces;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件客户端
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 16:51
 */
public interface FileClient {

    /**
     * 上传文件
     *
     * @param path    文件路径
     * @param content 文件内容
     * @return 路径
     * @throws Exception Exception
     */
    String upload(String path, byte[] content) throws Exception;

    /**
     * 上传文件
     *
     * @param path            文件路径
     * @param content         文件内容
     * @param overrideIfExist 文件存在是否覆盖
     * @return 路径
     * @throws Exception Exception
     */
    String upload(String path, byte[] content, boolean overrideIfExist) throws Exception;

    /**
     * 上传文件
     *
     * @param path 文件路径
     * @param in   in
     * @return 路径
     * @throws Exception Exception
     */
    String upload(String path, InputStream in) throws Exception;

    /**
     * 上传文件
     *
     * @param path      文件路径
     * @param in        in
     * @param autoClose autoClose
     * @return 路径
     * @throws Exception Exception
     */
    String upload(String path, InputStream in, boolean autoClose) throws Exception;

    /**
     * 上传文件
     *
     * @param path            文件路径
     * @param in              in
     * @param autoClose       autoClose
     * @param overrideIfExist 文件存在是否覆盖
     * @return 路径
     * @throws Exception Exception
     */
    String upload(String path, InputStream in, boolean autoClose, boolean overrideIfExist) throws Exception;

    /**
     * 检查文件是否存在
     *
     * @param path path
     * @return 是否存在
     */
    boolean isExists(String path);

    /**
     * 删除文件
     *
     * @param path 路径
     * @return 是否删除
     * @throws Exception Exception
     */
    boolean delete(String path) throws Exception;

    /**
     * 获取文件内容
     *
     * @param path path
     * @return bytes
     * @throws Exception Exception
     */
    byte[] getContent(String path) throws Exception;

    /**
     * 获取文件内容
     *
     * @param path path
     * @return content
     * @throws Exception Exception
     */
    String getContentAsString(String path) throws Exception;

    /**
     * 获取文件内容
     *
     * @param path    path
     * @param charset charset
     * @return content
     * @throws Exception Exception
     */
    String getContentAsString(String path, String charset) throws Exception;

    /**
     * 获取文件输入流
     *
     * @param path path
     * @return stream
     * @throws Exception Exception
     */
    InputStream getContentInputStream(String path) throws Exception;

    /**
     * 获取文件输出流
     *
     * @param path path
     * @return stream
     * @throws Exception Exception
     */
    OutputStream getContentOutputStream(String path) throws Exception;

    /**
     * 获取文件输出流
     *
     * @param path   path
     * @param append append
     * @return stream
     * @throws Exception Exception
     */
    OutputStream getContentOutputStream(String path, boolean append) throws Exception;

    /**
     * 获取返回路径 用于客户端返回
     *
     * @param path path
     * @return returnPath
     */
    String getReturnPath(String path);

    /**
     * 获取实际存储路径 用于服务端的存储
     *
     * @param returnPath returnPath
     * @return absolutePath
     */
    String getAbsolutePath(String returnPath);

}
