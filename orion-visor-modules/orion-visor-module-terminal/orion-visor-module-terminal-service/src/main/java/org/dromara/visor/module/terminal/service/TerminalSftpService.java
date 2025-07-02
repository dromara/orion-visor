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
package org.dromara.visor.module.terminal.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SFTP 操作 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
public interface TerminalSftpService {

    /**
     * 设置文件内容
     *
     * @param token    token
     * @param response response
     * @throws IOException IOException
     */
    void getFileContentByToken(String token, HttpServletResponse response) throws IOException;

    /**
     * 获取文件内容
     *
     * @param token token
     * @param file  file
     * @throws IOException IOException
     */
    void setFileContentByToken(String token, MultipartFile file) throws IOException;

    /**
     * 通过 transferToken 下载
     *
     * @param channelId     channelId
     * @param transferToken transferToken
     * @param response      response
     * @return body
     */
    StreamingResponseBody downloadWithTransferToken(String channelId,
                                                    String transferToken,
                                                    HttpServletResponse response);

}
