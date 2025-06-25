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
package org.dromara.visor.module.exec.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.exec.entity.domain.ExecHostLogDO;
import org.dromara.visor.module.exec.entity.po.ExecHostLogCountPO;

import java.util.Date;
import java.util.List;

/**
 * 批量执行主机日志 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 14:05
 */
@Mapper
public interface ExecHostLogDAO extends IMapper<ExecHostLogDO> {

    /**
     * 通过 logId 查询
     *
     * @param logId logId
     * @return rows
     */
    default List<ExecHostLogDO> selectByLogId(Long logId) {
        return this.of()
                .createWrapper()
                .eq(ExecHostLogDO::getLogId, logId)
                .then()
                .list();
    }

    /**
     * 通过 logId 查询
     *
     * @param id    id
     * @param logId logId
     * @return row
     */
    default ExecHostLogDO selectByIdAndLogId(Long id, Long logId) {
        return this.of()
                .createWrapper()
                .eq(ExecHostLogDO::getId, id)
                .eq(ExecHostLogDO::getLogId, logId)
                .then()
                .get();
    }

    /**
     * 通过 logIdList 查询
     *
     * @param logIdList logIdList
     * @return rows
     */
    default List<ExecHostLogDO> selectByLogIdList(List<Long> logIdList) {
        return this.of()
                .createWrapper()
                .in(ExecHostLogDO::getLogId, logIdList)
                .then()
                .list();
    }

    /**
     * 获取执行主机日志统计
     *
     * @param source    source
     * @param startTime startTime
     * @param endTime   endTime
     * @return count
     */
    List<ExecHostLogCountPO> selectExecHostLogCount(@Param("source") String source,
                                                    @Param("startTime") Date startTime,
                                                    @Param("endTime") Date endTime);

}
