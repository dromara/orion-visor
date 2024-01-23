package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.convert.CommandSnippetConvert;
import com.orion.ops.module.asset.dao.CommandSnippetDAO;
import com.orion.ops.module.asset.define.cache.CommandSnippetCacheKeyDefine;
import com.orion.ops.module.asset.entity.domain.CommandSnippetDO;
import com.orion.ops.module.asset.entity.dto.CommandSnippetCacheDTO;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetCreateRequest;
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
import java.util.stream.Collectors;

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
        Long userId = SecurityUtils.getLoginUserId();
        log.info("CommandSnippetService-createCommandSnippet request: {}", JSON.toJSONString(request));
        // 转换
        CommandSnippetDO record = CommandSnippetConvert.MAPPER.to(request);
        record.setUserId(userId);
        // 插入
        int effect = commandSnippetDAO.insert(record);
        Long id = record.getId();
        log.info("CommandSnippetService-createCommandSnippet id: {}, effect: {}", id, effect);
        // 设置分组引用
        if (request.getGroupId() != null) {
            dataGroupRelApi.addGroupRel(request.getGroupId(), id);
        }
        // 删除缓存
        String cacheKey = CommandSnippetCacheKeyDefine.COMMAND_SNIPPET.format(userId);
        RedisMaps.delete(cacheKey);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateCommandSnippetById(CommandSnippetUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        Long userId = SecurityUtils.getLoginUserId();
        log.info("CommandSnippetService-updateCommandSnippetById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        CommandSnippetDO record = commandSnippetDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        CommandSnippetDO updateRecord = CommandSnippetConvert.MAPPER.to(request);
        // 更新
        int effect = commandSnippetDAO.updateById(updateRecord);
        log.info("CommandSnippetService-updateCommandSnippetById effect: {}", effect);
        // fixme 删除分组引用
        dataGroupRelApi.deleteByRelId(DataGroupTypeEnum.COMMAND_SNIPPET, id);
        // 设置分组引用
        if (request.getGroupId() != null) {
            dataGroupRelApi.addGroupRel(request.getGroupId(), id);
        }
        // 删除缓存
        String cacheKey = CommandSnippetCacheKeyDefine.COMMAND_SNIPPET.format(userId);
        RedisMaps.delete(cacheKey);
        return effect;
    }

    @Override
    public List<CommandSnippetVO> getCommandSnippetList() {
        Long userId = SecurityUtils.getLoginUserId();
        String cacheKey = CommandSnippetCacheKeyDefine.COMMAND_SNIPPET.format(userId);
        // fixme 查询分组

        // 查询缓存
        List<CommandSnippetCacheDTO> list = RedisMaps.valuesJson(cacheKey, CommandSnippetCacheKeyDefine.COMMAND_SNIPPET);
        if (list.isEmpty()) {
            // 查询数据库
            list = commandSnippetDAO.of()
                    .createWrapper()
                    .eq(CommandSnippetDO::getUserId, userId)
                    .then()
                    .list(CommandSnippetConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, CommandSnippetCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(CommandSnippetCacheKeyDefine.COMMAND_SNIPPET, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(CommandSnippetConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteCommandSnippetById(Long id) {
        Long userId = SecurityUtils.getLoginUserId();
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
        String cacheKey = CommandSnippetCacheKeyDefine.COMMAND_SNIPPET.format(userId);
        RedisMaps.delete(cacheKey, id);
        return effect;
    }

}
