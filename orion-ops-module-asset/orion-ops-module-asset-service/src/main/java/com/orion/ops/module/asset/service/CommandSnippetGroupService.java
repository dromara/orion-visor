package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupCreateRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupDeleteRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupUpdateRequest;
import com.orion.ops.module.asset.entity.vo.CommandSnippetGroupVO;

import java.util.List;

/**
 * 命令片段分组 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
public interface CommandSnippetGroupService {

    /**
     * 创建命令片段分组
     *
     * @param request request
     * @return id
     */
    Long createCommandSnippetGroup(CommandSnippetGroupCreateRequest request);

    /**
     * 更新命令片段分组
     *
     * @param request request
     * @return effect
     */
    Integer updateCommandSnippetGroupById(CommandSnippetGroupUpdateRequest request);

    /**
     * 查询全部命令片段分组
     *
     * @return rows
     */
    List<CommandSnippetGroupVO> getCommandSnippetGroupList();

    /**
     * 删除命令片段分组
     *
     * @param request request
     * @return effect
     */
    Integer deleteCommandSnippetGroup(CommandSnippetGroupDeleteRequest request);

    /**
     * 清理未使用的分组
     */
    void clearUnusedGroup();

}
