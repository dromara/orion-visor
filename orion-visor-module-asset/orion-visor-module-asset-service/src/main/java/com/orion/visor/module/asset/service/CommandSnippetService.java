package com.orion.visor.module.asset.service;

import com.orion.visor.module.asset.entity.request.command.CommandSnippetCreateRequest;
import com.orion.visor.module.asset.entity.request.command.CommandSnippetUpdateRequest;
import com.orion.visor.module.asset.entity.vo.CommandSnippetVO;
import com.orion.visor.module.asset.entity.vo.CommandSnippetWrapperVO;

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
     * 查询命令片段
     *
     * @return rows
     */
    CommandSnippetWrapperVO getCommandSnippet();

    /**
     * 查询全部命令片段
     *
     * @return rows
     */
    List<CommandSnippetVO> getCommandSnippetList();

    /**
     * 删除命令片段
     *
     * @param id id
     * @return effect
     */
    Integer deleteCommandSnippetById(Long id);

    /**
     * 设置分组为 null
     *
     * @param userId  userId
     * @param groupId groupId
     * @return effect
     */
    Integer setGroupNull(Long userId, Long groupId);

    /**
     * 通过 groupId 删除
     *
     * @param userId  userId
     * @param groupId groupId
     * @return effect
     */
    Integer deleteByGroupId(Long userId, Long groupId);

}
