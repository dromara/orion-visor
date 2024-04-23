package com.orion.ops.module.asset.convert;

import com.orion.ops.module.asset.entity.domain.CommandSnippetGroupDO;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupCreateRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupUpdateRequest;
import com.orion.ops.module.asset.entity.vo.CommandSnippetGroupVO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupRenameDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

    List<CommandSnippetGroupVO> to(List<CommandSnippetGroupDO> list);

}
