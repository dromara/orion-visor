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
package com.orion.visor.module.asset.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.ExecJobHostDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 计划任务主机 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Mapper
public interface ExecJobHostDAO extends IMapper<ExecJobHostDO> {

    /**
     * 通过 hostId 获取 jobId
     *
     * @param jobId jobId
     * @return hostId
     */
    default List<Long> selectHostIdByJobId(Long jobId) {
        return this.of()
                .createWrapper()
                .select(ExecJobHostDO::getHostId)
                .eq(ExecJobHostDO::getJobId, jobId)
                .then()
                .list(ExecJobHostDO::getHostId);
    }

    /**
     * 通过 jobId 删除
     *
     * @param jobId jobId
     * @return effect
     */
    default Integer deleteByJobId(Long jobId) {
        LambdaQueryWrapper<ExecJobHostDO> wrapper = this.wrapper()
                .eq(ExecJobHostDO::getJobId, jobId);
        return this.delete(wrapper);
    }

    /**
     * 通过 jobId 删除
     *
     * @param jobIdList jobIdList
     * @return effect
     */
    default Integer deleteByJobIdList(List<Long> jobIdList) {
        LambdaQueryWrapper<ExecJobHostDO> wrapper = this.wrapper()
                .in(ExecJobHostDO::getJobId, jobIdList);
        return this.delete(wrapper);
    }

    /**
     * 通过 hostId 删除
     *
     * @param hostId hostId
     * @return effect
     */
    default Integer deleteByHostId(Long hostId) {
        LambdaQueryWrapper<ExecJobHostDO> wrapper = this.wrapper()
                .eq(ExecJobHostDO::getHostId, hostId);
        return this.delete(wrapper);
    }

    /**
     * 通过 hostId 删除
     *
     * @param hostIdList hostIdList
     * @return effect
     */
    default Integer deleteByHostIdList(List<Long> hostIdList) {
        LambdaQueryWrapper<ExecJobHostDO> wrapper = this.wrapper()
                .in(ExecJobHostDO::getHostId, hostIdList);
        return this.delete(wrapper);
    }

}
