package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.request.command.CommandSnippetCreateRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetQueryRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetUpdateRequest;
import com.orion.ops.module.asset.entity.vo.CommandSnippetVO;

import java.util.List;

/**
 * 命令片段 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
public interface CommandSnippetService {

    /**
     * 创建命令片段
     *
     * @param request request
     * @return id
     */
    Long createCommandSnippet(CommandSnippetCreateRequest request);

    /**
     * 更新命令片段
     *
     * @param request request
     * @return effect
     */
    Integer updateCommandSnippetById(CommandSnippetUpdateRequest request);

    /**
     * 查询全部命令片段
     *
     * @param request request
     * @return rows
     */
    List<CommandSnippetVO> getCommandSnippetList(CommandSnippetQueryRequest request);

    /**
     * 删除命令片段
     *
     * @param id id
     * @return effect
     */
    Integer deleteCommandSnippetById(Long id);

}
