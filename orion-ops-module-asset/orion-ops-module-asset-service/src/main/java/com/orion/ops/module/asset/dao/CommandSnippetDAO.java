package com.orion.ops.module.asset.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.CommandSnippetDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 命令片段 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Mapper
public interface CommandSnippetDAO extends IMapper<CommandSnippetDO> {

}
