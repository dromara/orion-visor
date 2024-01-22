package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.CommandSnippetDO;
import com.orion.ops.module.asset.entity.dto.CommandSnippetCacheDTO;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetCreateRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetQueryRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetUpdateRequest;
import com.orion.ops.module.asset.entity.vo.CommandSnippetVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 命令片段 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Mapper
public interface CommandSnippetConvert {

    CommandSnippetConvert MAPPER = Mappers.getMapper(CommandSnippetConvert.class);

    CommandSnippetDO to(CommandSnippetCreateRequest request);

    CommandSnippetDO to(CommandSnippetUpdateRequest request);

    CommandSnippetDO to(CommandSnippetQueryRequest request);

    CommandSnippetVO to(CommandSnippetDO domain);

    List<CommandSnippetVO> to(List<CommandSnippetDO> list);

    CommandSnippetVO to(CommandSnippetCacheDTO cache);

    CommandSnippetCacheDTO toCache(CommandSnippetDO domain);

}
