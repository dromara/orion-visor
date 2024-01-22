package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.module.asset.convert.CommandSnippetConvert;
import com.orion.ops.module.asset.dao.CommandSnippetDAO;
import com.orion.ops.module.asset.define.cache.CommandSnippetCacheKeyDefine;
import com.orion.ops.module.asset.entity.domain.CommandSnippetDO;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetCreateRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetQueryRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetUpdateRequest;
import com.orion.ops.module.asset.entity.vo.CommandSnippetVO;
import com.orion.ops.module.asset.service.CommandSnippetService;
import com.orion.ops.module.infra.api.DataGroupRelApi;
import com.orion.ops.module.infra.enums.DataGroupTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 命令片段 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-22 15:28
 */
@Slf4j
@Service
public class CommandSnippetServiceImpl implements CommandSnippetService {

    @Resource
    private CommandSnippetDAO commandSnippetDAO;

    @Resource
    private DataGroupRelApi dataGroupRelApi;

    @Override
    public Long createCommandSnippet(CommandSnippetCreateRequest request) {
        log.info("CommandSnippetService-createCommandSnippet request: {}", JSON.toJSONString(request));
        // 转换
        CommandSnippetDO record = CommandSnippetConvert.MAPPER.to(request);
        // 插入
        int effect = commandSnippetDAO.insert(record);
        Long id = record.getId();
        log.info("CommandSnippetService-createCommandSnippet id: {}, effect: {}", id, effect);
        // 设置分组引用
        if (request.getGroupId() != null) {
            dataGroupRelApi.addGroupRel(request.getGroupId(), id);
        }
        // 删除缓存
        RedisMaps.delete(CommandSnippetCacheKeyDefine.COMMAND_SNIPPET);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateCommandSnippetById(CommandSnippetUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("CommandSnippetService-updateCommandSnippetById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        CommandSnippetDO record = commandSnippetDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        CommandSnippetDO updateRecord = CommandSnippetConvert.MAPPER.to(request);
        // 更新
        int effect = commandSnippetDAO.updateById(updateRecord);
        log.info("CommandSnippetService-updateCommandSnippetById effect: {}", effect);
        // 删除分组引用
        dataGroupRelApi.deleteByRelId(DataGroupTypeEnum.COMMAND_SNIPPET, id);
        // 设置分组引用
        if (request.getGroupId() != null) {
            dataGroupRelApi.addGroupRel(request.getGroupId(), id);
        }
        // 删除缓存
        RedisMaps.delete(CommandSnippetCacheKeyDefine.COMMAND_SNIPPET);
        return effect;
    }

    @Override
    public List<CommandSnippetVO> getCommandSnippetList(CommandSnippetQueryRequest request) {
        // FIXME 查询缓存
        // fixme 查询分组
        // 条件
        LambdaQueryWrapper<CommandSnippetDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return commandSnippetDAO.of(wrapper).list(CommandSnippetConvert.MAPPER::to);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteCommandSnippetById(Long id) {
        log.info("CommandSnippetService-deleteCommandSnippetById id: {}", id);
        // 检查数据是否存在
        CommandSnippetDO record = commandSnippetDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = commandSnippetDAO.deleteById(id);
        log.info("CommandSnippetService-deleteCommandSnippetById id: {}, effect: {}", id, effect);
        // 删除分组引用
        dataGroupRelApi.deleteByRelId(DataGroupTypeEnum.COMMAND_SNIPPET, id);
        // 删除缓存
        RedisMaps.delete(CommandSnippetCacheKeyDefine.COMMAND_SNIPPET, id);
        return effect;
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<CommandSnippetDO> buildQueryWrapper(CommandSnippetQueryRequest request) {
        return commandSnippetDAO.wrapper()
                .eq(CommandSnippetDO::getName, request.getName());
    }

}
