package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.Booleans;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.convert.CommandSnippetGroupConvert;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupCreateRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupDeleteRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupUpdateRequest;
import com.orion.ops.module.asset.entity.vo.CommandSnippetGroupVO;
import com.orion.ops.module.asset.service.CommandSnippetGroupService;
import com.orion.ops.module.asset.service.CommandSnippetService;
import com.orion.ops.module.infra.api.DataGroupApi;
import com.orion.ops.module.infra.api.DataGroupUserApi;
import com.orion.ops.module.infra.entity.dto.data.DataGroupCreateDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupDTO;
import com.orion.ops.module.infra.entity.dto.data.DataGroupRenameDTO;
import com.orion.ops.module.infra.enums.DataGroupTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 命令片段分组 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Slf4j
@Service
public class CommandSnippetGroupServiceImpl implements CommandSnippetGroupService {

    @Resource
    private DataGroupApi dataGroupApi;

    @Resource
    private DataGroupUserApi dataGroupUserApi;

    @Resource
    private CommandSnippetService commandSnippetService;

    @Override
    public Long createCommandSnippetGroup(CommandSnippetGroupCreateRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        log.info("CommandSnippetGroupService-createCommandSnippetGroup request: {}", JSON.toJSONString(request));
        // 创建
        DataGroupCreateDTO create = CommandSnippetGroupConvert.MAPPER.to(request);
        create.setParentId(Const.ROOT_PARENT_ID);
        return dataGroupUserApi.createDataGroup(DataGroupTypeEnum.COMMAND_SNIPPET, userId, create);
    }

    @Override
    public Integer updateCommandSnippetGroupById(CommandSnippetGroupUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("CommandSnippetGroupService-updateCommandSnippetGroupById id: {}, request: {}", id, JSON.toJSONString(request));
        // 重命名
        DataGroupRenameDTO rename = CommandSnippetGroupConvert.MAPPER.to(request);
        return dataGroupApi.renameDataGroup(rename);
    }

    @Override
    public List<CommandSnippetGroupVO> getCommandSnippetGroupList() {
        Long userId = SecurityUtils.getLoginUserId();
        // 查询分组
        return dataGroupUserApi.getDataGroupList(DataGroupTypeEnum.COMMAND_SNIPPET, userId)
                .stream()
                .sorted(Comparator.comparing(DataGroupDTO::getSort))
                .map(CommandSnippetGroupConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteCommandSnippetGroup(CommandSnippetGroupDeleteRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        Long id = request.getId();
        log.info("CommandSnippetGroupService-deleteCommandSnippetGroupById id: {}", id);
        // 删除分组
        Integer effect = dataGroupApi.deleteDataGroupById(id);
        if (Booleans.isTrue(request.getDeleteItem())) {
            // 删除组内数据
            effect += commandSnippetService.deleteByGroupId(userId, id);
        } else {
            // 移动到根节点
            effect += commandSnippetService.setGroupNull(userId, id);
        }
        return effect;
    }

}
