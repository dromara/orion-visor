package com.orion.ops.module.asset.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.ExecHostLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 批量执行主机日志 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.1
 * @since 2024-3-11 14:05
 */
@Mapper
public interface ExecHostLogDAO extends IMapper<ExecHostLogDO> {

}
