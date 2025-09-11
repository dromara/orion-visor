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
package org.dromara.visor.module.asset.service;

import org.dromara.visor.module.asset.entity.request.host.HostAgentInstallRequest;
import org.dromara.visor.module.asset.entity.vo.HostAgentStatusVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 主机探针 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/27 16:06
 */
public interface HostAgentService {

    /**
     * 获取探针状态
     *
     * @param idList idList
     * @return rows
     */
    List<HostAgentStatusVO> getAgentStatus(List<Long> idList);

    /**
     * 安装探针
     *
     * @param request request
     */
    void installAgent(HostAgentInstallRequest request);

    /**
     * 上传探针发布包
     *
     * @param file file
     */
    void uploadAgentRelease(MultipartFile file);

    /**
     * 获取探针版本
     *
     * @return version
     */
    String getAgentVersion();

}
