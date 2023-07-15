package com.orion.ops.module.infra.dao;

import com.orion.ops.module.infra.entity.domain.SystemRoleMenuDO;
import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色菜单关联 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
@Mapper
public interface SystemRoleMenuDAO extends IMapper<SystemRoleMenuDO> {

}
