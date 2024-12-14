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
package org.dromara.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.framework.mybatis.core.query.Conditions;
import org.dromara.visor.module.asset.entity.domain.UploadTaskFileDO;
import org.dromara.visor.module.asset.enums.UploadTaskFileStatusEnum;

import java.util.Date;
import java.util.List;

/**
 * 上传任务文件 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-8 10:31
 */
@Mapper
public interface UploadTaskFileDAO extends IMapper<UploadTaskFileDO> {

    /**
     * 通过 taskId 查询
     *
     * @param taskId taskId
     * @return files
     */
    default List<UploadTaskFileDO> selectByTaskId(Long taskId) {
        return this.selectList(Conditions.eq(UploadTaskFileDO::getTaskId, taskId));
    }

    /**
     * 通过 taskId 查询
     *
     * @param taskId taskId
     * @return files
     */
    default List<UploadTaskFileDO> selectByTaskId(Long taskId, String status) {
        // 条件
        LambdaQueryWrapper<UploadTaskFileDO> wrapper = this.wrapper()
                .eq(UploadTaskFileDO::getTaskId, taskId)
                .eq(UploadTaskFileDO::getStatus, status);
        return this.selectList(wrapper);
    }

    /**
     * 通过 taskId hostId 更新为失败
     *
     * @param taskId       taskId
     * @param hostId       hostId
     * @param errorMessage errorMessage
     * @return effect
     */
    default int updateToFailedByTaskHostId(Long taskId, Long hostId, String errorMessage) {
        // 条件
        LambdaQueryWrapper<UploadTaskFileDO> wrapper = this.wrapper()
                .eq(UploadTaskFileDO::getTaskId, taskId)
                .eq(UploadTaskFileDO::getHostId, hostId);
        // 修改值
        UploadTaskFileDO update = new UploadTaskFileDO();
        update.setStatus(UploadTaskFileStatusEnum.FAILED.name());
        update.setErrorMessage(errorMessage);
        update.setStartTime(new Date());
        update.setEndTime(new Date());
        // 更新
        return this.update(update, wrapper);
    }

    /**
     * 通过 id 更新为取消
     *
     * @param idList idList
     * @return effect
     */
    default int updateToCanceledByIdList(List<Long> idList) {
        UploadTaskFileDO update = new UploadTaskFileDO();
        update.setStatus(UploadTaskFileStatusEnum.CANCELED.name());
        update.setStartTime(new Date());
        update.setEndTime(new Date());
        // 更新
        return this.update(update, Conditions.in(UploadTaskFileDO::getId, idList));
    }

}
