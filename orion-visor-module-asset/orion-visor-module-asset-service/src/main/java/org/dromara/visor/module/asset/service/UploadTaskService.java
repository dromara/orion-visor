/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   lijiahangmax ljh1553488six@139.com - author
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

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.dromara.visor.module.asset.entity.domain.UploadTaskDO;
import org.dromara.visor.module.asset.entity.request.upload.UploadTaskClearRequest;
import org.dromara.visor.module.asset.entity.request.upload.UploadTaskCreateRequest;
import org.dromara.visor.module.asset.entity.request.upload.UploadTaskQueryRequest;
import org.dromara.visor.module.asset.entity.request.upload.UploadTaskRequest;
import org.dromara.visor.module.asset.entity.vo.UploadTaskCreateVO;
import org.dromara.visor.module.asset.entity.vo.UploadTaskStatusVO;
import org.dromara.visor.module.asset.entity.vo.UploadTaskVO;

import java.util.List;

/**
 * 上传任务 服务类
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
public interface UploadTaskService {

    /**
     * 创建上传任务
     *
     * @param request request
     * @return result
     */
    UploadTaskCreateVO createUploadTask(UploadTaskCreateRequest request);

    /**
     * 查询上传任务
     *
     * @param id id
     * @return row
     */
    UploadTaskVO getUploadTask(Long id);

    /**
     * 分页查询上传任务
     *
     * @param request request
     * @return rows
     */
    DataGrid<UploadTaskVO> getUploadTaskPage(UploadTaskQueryRequest request);

    /**
     * 获取上传任务状态
     *
     * @param idList     idList
     * @param queryFiles queryFiles
     * @return rows
     */
    List<UploadTaskStatusVO> getUploadTaskStatus(List<Long> idList, Boolean queryFiles);

    /**
     * 获取上传任务数量
     *
     * @param request request
     * @return count
     */
    Long getUploadTaskCount(UploadTaskQueryRequest request);

    /**
     * 清理上传任务
     *
     * @param request request
     * @return count
     */
    Integer clearUploadTask(UploadTaskClearRequest request);

    /**
     * 删除上传任务
     *
     * @param id id
     * @return effect
     */
    Integer deleteUploadTaskById(Long id);

    /**
     * 批量删除上传任务
     *
     * @param idList idList
     * @return effect
     */
    Integer deleteUploadTaskByIdList(List<Long> idList);

    /**
     * 开始上传
     *
     * @param id id
     */
    void startUploadTask(Long id);

    /**
     * 取消上传
     *
     * @param request request
     */
    void cancelUploadTask(UploadTaskRequest request);

    /**
     * 删除上传交换区的文件
     *
     * @param id id
     */
    void clearUploadSwapFiles(Long id);

    /**
     * 删除上传交换区的文件
     *
     * @param idList idList
     */
    void clearUploadSwapFiles(List<Long> idList);

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    LambdaQueryWrapper<UploadTaskDO> buildQueryWrapper(UploadTaskQueryRequest request);

}
