package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.dto.TagCacheDTO;
import com.orion.ops.module.infra.entity.dto.tag.TagDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 标签枚举 对外对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
@Mapper
public interface TagProviderConvert {

    TagProviderConvert MAPPER = Mappers.getMapper(TagProviderConvert.class);

    List<TagDTO> toList(List<TagCacheDTO> cache);

}
