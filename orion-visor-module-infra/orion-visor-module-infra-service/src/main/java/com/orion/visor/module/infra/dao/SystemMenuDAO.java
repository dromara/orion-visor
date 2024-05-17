package com.orion.visor.module.infra.dao;

import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.infra.entity.domain.SystemMenuDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 22:31
 */
@Mapper
public interface SystemMenuDAO extends IMapper<SystemMenuDO> {

}
