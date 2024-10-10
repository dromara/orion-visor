package com.orion.visor.module.infra.dao;

import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.infra.entity.domain.SystemSettingDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统设置 Mapper 接口
 *
 * @author Jiahang Li
 * @version 3.0.0
 * @since 2024-9-27 18:52
 */
@Mapper
public interface SystemSettingDAO extends IMapper<SystemSettingDO> {

}
