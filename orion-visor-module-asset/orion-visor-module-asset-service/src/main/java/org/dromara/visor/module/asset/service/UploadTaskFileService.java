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

import org.dromara.visor.module.asset.entity.vo.UploadTaskFileVO;

import java.util.List;

/**
 * 上传任务文件 服务类
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-8 10:31
 */
public interface UploadTaskFileService {

    /**
     * 查询上传任务文件
     *
     * @param taskId taskId
     * @return row
     */
    List<UploadTaskFileVO> getFileByTaskId(Long taskId);

    /**
     * 通过 taskId 删除
     *
     * @param taskId taskId
     * @return effect
     */
    Integer deleteFileByTaskId(Long taskId);

    /**
     * 通过 taskId 删除
     *
     * @param taskIdList taskIdList
     * @return effect
     */
    Integer deleteFileByTaskIdList(List<Long> taskIdList);

}
