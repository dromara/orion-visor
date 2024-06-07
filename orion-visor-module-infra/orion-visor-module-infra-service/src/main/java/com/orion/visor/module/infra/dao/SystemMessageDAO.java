package com.orion.visor.module.infra.dao;

import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.infra.entity.domain.SystemMessageDO;
import com.orion.visor.module.infra.entity.po.SystemMessageCountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
