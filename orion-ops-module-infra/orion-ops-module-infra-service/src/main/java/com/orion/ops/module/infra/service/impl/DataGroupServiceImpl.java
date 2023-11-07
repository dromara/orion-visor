package com.orion.ops.module.infra.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.FileNames;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisMaps;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.request.data.*;
import com.orion.ops.module.infra.convert.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.define.cache.*;
import com.orion.ops.module.infra.define.operator.*;
import com.orion.ops.module.infra.entity.domain.DataGroupDO;
import com.orion.ops.module.infra.dao.DataGroupDAO;
import com.orion.ops.module.infra.service.DataGroupService;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    @Override
    public Long createDataGroup(DataGroupCreateRequest request) {
        log.info("DataGroupService-createDataGroup request: {}", JSON.toJSONString(request));
        // 转换
        DataGroupDO record = DataGroupConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkDataGroupPresent(record);
        // 插入
        int effect = dataGroupDAO.insert(record);
        Long id = record.getId();
        log.info("DataGroupService-createDataGroup id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(DataGroupCacheKeyDefine.DATA_GROUP);
        return id;
    }

    @Override
    public Integer updateDataGroupById(DataGroupUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("DataGroupService-updateDataGroupById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        DataGroupDO record = dataGroupDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        DataGroupDO updateRecord = DataGroupConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkDataGroupPresent(updateRecord);
        // 更新
        int effect = dataGroupDAO.updateById(updateRecord);
        log.info("DataGroupService-updateDataGroupById effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DataGroupCacheKeyDefine.DATA_GROUP);
        return effect;
    }

    @Override
    public Integer updateDataGroup(DataGroupQueryRequest query, DataGroupUpdateRequest update) {
        log.info("DataGroupService.updateDataGroup query: {}, update: {}", JSON.toJSONString(query), JSON.toJSONString(update));
        // 条件
        LambdaQueryWrapper<DataGroupDO> wrapper = this.buildQueryWrapper(query);
        // 转换
        DataGroupDO updateRecord = DataGroupConvert.MAPPER.to(update);
        // 更新
        int effect = dataGroupDAO.update(updateRecord, wrapper);
        log.info("DataGroupService.updateDataGroup effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DataGroupCacheKeyDefine.DATA_GROUP);
        return effect;
    }

    @Override
    public DataGroupVO getDataGroupById(Long id) {
        // 查询
        DataGroupDO record = dataGroupDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return DataGroupConvert.MAPPER.to(record);
    }

    @Override
    public List<DataGroupVO> getDataGroupByIdList(List<Long> idList) {
        // 查询
        List<DataGroupDO> records = dataGroupDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return DataGroupConvert.MAPPER.to(records);
    }

    @Override
    public List<DataGroupVO> getDataGroupList(DataGroupQueryRequest request) {
        // 条件
        LambdaQueryWrapper<DataGroupDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return dataGroupDAO.of(wrapper).list(DataGroupConvert.MAPPER::to);
    }

    @Override
    public List<DataGroupVO> getDataGroupListByCache() {
        // 查询缓存
        List<DataGroupCacheDTO> list = RedisMaps.valuesJson(DataGroupCacheKeyDefine.DATA_GROUP);
        if (list.isEmpty()) {
            // 查询数据库
            list = dataGroupDAO.of().list(DataGroupConvert.MAPPER::toCache);
            // 添加默认值 防止穿透
            if (list.isEmpty()) {
                list.add(DataGroupCacheDTO.builder()
                        .id(Const.NONE_ID)
                        .build());
            }
            // 设置缓存
            RedisMaps.putAllJson(DataGroupCacheKeyDefine.DATA_GROUP.getKey(), s -> s.getId().toString(), list);
            RedisMaps.setExpire(DataGroupCacheKeyDefine.DATA_GROUP);
        }
        // 删除默认值
        return list.stream()
                .filter(s -> !s.getId().equals(Const.NONE_ID))
                .map(DataGroupConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public Long getDataGroupCount(DataGroupQueryRequest request) {
        // 条件
        LambdaQueryWrapper<DataGroupDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return dataGroupDAO.selectCount(wrapper);
    }

    @Override
    public DataGrid<DataGroupVO> getDataGroupPage(DataGroupQueryRequest request) {
        // 条件
        LambdaQueryWrapper<DataGroupDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return dataGroupDAO.of(wrapper)
                .page(request)
                .dataGrid(DataGroupConvert.MAPPER::to);
    }

    @Override
    public Integer deleteDataGroupById(Long id) {
        log.info("DataGroupService-deleteDataGroupById id: {}", id);
        // 检查数据是否存在
        DataGroupDO record = dataGroupDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = dataGroupDAO.deleteById(id);
        log.info("DataGroupService-deleteDataGroupById id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(DataGroupCacheKeyDefine.DATA_GROUP, id);
        return effect;
    }

    @Override
    public Integer deleteDataGroupByIdList(List<Long> idList) {
        log.info("DataGroupService-deleteDataGroupByIdList idList: {}", idList);
        int effect = dataGroupDAO.deleteBatchIds(idList);
        log.info("DataGroupService-deleteDataGroupByIdList effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DataGroupCacheKeyDefine.DATA_GROUP, idList);
        return effect;
    }

    @Override
    public Integer deleteDataGroup(DataGroupQueryRequest request) {
        log.info("DataGroupService.deleteDataGroup request: {}", JSON.toJSONString(request));
        // 条件
        LambdaQueryWrapper<DataGroupDO> wrapper = this.buildQueryWrapper(request);
        // 删除
        int effect = dataGroupDAO.delete(wrapper);
        log.info("DataGroupService.deleteDataGroup effect: {}", effect);
        // 删除缓存
        RedisStrings.delete(DataGroupCacheKeyDefine.DATA_GROUP);
        return effect;
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
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<DataGroupDO> buildQueryWrapper(DataGroupQueryRequest request) {
        String searchValue = request.getSearchValue();
        return dataGroupDAO.wrapper()
                .eq(DataGroupDO::getId, request.getId())
                .eq(DataGroupDO::getParentId, request.getParentId())
                .eq(DataGroupDO::getName, request.getName())
                .eq(DataGroupDO::getType, request.getType())
                .eq(DataGroupDO::getSort, request.getSort())
                .and(Strings.isNotEmpty(searchValue), c -> c
                        .eq(DataGroupDO::getId, searchValue).or()
                        .eq(DataGroupDO::getParentId, searchValue).or()
                        .eq(DataGroupDO::getName, searchValue).or()
                        .eq(DataGroupDO::getType, searchValue).or()
                        .eq(DataGroupDO::getSort, searchValue)
                );
    }

}
