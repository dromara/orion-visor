package com.orion.ops.module.infra.dao;

import com.orion.ops.framework.mybatis.core.mapper.IMapper;
import com.orion.ops.module.infra.entity.domain.TagDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签枚举 Mapper 接口
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
@Mapper
public interface TagDAO extends IMapper<TagDO> {

}
