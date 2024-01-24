package com.orion.ops.module.asset.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.asset.entity.domain.CommandSnippetGroupDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 命令片段分组 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Mapper
public interface CommandSnippetGroupDAO extends IMapper<CommandSnippetGroupDO> {

}
