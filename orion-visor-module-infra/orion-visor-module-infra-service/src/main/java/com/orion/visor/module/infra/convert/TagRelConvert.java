package com.orion.visor.module.infra.convert;

import com.orion.visor.module.infra.entity.domain.TagRelDO;
import com.orion.visor.module.infra.entity.dto.TagCacheDTO;
import com.orion.visor.module.infra.entity.request.tag.TagRelQueryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 标签引用 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-6 16:54
 */
@Mapper
public interface TagRelConvert {

    TagRelConvert MAPPER = Mappers.getMapper(TagRelConvert.class);

    TagRelDO to(TagRelQueryRequest request);

    @Mapping(target = "name", source = "tagName")
    @Mapping(target = "id", source = "tagId")
    TagCacheDTO toCache(TagRelDO domain);

    List<TagCacheDTO> toCacheList(List<TagRelDO> list);

}
