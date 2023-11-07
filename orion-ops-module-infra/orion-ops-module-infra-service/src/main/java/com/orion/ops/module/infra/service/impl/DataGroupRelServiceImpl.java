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
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.request.data.*;
import com.orion.ops.module.infra.convert.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.define.cache.*;
import com.orion.ops.module.infra.define.operator.*;
import com.orion.ops.module.infra.entity.domain.DataGroupRelDO;
import com.orion.ops.module.infra.dao.DataGroupRelDAO;
import com.orion.ops.module.infra.service.DataGroupRelService;
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
 * 数据分组关联 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Slf4j
@Service
public class DataGroupRelServiceImpl implements DataGroupRelService {

    @Resource
    private DataGroupRelDAO dataGroupRelDAO;

    @Override
    public Long createDataGroupRel(DataGroupRelCreateRequest request) {
        log.info("DataGroupRelService-createDataGroupRel request: {}", JSON.toJSONString(request));
        // 转换
        DataGroupRelDO record = DataGroupRelConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkDataGroupRelPresent(record);
        // 插入
        int effect = dataGroupRelDAO.insert(record);
        Long id = record.getId();
        log.info("DataGroupRelService-createDataGroupRel id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(DataGroupRelCacheKeyDefine.DATA_GROUP_REL);
        return id;
    }

    @Override
    public Integer updateDataGroupRelById(DataGroupRelUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("DataGroupRelService-updateDataGroupRelById id: {}, request: {}", id, JSON.toJSONString(request));
        // 查询
        DataGroupRelDO record = dataGroupRelDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        DataGroupRelDO updateRecord = DataGroupRelConvert.MAPPER.to(request);
        // 查询数据是否冲突
        this.checkDataGroupRelPresent(updateRecord);
        // 更新
        int effect = dataGroupRelDAO.updateById(updateRecord);
        log.info("DataGroupRelService-updateDataGroupRelById effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DataGroupRelCacheKeyDefine.DATA_GROUP_REL);
        return effect;
    }

    @Override
    public Integer updateDataGroupRel(DataGroupRelQueryRequest query, DataGroupRelUpdateRequest update) {
        log.info("DataGroupRelService.updateDataGroupRel query: {}, update: {}", JSON.toJSONString(query), JSON.toJSONString(update));
        // 条件
        LambdaQueryWrapper<DataGroupRelDO> wrapper = this.buildQueryWrapper(query);
        // 转换
        DataGroupRelDO updateRecord = DataGroupRelConvert.MAPPER.to(update);
        // 更新
        int effect = dataGroupRelDAO.update(updateRecord, wrapper);
        log.info("DataGroupRelService.updateDataGroupRel effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DataGroupRelCacheKeyDefine.DATA_GROUP_REL);
        return effect;
    }

    @Override
    public DataGroupRelVO getDataGroupRelById(Long id) {
        // 查询
        DataGroupRelDO record = dataGroupRelDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 转换
        return DataGroupRelConvert.MAPPER.to(record);
    }

    @Override
    public List<DataGroupRelVO> getDataGroupRelByIdList(List<Long> idList) {
        // 查询
        List<DataGroupRelDO> records = dataGroupRelDAO.selectBatchIds(idList);
        if (records.isEmpty()) {
            return Lists.empty();
        }
        // 转换
        return DataGroupRelConvert.MAPPER.to(records);
    }

    @Override
    public List<DataGroupRelVO> getDataGroupRelList(DataGroupRelQueryRequest request) {
        // 条件
        LambdaQueryWrapper<DataGroupRelDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return dataGroupRelDAO.of(wrapper).list(DataGroupRelConvert.MAPPER::to);
    }

    @Override
    public List<DataGroupRelVO> getDataGroupRelListByCache() {
        // 查询缓存
        List<DataGroupRelCacheDTO> list = RedisMaps.valuesJson(DataGroupRelCacheKeyDefine.DATA_GROUP_REL);
        if (list.isEmpty()) {
            // 查询数据库
            list = dataGroupRelDAO.of().list(DataGroupRelConvert.MAPPER::toCache);
            // 添加默认值 防止穿透
            if (list.isEmpty()) {
                list.add(DataGroupRelCacheDTO.builder()
                        .id(Const.NONE_ID)
                        .build());
            }
            // 设置缓存
            RedisMaps.putAllJson(DataGroupRelCacheKeyDefine.DATA_GROUP_REL.getKey(), s -> s.getId().toString(), list);
            RedisMaps.setExpire(DataGroupRelCacheKeyDefine.DATA_GROUP_REL);
        }
        // 删除默认值
        return list.stream()
                .filter(s -> !s.getId().equals(Const.NONE_ID))
                .map(DataGroupRelConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public Long getDataGroupRelCount(DataGroupRelQueryRequest request) {
        // 条件
        LambdaQueryWrapper<DataGroupRelDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return dataGroupRelDAO.selectCount(wrapper);
    }

    @Override
    public DataGrid<DataGroupRelVO> getDataGroupRelPage(DataGroupRelQueryRequest request) {
        // 条件
        LambdaQueryWrapper<DataGroupRelDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return dataGroupRelDAO.of(wrapper)
                .page(request)
                .dataGrid(DataGroupRelConvert.MAPPER::to);
    }

    @Override
    public Integer deleteDataGroupRelById(Long id) {
        log.info("DataGroupRelService-deleteDataGroupRelById id: {}", id);
        // 检查数据是否存在
        DataGroupRelDO record = dataGroupRelDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.DATA_ABSENT);
        // 删除
        int effect = dataGroupRelDAO.deleteById(id);
        log.info("DataGroupRelService-deleteDataGroupRelById id: {}, effect: {}", id, effect);
        // 删除缓存
        RedisMaps.delete(DataGroupRelCacheKeyDefine.DATA_GROUP_REL, id);
        return effect;
    }

    @Override
    public Integer deleteDataGroupRelByIdList(List<Long> idList) {
        log.info("DataGroupRelService-deleteDataGroupRelByIdList idList: {}", idList);
        int effect = dataGroupRelDAO.deleteBatchIds(idList);
        log.info("DataGroupRelService-deleteDataGroupRelByIdList effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DataGroupRelCacheKeyDefine.DATA_GROUP_REL, idList);
        return effect;
    }

    @Override
    public Integer deleteDataGroupRel(DataGroupRelQueryRequest request) {
        log.info("DataGroupRelService.deleteDataGroupRel request: {}", JSON.toJSONString(request));
        // 条件
        LambdaQueryWrapper<DataGroupRelDO> wrapper = this.buildQueryWrapper(request);
        // 删除
        int effect = dataGroupRelDAO.delete(wrapper);
        log.info("DataGroupRelService.deleteDataGroupRel effect: {}", effect);
        // 删除缓存
        RedisMaps.delete(DataGroupRelCacheKeyDefine.DATA_GROUP_REL);
        return effect;
    }

    /**
     * 检查对象是否存在
     *
     * @param domain domain
     */
    private void checkDataGroupRelPresent(DataGroupRelDO domain) {
        // 构造条件
        LambdaQueryWrapper<DataGroupRelDO> wrapper = dataGroupRelDAO.wrapper()
                // 更新时忽略当前记录
                .ne(DataGroupRelDO::getId, domain.getId())
                // 用其他字段做重复校验
                .eq(DataGroupRelDO::getGroupId, domain.getGroupId())
                .eq(DataGroupRelDO::getRelId, domain.getRelId())
                .eq(DataGroupRelDO::getType, domain.getType())
                .eq(DataGroupRelDO::getSort, domain.getSort());
        // 检查是否存在
        boolean present = dataGroupRelDAO.of(wrapper).present();
        Valid.isFalse(present, ErrorMessage.DATA_PRESENT);
    }

    /**
     * 构建查询 wrapper
     *
     * @param request request
     * @return wrapper
     */
    private LambdaQueryWrapper<DataGroupRelDO> buildQueryWrapper(DataGroupRelQueryRequest request) {
        String searchValue = request.getSearchValue();
        return dataGroupRelDAO.wrapper()
                .eq(DataGroupRelDO::getId, request.getId())
                .eq(DataGroupRelDO::getGroupId, request.getGroupId())
                .eq(DataGroupRelDO::getRelId, request.getRelId())
                .eq(DataGroupRelDO::getType, request.getType())
                .eq(DataGroupRelDO::getSort, request.getSort())
                .and(Strings.isNotEmpty(searchValue), c -> c
                        .eq(DataGroupRelDO::getId, searchValue).or()
                        .eq(DataGroupRelDO::getGroupId, searchValue).or()
                        .eq(DataGroupRelDO::getRelId, searchValue).or()
                        .eq(DataGroupRelDO::getType, searchValue).or()
                        .eq(DataGroupRelDO::getSort, searchValue)
                );
    }

}
