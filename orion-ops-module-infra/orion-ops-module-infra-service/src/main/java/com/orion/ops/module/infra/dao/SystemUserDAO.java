package com.orion.ops.module.infra.dao;

import com.orion.ops.module.infra.entity.domain.SystemUserDO;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
@Mapper
public interface SystemUserDAO extends IMapper<SystemUserDO> {

}
