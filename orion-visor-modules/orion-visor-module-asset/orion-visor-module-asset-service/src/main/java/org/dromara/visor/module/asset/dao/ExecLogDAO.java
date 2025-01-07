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
package org.dromara.visor.module.asset.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.asset.entity.domain.ExecLogDO;
import org.dromara.visor.module.asset.entity.po.ExecLogCountPO;

import java.util.Date;
import java.util.List;

/**
 * 批量执行日志 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Mapper
public interface ExecLogDAO extends IMapper<ExecLogDO> {

    /**
     * 通过 id 和 source 查询
     *
     * @param id     id
     * @param source source
     * @return log
     */
    default ExecLogDO selectByIdSource(Long id, String source) {
        return this.of()
                .createWrapper()
                .eq(ExecLogDO::getId, id)
                .eq(ExecLogDO::getSource, source)
                .then()
                .getOne();
    }

    /**
     * 获取执行历史
     *
     * @param source source
     * @param userId userId
     * @param limit  limit
     * @return rows
     */
    List<ExecLogDO> selectExecHistory(@Param("source") String source,
                                      @Param("userId") Long userId,
                                      @Param("limit") Integer limit);

    /**
     * 获取执行日志统计
     *
     * @param userId    userId
     * @param source    source
     * @param startTime startTime
     * @param endTime   endTime
     * @return count
     */
    List<ExecLogCountPO> selectExecLogCount(@Param("userId") Long userId,
                                            @Param("source") String source,
                                            @Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime);

}
