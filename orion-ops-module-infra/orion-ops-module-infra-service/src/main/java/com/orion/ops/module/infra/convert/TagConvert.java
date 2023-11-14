package com.orion.ops.module.infra.convert;

import com.orion.ops.module.infra.entity.domain.TagDO;
import com.orion.ops.module.infra.entity.dto.TagCacheDTO;
import com.orion.ops.module.infra.entity.request.tag.TagCreateRequest;
import com.orion.ops.module.infra.entity.request.tag.TagQueryRequest;
import com.orion.ops.module.infra.entity.vo.TagVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 标签枚举 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-5 11:58
 */
@Mapper
public interface TagConvert {

    TagConvert MAPPER = Mappers.getMapper(TagConvert.class);

    TagDO to(TagCreateRequest request);

    TagDO to(TagQueryRequest request);

    TagVO to(TagDO domain);

    TagVO to(TagCacheDTO cache);

    TagCacheDTO toCache(TagDO domain);

    List<TagVO> to(List<TagDO> list);

}
