package com.orion.ops.module.asset.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.HostConnectLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 主机连接日志 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Mapper
public interface HostConnectLogDAO extends IMapper<HostConnectLogDO> {

}
