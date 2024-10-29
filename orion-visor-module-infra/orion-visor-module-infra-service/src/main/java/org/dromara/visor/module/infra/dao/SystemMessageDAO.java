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
package org.dromara.visor.module.infra.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.dromara.visor.framework.mybatis.core.mapper.IMapper;
import org.dromara.visor.module.infra.entity.domain.SystemMessageDO;
import org.dromara.visor.module.infra.entity.po.SystemMessageCountPO;

import java.util.List;

/**
 * 系统消息 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.8
 * @since 2024-5-11 16:29
 */
@Mapper
public interface SystemMessageDAO extends IMapper<SystemMessageDO> {

    /**
     * 查询消息数量
     *
     * @param receiverId receiverId
     * @param status     status
     * @return count
     */
    List<SystemMessageCountPO> selectSystemMessageCount(@Param("receiverId") Long receiverId,
                                                        @Param("status") Integer status);

}
