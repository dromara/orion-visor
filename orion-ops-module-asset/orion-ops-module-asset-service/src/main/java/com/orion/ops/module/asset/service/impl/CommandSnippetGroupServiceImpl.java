package com.orion.ops.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.utils.Booleans;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.convert.CommandSnippetGroupConvert;
import com.orion.ops.module.asset.dao.CommandSnippetGroupDAO;
import com.orion.ops.module.asset.define.cache.CommandSnippetCacheKeyDefine;
import com.orion.ops.module.asset.entity.domain.CommandSnippetGroupDO;
import com.orion.ops.module.asset.entity.dto.CommandSnippetGroupCacheDTO;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupCreateRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupDeleteRequest;
import com.orion.ops.module.asset.entity.request.command.CommandSnippetGroupUpdateRequest;
import com.orion.ops.module.asset.entity.vo.CommandSnippetGroupVO;
import com.orion.ops.module.asset.service.CommandSnippetGroupService;
import com.orion.ops.module.asset.service.CommandSnippetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private CommandSnippetGroupDAO commandSnippetGroupDAO;

    @Resource
    private CommandSnippetService commandSnippetService;

    @Override
    public Long createCommandSnippetGroup(CommandSnippetGroupCreateRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        log.info("CommandSnippetGroupService-createCommandSnippetGroup request: {}", JSON.toJSONString(request));
        // 转换
        CommandSnippetGroupDO record = CommandSnippetGroupConvert.MAPPER.to(request);
        record.setUserId(userId);
        // 查询数据是否冲突
        this.checkCommandSnippetGroupPresent(record);
        // 插入
        int effect = commandSnippetGroupDAO.insert(record);
        Long id = record.getId();
        log.info("CommandSnippetGroupService-createCommandSnippetGroup id: {}, effect: {}", id, effect);
        // 删除缓存
        String cacheKey = CommandSnippetCacheKeyDefine.SNIPPET_GROUP.format(userId);
        RedisMaps.delete(cacheKey);
        return id;
    }

    @Override
    public Integer updateCommandSnippetGroupById(CommandSnippetGroupUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("CommandSnippetGroupService-updateCommandSnippetGroupById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        CommandSnippetGroupDO record = commandSnippetGroupDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        CommandSnippetGroupDO updateRecord = CommandSnippetGroupConvert.MAPPER.to(request);
        updateRecord.setUserId(record.getUserId());
        // 查询数据是否冲突
        this.checkCommandSnippetGroupPresent(updateRecord);
        // 更新
        int effect = commandSnippetGroupDAO.updateById(updateRecord);
        log.info("CommandSnippetGroupService-updateCommandSnippetGroupById effect: {}", effect);
        // 删除缓存
        String cacheKey = CommandSnippetCacheKeyDefine.SNIPPET_GROUP.format(record.getUserId());
        RedisMaps.delete(cacheKey);
        return effect;
    }

    @Override
    public List<CommandSnippetGroupVO> getCommandSnippetGroupList() {
        Long userId = SecurityUtils.getLoginUserId();
        // 查询缓存
        String cacheKey = CommandSnippetCacheKeyDefine.SNIPPET_GROUP.format(userId);
        List<CommandSnippetGroupCacheDTO> list = RedisMaps.valuesJson(cacheKey, CommandSnippetCacheKeyDefine.SNIPPET_GROUP);
        if (list.isEmpty()) {
            // 查询数据库
            list = commandSnippetGroupDAO.of()
                    .createWrapper()
                    .eq(CommandSnippetGroupDO::getUserId, userId)
                    .then()
                    .list(CommandSnippetGroupConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, CommandSnippetGroupCacheDTO::new);
            // 设置缓存
            RedisMaps.putAllJson(cacheKey, s -> s.getId().toString(), list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        // 转换
        return list.stream()
                .map(CommandSnippetGroupConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteCommandSnippetGroup(CommandSnippetGroupDeleteRequest request) {
        Long id = request.getId();
        log.info("CommandSnippetGroupService-deleteCommandSnippetGroupById id: {}", id);
        // 检查数据是否存在
        CommandSnippetGroupDO record = commandSnippetGroupDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        Long userId = record.getUserId();
        // 删除
        int effect = commandSnippetGroupDAO.deleteById(id);
        log.info("CommandSnippetGroupService-deleteCommandSnippetGroupById id: {}, effect: {}", id, effect);
        if (Booleans.isTrue(request.getDeleteItem())) {
            // 删除组内数据
            commandSnippetService.deleteByGroupId(userId, id);
        } else {
            // 移动到根节点
            commandSnippetService.setGroupNull(userId, id);
        }
        // 删除缓存
        String cacheKey = CommandSnippetCacheKeyDefine.SNIPPET_GROUP.format(userId);
        RedisMaps.delete(cacheKey, id);
        return effect;
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkCommandSnippetGroupPresent(CommandSnippetGroupDO domain) {
        // 构造条件
        LambdaQueryWrapper<CommandSnippetGroupDO> wrapper = commandSnippetGroupDAO.wrapper()
                // 更新时忽略当前记录
                .ne(CommandSnippetGroupDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(CommandSnippetGroupDO::getUserId, domain.getUserId())
                .eq(CommandSnippetGroupDO::getName, domain.getName());
        // 检查是否存在
        boolean present = commandSnippetGroupDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

}
