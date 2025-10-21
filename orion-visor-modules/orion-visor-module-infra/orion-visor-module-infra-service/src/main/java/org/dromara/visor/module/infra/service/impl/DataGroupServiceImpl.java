/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.infra.service.impl;

import cn.orionsec.kit.lang.utils.collect.Lists;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.enums.MovePosition;
import org.dromara.visor.common.utils.Assert;
import org.dromara.visor.common.utils.TreeUtils;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.redis.core.utils.barrier.CacheBarriers;
import org.dromara.visor.module.infra.convert.DataGroupConvert;
import org.dromara.visor.module.infra.dao.DataGroupDAO;
import org.dromara.visor.module.infra.dao.DataGroupRelDAO;
import org.dromara.visor.module.infra.define.cache.DataGroupCacheKeyDefine;
import org.dromara.visor.module.infra.entity.domain.DataGroupDO;
import org.dromara.visor.module.infra.entity.domain.DataGroupRelDO;
import org.dromara.visor.module.infra.entity.dto.DataGroupCacheDTO;
import org.dromara.visor.module.infra.entity.request.data.DataGroupCreateRequest;
import org.dromara.visor.module.infra.entity.request.data.DataGroupMoveRequest;
import org.dromara.visor.module.infra.entity.request.data.DataGroupRenameRequest;
import org.dromara.visor.module.infra.service.DataGroupRelService;
import org.dromara.visor.module.infra.service.DataGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据分组 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Slf4j
@Service
public class DataGroupServiceImpl implements DataGroupService {

    @Resource
    private DataGroupDAO dataGroupDAO;

    @Resource
    private DataGroupRelDAO dataGroupRelDAO;

    @Resource
    private DataGroupRelService dataGroupRelService;

    @Override
    public Long createDataGroup(DataGroupCreateRequest request) {
        log.info("DataGroupService-createDataGroup request: {}", JSON.toJSONString(request));
        // 转换
        DataGroupDO record = DataGroupConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkDataGroupPresent(record);
        // 查询最大排序
        Integer sort = dataGroupDAO.selectMaxSort(request.getParentId(), request.getType(), request.getUserId());
        record.setSort(sort + Const.DEFAULT_SORT);
        // 插入
        int effect = dataGroupDAO.insert(record);
        Long id = record.getId();
        log.info("DataGroupService-createDataGroup id: {}, effect: {}", id, effect);
        // 删除缓存
        this.deleteCache(request.getType(), record.getUserId());
        return id;
    }

