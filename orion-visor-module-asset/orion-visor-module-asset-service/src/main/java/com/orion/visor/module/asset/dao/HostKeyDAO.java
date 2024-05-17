package com.orion.visor.module.asset.dao;

import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.HostKeyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 主机密钥 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Mapper
public interface HostKeyDAO extends IMapper<HostKeyDO> {

}
