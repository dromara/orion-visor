package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.enums.MovePosition;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.framework.redis.core.utils.barrier.CacheBarriers;
import com.orion.ops.module.infra.convert.DataGroupConvert;
import com.orion.ops.module.infra.dao.DataGroupDAO;
import com.orion.ops.module.infra.define.cache.DataGroupCacheKeyDefine;
import com.orion.ops.module.infra.entity.domain.DataGroupDO;
import com.orion.ops.module.infra.entity.dto.DataGroupCacheDTO;
import com.orion.ops.module.infra.entity.request.data.DataGroupCreateRequest;
import com.orion.ops.module.infra.entity.request.data.DataGroupMoveRequest;
import com.orion.ops.module.infra.entity.request.data.DataGroupRenameRequest;
import com.orion.ops.module.infra.service.DataGroupRelService;
import com.orion.ops.module.infra.service.DataGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
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
    private DataGroupRelService dataGroupRelService;

    @Override
    public Long createDataGroup(DataGroupCreateRequest request) {
        log.info("DataGroupService-createDataGroup request: {}", JSON.toJSONString(request));
        // 转换
        DataGroupDO record = DataGroupConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkDataGroupPresent(record);
        // 查询最大排序
        Integer sort = dataGroupDAO.selectMaxSort(request.getParentId(), request.getType());
        record.setSort(sort + Const.DEFAULT_SORT);
        // 插入
        int effect = dataGroupDAO.insert(record);
        Long id = record.getId();
        log.info("DataGroupService-createDataGroup id: {}, effect: {}", id, effect);
        // 删除缓存
        this.deleteCache(request.getType());
        return id;
    }

    @Override
    public Integer renameDataGroup(DataGroupRenameRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        String name = Valid.notBlank(request.getName());
        // 查询
        DataGroupDO record = dataGroupDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.GROUP_ABSENT);
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
        this.deleteCache(record.getType());
        return effect;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer moveDataGroup(DataGroupMoveRequest request) {
        Long id = request.getId();
        Long targetId = request.getTargetId();
        MovePosition position = Valid.valid(MovePosition::of, request.getPosition());
        // 查询节点是否存在
        DataGroupDO moveRecord = dataGroupDAO.selectById(id);
        DataGroupDO targetRecord = dataGroupDAO.selectById(targetId);
        Valid.notNull(moveRecord, ErrorMessage.GROUP_ABSENT);
        Valid.notNull(targetRecord, ErrorMessage.GROUP_ABSENT);
        // 更新
        String type = moveRecord.getType();
        Long targetParentId = targetRecord.getParentId();
        int effect = 0;
        // 修改排序
        if (MovePosition.TOP.equals(position)) {
            // 移动到元素上 将大于等于 targetRecord 的排序都加 10
            dataGroupDAO.updateSort(targetParentId, ">=",
                    targetRecord.getSort(), Const.DEFAULT_SORT);
            // 修改 parentId sort
            DataGroupDO update = DataGroupDO.builder()
                    .id(id)
                    .parentId(targetParentId)
                    .sort(targetRecord.getSort())
                    .build();
            effect = dataGroupDAO.updateById(update);
        } else if (MovePosition.IN.equals(position)) {
            // 移动到元素中 获取最大排序
            Integer newSort = dataGroupDAO.selectMaxSort(targetId, type) + Const.DEFAULT_SORT;
            // 修改 parentId sort
            DataGroupDO update = DataGroupDO.builder()
                    .id(id)
                    .parentId(targetId)
                    .sort(newSort)
                    .build();
            effect = dataGroupDAO.updateById(update);
        } else if (MovePosition.BOTTOM.equals(position)) {
            // 移动到元素下 将大于 targetRecord 的排序都加 10
            dataGroupDAO.updateSort(targetParentId, ">",
                    targetRecord.getSort(), Const.DEFAULT_SORT);
            // 修改 parentId sort
            DataGroupDO update = DataGroupDO.builder()
                    .id(id)
                    .parentId(targetParentId)
                    .sort(targetRecord.getSort() + 1)
                    .build();
            effect = dataGroupDAO.updateById(update);
        }
        // 删除缓存
        this.deleteCache(type);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.SOURCE, moveRecord.getName());
        OperatorLogs.add(OperatorLogs.TARGET, targetRecord.getName());
        OperatorLogs.add(OperatorLogs.POSITION_NAME, position.name());
        return effect;
    }

    @Override
    public List<DataGroupCacheDTO> getDataGroupListByCache(String type) {
        // 查询缓存
        String key = DataGroupCacheKeyDefine.DATA_GROUP_LIST.format(type);
        List<DataGroupCacheDTO> list = RedisStrings.getJsonArray(key, DataGroupCacheKeyDefine.DATA_GROUP_LIST);
        if (Lists.isEmpty(list)) {
            // 查询数据库
            list = dataGroupDAO.of()
                    .createWrapper()
                    .eq(DataGroupDO::getType, type)
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
    public List<DataGroupCacheDTO> getDataGroupTreeByCache(String type) {
        // 查询缓存
        String key = DataGroupCacheKeyDefine.DATA_GROUP_TREE.format(type);
        List<DataGroupCacheDTO> treeData = RedisStrings.getJsonArray(key, DataGroupCacheKeyDefine.DATA_GROUP_TREE);
        if (Lists.isEmpty(treeData)) {
            // 查询列表缓存
            List<DataGroupCacheDTO> rows = this.getDataGroupListByCache(type);
            // 设置屏障 防止穿透
            CacheBarriers.checkBarrier(rows, DataGroupCacheDTO::new);
            if (!Lists.isEmpty(rows)) {
                // 构建树
                DataGroupCacheDTO rootNode = DataGroupCacheDTO.builder()
                        .id(Const.ROOT_PARENT_ID)
                        .sort(Const.DEFAULT_SORT)
                        .build();
                this.buildGroupTree(rootNode, rows);
                treeData = rootNode.getChildren();
            }
            // 设置缓存
            RedisStrings.setJson(key, DataGroupCacheKeyDefine.DATA_GROUP_LIST, treeData);
        }
        // 删除屏障
        CacheBarriers.removeBarrier(treeData);
        return treeData;
    }

    /**
     * 构建树
     *
     * @param parentNode parentNode
     * @param nodes      nodes
     */
    private void buildGroupTree(DataGroupCacheDTO parentNode,
                                List<DataGroupCacheDTO> nodes) {
        // 获取子节点
        List<DataGroupCacheDTO> childrenNodes = nodes.stream()
                .filter(s -> parentNode.getId().equals(s.getParentId()))
                .sorted(Comparator.comparing(DataGroupCacheDTO::getSort))
                .collect(Collectors.toList());
        if (childrenNodes.isEmpty()) {
            return;
        }
        parentNode.setChildren(childrenNodes);
        // 遍历子节点
        for (DataGroupCacheDTO childrenNode : childrenNodes) {
            this.buildGroupTree(childrenNode, nodes);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteDataGroupById(Long id) {
        log.info("DataGroupService-deleteDataGroupById id: {}", id);
        // 检查数据是否存在
        DataGroupDO record = dataGroupDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.GROUP_ABSENT);
        String type = record.getType();
        // 查询子级
        List<Long> deleteIdList = Lists.of(id);
        this.flatChildrenId(Lists.singleton(id), deleteIdList);
        // 删除分组
        int effect = dataGroupDAO.deleteBatchIds(deleteIdList);
        // 删除组内数据
        dataGroupRelService.deleteByGroupIdList(type, deleteIdList);
        log.info("DataGroupService-deleteDataGroupById id: {}, effect: {}", id, effect);
        // 删除缓存
        this.deleteCache(type);
        // 添加日志参数
        OperatorLogs.add(OperatorLogs.GROUP_NAME, record.getName());
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
                .eq(DataGroupDO::getName, domain.getName())
                .eq(DataGroupDO::getType, domain.getType());
        // 检查是否存在
        boolean present = dataGroupDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 删除缓存
     *
     * @param type type
     */
    private void deleteCache(String type) {
        RedisStrings.delete(DataGroupCacheKeyDefine.DATA_GROUP_LIST.format(type),
                DataGroupCacheKeyDefine.DATA_GROUP_TREE.format(type));
    }

}
