package com.orion.ops.module.asset.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.HostKeyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 主机秘钥 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Mapper
public interface HostKeyDAO extends IMapper<HostKeyDO> {

}
