package com.orion.visor.module.asset.dao;

import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.HostConnectLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 主机连接日志 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Mapper
public interface HostConnectLogDAO extends IMapper<HostConnectLogDO> {

    /**
     * 查询最近连接的 hostId
     *
     * @param userId userId
     * @param type   type
     * @param limit  limit
     * @return hostId
     */
    List<Long> selectLatestConnectHostId(@Param("userId") Long userId, @Param("type") String type, @Param("limit") Integer limit);

}