    @Override
    public Integer renameDataGroup(DataGroupRenameRequest request) {
        Long id = Assert.notNull(request.getId(), ErrorMessage.ID_MISSING);
        String name = Assert.notBlank(request.getName());
        // 查询
        DataGroupDO record = dataGroupDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.GROUP_ABSENT);
        // 转换
        DataGroupDO updateRecord = DataGroupDO.builder()
                .id(id)
                .name(name)
                .parentId(record.getParentId())
                .build();
        // 查询数据是否冲突
        this.checkDataGroupPresent(updateRecord);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.BEFORE, record.getName());
        // 更新
        int effect = dataGroupDAO.updateById(updateRecord);
        // 删除缓存
        this.deleteCache(record.getType(), record.getUserId());
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer moveDataGroup(DataGroupMoveRequest request) {
        Long id = request.getId();
        Long targetId = request.getTargetId();
        MovePosition position = Assert.valid(MovePosition::of, request.getPosition());
        // 查询节点是否存在
        DataGroupDO moveRecord = dataGroupDAO.selectById(id);
        DataGroupDO targetRecord = dataGroupDAO.selectById(targetId);
        Assert.notNull(moveRecord, ErrorMessage.GROUP_ABSENT);
        Assert.notNull(targetRecord, ErrorMessage.GROUP_ABSENT);
        // 更新
        String type = moveRecord.getType();
        Long userId = moveRecord.getUserId();
        Long targetParentId = targetRecord.getParentId();
        int effect = 0;
        // 修改排序
        if (MovePosition.TOP.equals(position)) {
            // 移动到元素上 将大于等于 targetRecord 的排序都加 10
            dataGroupDAO.updateSort(targetParentId,
                    type,
                    userId,
                    ">=",
                    targetRecord.getSort(),
                    Const.DEFAULT_SORT);
            // 修改关联以及排序
            DataGroupDO update = DataGroupDO.builder()
                    .id(id)
                    .parentId(targetParentId)
                    .sort(targetRecord.getSort())
                    .build();
            effect = dataGroupDAO.updateById(update);
        } else if (MovePosition.IN.equals(position)) {
            // 移动到元素中 获取最大排序
            Integer newSort = dataGroupDAO.selectMaxSort(targetId, type, userId) + Const.DEFAULT_SORT;
            // 修改关联以及排序
            DataGroupDO update = DataGroupDO.builder()
                    .id(id)
                    .parentId(targetId)
                    .sort(newSort)
                    .build();
            effect = dataGroupDAO.updateById(update);
        } else if (MovePosition.BOTTOM.equals(position)) {
            // 移动到元素下 将大于 targetRecord 的排序都加 10
            dataGroupDAO.updateSort(targetParentId,
                    type,
                    userId,
                    ">",
                    targetRecord.getSort(),
                    Const.DEFAULT_SORT);
            // 修改关联以及排序
            DataGroupDO update = DataGroupDO.builder()
                    .id(id)
                    .parentId(targetParentId)
                    .sort(targetRecord.getSort() + 1)
                    .build();
            effect = dataGroupDAO.updateById(update);
        }
        // 删除缓存
        this.deleteCache(type, userId);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.SOURCE, moveRecord.getName());
        OperatorLogs.add(OperatorLogs.TARGET, targetRecord.getName());
        OperatorLogs.add(OperatorLogs.POSITION_NAME, position.name());
        return effect;
    }

    @Override
    public List<DataGroupCacheDTO> getDataGroupListByCache(String type, Long userId) {
        // 查询缓存
        String key = DataGroupCacheKeyDefine.DATA_GROUP_LIST.format(type, userId);
        List<DataGroupCacheDTO> list = RedisStrings.getJsonArray(key, DataGroupCacheKeyDefine.DATA_GROUP_LIST);
        if (Lists.isEmpty(list)) {
            // 查询数据库
            list = dataGroupDAO.of()
                    .createWrapper()
                    .eq(DataGroupDO::getType, type)
                    .eq(DataGroupDO::getUserId, userId)
                    .then()
                    .list(DataGroupConvert.MAPPER::toCache);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(list, DataGroupCacheDTO::new);
            // 设置缓存
            RedisStrings.setJson(key, DataGroupCacheKeyDefine.DATA_GROUP_LIST, list);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(list);
        return list;
    }

    @Override
    public List<DataGroupCacheDTO> getDataGroupTreeByCache(String type, Long userId) {
        // 查询缓存
        String key = DataGroupCacheKeyDefine.DATA_GROUP_TREE.format(type, userId);
        List<DataGroupCacheDTO> treeData = RedisStrings.getJsonArray(key, DataGroupCacheKeyDefine.DATA_GROUP_TREE);
        if (Lists.isEmpty(treeData)) {
            // 查询列表缓存
            List<DataGroupCacheDTO> rows = this.getDataGroupListByCache(type, userId);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(rows, DataGroupCacheDTO::new);
            if (!Lists.isEmpty(rows)) {
                // 构建树
                DataGroupCacheDTO rootNode = DataGroupCacheDTO.builder()
                        .id(Const.ROOT_PARENT_ID)
                        .sort(Const.DEFAULT_SORT)
                        .build();
                TreeUtils.buildGroupTree(rootNode, rows);
                treeData = rootNode.getChildren();
            }
            // 设置缓存
            RedisStrings.setJson(key, DataGroupCacheKeyDefine.DATA_GROUP_LIST, treeData);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(treeData);
        return treeData;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteDataGroupById(Long id) {
        log.info("DataGroupService-deleteDataGroupById id: {}", id);
        // 检查数据是否存在
        DataGroupDO record = dataGroupDAO.selectById(id);
        Assert.notNull(record, ErrorMessage.GROUP_ABSENT);
        String type = record.getType();
        // 查询子级
        List<Long> deleteIdList = Lists.of(id);
        this.flatChildrenId(Lists.singleton(id), deleteIdList);
        // 删除分组
        int effect = dataGroupDAO.deleteBatchIds(deleteIdList);
        // 删除组内数据
        dataGroupRelService.deleteByGroupIdList(type, record.getUserId(), deleteIdList);
        log.info("DataGroupService-deleteDataGroupById id: {}, effect: {}", id, effect);
        // 删除缓存
        this.deleteCache(type, record.getUserId());
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.GROUP_NAME, record.getName());
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteDataGroupByIdList(String type, Long userId, List<Long> idList) {
        log.info("DataGroupService-deleteDataGroupByIdList idList: {}", idList);
        // 检查数据是否存在
        List<DataGroupDO> records = dataGroupDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return 0;
        }
        // 查询子级
        List<Long> deleteIdList = new ArrayList<>(idList);
        this.flatChildrenId(idList, deleteIdList);
        // 删除分组
        int effect = dataGroupDAO.deleteBatchIds(deleteIdList);
        // 删除组内数据
        dataGroupRelService.deleteByGroupIdList(type, userId, deleteIdList);
        log.info("DataGroupService-deleteDataGroupByIdList id: {}, effect: {}", idList, effect);
        // 删除缓存
        this.deleteCache(type, userId);
        return effect;
    }

    @Override
    public Integer deleteDataGroupByUserId(Long userId) {
        if (userId == null) {
            return 0;
        }
        // 删除
        return this.deleteDataGroupByUserIdList(Lists.singleton(userId));
    }

    @Override
    public Integer deleteDataGroupByUserIdList(List<Long> userIdList) {
        if (Lists.isEmpty(userIdList)) {
            return 0;
        }
        // 删除分组
        LambdaQueryWrapper<DataGroupDO> deleteGroup = dataGroupDAO.lambda()
                .in(DataGroupDO::getUserId, userIdList);
        int effect = dataGroupDAO.delete(deleteGroup);
        // 删除分组引用
        LambdaQueryWrapper<DataGroupRelDO> deleteRel = dataGroupRelDAO.lambda()
                .in(DataGroupRelDO::getUserId, userIdList);
        effect += dataGroupRelDAO.delete(deleteRel);
        // 不删除缓存 让其自动过期
        return effect;
    }

    /**
     * 获取所有子节点 id
     *
     * @param parentIdList parentIdList
     * @param result       result
     */
    private void flatChildrenId(List<Long> parentIdList, List<Long> result) {
        // 查询数据
        List<DataGroupDO> rows = dataGroupDAO.selectByParentId(parentIdList);
        if (rows.isEmpty()) {
            return;
        }
        List<Long> idList = rows.stream()
                .map(DataGroupDO::getId)
                .distinct()
                .collect(Collectors.toList());
        result.addAll(idList);
        // 递归
        this.flatChildrenId(idList, result);
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkDataGroupPresent(DataGroupDO domain) {
        // 构造条件
        LambdaQueryWrapper<DataGroupDO> wrapper = dataGroupDAO.wrapper()
                // 更新时忽略当前记录
                .ne(DataGroupDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(DataGroupDO::getParentId, domain.getParentId())
                .eq(DataGroupDO::getType, domain.getType())
                .eq(DataGroupDO::getUserId, domain.getUserId())
                .eq(DataGroupDO::getName, domain.getName());
        // 检查是否存在
        boolean present = dataGroupDAO.of(wrapper).present();
        Assert.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 删除缓存
     *
     * @param type   type
     * @param userId userId
     */
    private void deleteCache(String type, Long userId) {
        RedisStrings.delete(DataGroupCacheKeyDefine.DATA_GROUP_LIST.format(type, userId),
                DataGroupCacheKeyDefine.DATA_GROUP_TREE.format(type, userId));
    }

}
