package com.orion.ops.module.asset.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.ExecLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 执行日志 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 11:31
 */
@Mapper
public interface ExecLogDAO extends IMapper<ExecLogDO> {

}
