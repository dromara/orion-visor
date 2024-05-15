package com.orion.visor.module.asset.dao;

import com.orion.visor.framework.mybatis.core.mapper.IMapper;
import com.orion.visor.module.asset.entity.domain.ExecTemplateHostDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行模板主机 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.6
 * @since 2024-4-22 12:13
 */
@Mapper
public interface ExecTemplateHostDAO extends IMapper<ExecTemplateHostDO> {

}
