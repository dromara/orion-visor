package com.orion.visor.module.asset.convert;

import com.orion.visor.module.asset.entity.request.command.CommandSnippetGroupCreateRequest;
import com.orion.visor.module.asset.entity.request.command.CommandSnippetGroupUpdateRequest;
import com.orion.visor.module.asset.entity.vo.CommandSnippetGroupVO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 命令片段分组 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Mapper
public interface CommandSnippetGroupConvert {

    CommandSnippetGroupConvert MAPPER = Mappers.getMapper(CommandSnippetGroupConvert.class);

    DataGroupCreateDTO to(CommandSnippetGroupCreateRequest request);

    DataGroupRenameDTO to(CommandSnippetGroupUpdateRequest request);

    CommandSnippetGroupVO to(DataGroupDTO domain);

}
