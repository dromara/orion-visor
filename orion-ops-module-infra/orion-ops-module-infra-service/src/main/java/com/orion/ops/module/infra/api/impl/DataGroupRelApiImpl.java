package com.orion.ops.module.infra.api.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.orion.lang.utils.collect.Lists;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.module.infra.entity.vo.*;
import com.orion.ops.module.infra.entity.request.data.*;
import com.orion.ops.module.infra.convert.*;
import com.orion.ops.module.infra.entity.dto.*;
import com.orion.ops.module.infra.define.cache.*;
import com.orion.ops.module.infra.define.operator.*;
import com.orion.ops.module.infra.api.*;
import com.orion.ops.module.infra.api.impl.*;
import com.orion.ops.module.infra.entity.dto.data.*;
import com.orion.ops.module.infra.convert.*;
import com.orion.ops.module.infra.entity.domain.DataGroupRelDO;
import com.orion.ops.module.infra.dao.DataGroupRelDAO;
import com.orion.ops.module.infra.service.DataGroupRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据分组关联 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Slf4j
@Service
public class DataGroupRelApiImpl implements DataGroupRelApi {

    @Resource
    private DataGroupRelService dataGroupRelService;

    @Resource
    private DataGroupRelDAO dataGroupRelDAO;

    @Override
    public Long createDataGroupRel(DataGroupRelCreateDTO dto) {
        log.info("DataGroupRelApi.createDataGroupRel dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 转换
        DataGroupRelCreateRequest request = DataGroupRelProviderConvert.MAPPER.toRequest(dto);
        // 创建
        return dataGroupRelService.createDataGroupRel(request);
    }

    @Override
    public Integer updateDataGroupRelById(DataGroupRelUpdateDTO dto) {
        log.info("DataGroupRelApi.updateDataGroupRelById dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 转换
        DataGroupRelUpdateRequest request = DataGroupRelProviderConvert.MAPPER.toRequest(dto);
        // 修改
        return dataGroupRelService.updateDataGroupRelById(request);
    }

    @Override
    public Integer updateDataGroupRel(DataGroupRelQueryDTO query, DataGroupRelUpdateDTO update) {
        log.info("DataGroupRelApi.updateDataGroupRel query: {}, update: {}", JSON.toJSONString(query), JSON.toJSONString(update));
        Valid.valid(query);
        Valid.valid(update);
        // 更新
        int effect = dataGroupRelService.updateDataGroupRel(DataGroupRelProviderConvert.MAPPER.toRequest(query),
                DataGroupRelProviderConvert.MAPPER.toRequest(update));
        log.info("DataGroupRelApi.updateDataGroupRel effect: {}", effect);
        return effect;
    }

    @Override
    public DataGroupRelDTO getDataGroupRelById(Long id) {
        log.info("DataGroupRelApi.getDataGroupRelById id: {}", id);
        Valid.notNull(id, ErrorMessage.ID_MISSING);
        // 修改
        DataGroupRelDO record = dataGroupRelDAO.selectById(id);
        if (record == null) {
            return null;
        }
        // 转换
        return DataGroupRelProviderConvert.MAPPER.to(record);
    }

    @Override
    public List<DataGroupRelDTO> getDataGroupRelByIdList(List<Long> idList) {
        log.info("DataGroupRelApi.getDataGroupRelByIdList idList: {}", idList);
        if (Lists.isEmpty(idList)) {
            return new ArrayList<>();
        }
        // 查询
        List<DataGroupRelDO> rows = dataGroupRelDAO.selectBatchIds(idList);
        // 转换
        return DataGroupRelProviderConvert.MAPPER.toList(rows);
    }

    @Override
    public List<DataGroupRelDTO> getDataGroupRelList(DataGroupRelQueryDTO dto) {
        log.info("DataGroupRelApi.getDataGroupRelList dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 条件
        LambdaQueryWrapper<DataGroupRelDO> wrapper = this.buildQueryWrapper(dto);
        // 查询
        return dataGroupRelDAO.of(wrapper).list(DataGroupRelProviderConvert.MAPPER::to);
    }

    @Override
    public List<DataGroupRelDTO> getDataGroupRelListByCache() {
        return dataGroupRelService.getDataGroupRelListByCache()
                .stream()
                .map(DataGroupRelProviderConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public Long getDataGroupRelCount(DataGroupRelQueryDTO dto) {
        log.info("DataGroupRelApi.getDataGroupRelCount dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 条件
        LambdaQueryWrapper<DataGroupRelDO> wrapper = this.buildQueryWrapper(dto);
        // 查询
        return dataGroupRelDAO.selectCount(wrapper);
    }

    @Override
    public Integer deleteDataGroupRelById(Long id) {
        log.info("DataGroupRelApi.deleteDataGroupRelById id: {}", id);
        Valid.notNull(id, ErrorMessage.ID_MISSING);
        // 删除
        Integer effect = dataGroupRelService.deleteDataGroupRelById(id);
        log.info("DataGroupRelApi.deleteDataGroupRelById id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer deleteDataGroupRelByIdList(List<Long> idList) {
        log.info("DataGroupRelApi.deleteDataGroupRelByIdList idList: {}", idList);
        Valid.notEmpty(idList, ErrorMessage.ID_MISSING);
        // 删除
        Integer effect = dataGroupRelService.deleteDataGroupRelByIdList(idList);
        log.info("DataGroupRelApi.deleteDataGroupRelByIdList effect: {}", effect);
        return effect;
    }

    @Override
    public Integer deleteDataGroupRel(DataGroupRelQueryDTO dto) {
        log.info("DataGroupRelApi.deleteDataGroupRel dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 删除
        Integer effect = dataGroupRelService.deleteDataGroupRel(DataGroupRelProviderConvert.MAPPER.toRequest(dto));
        log.info("DataGroupRelApi.deleteDataGroupRel effect: {}", effect);
        return effect;
    }

    /**
     * 构建查询 wrapper
     *
     * @param dto dto
     * @return wrapper
     */
    private LambdaQueryWrapper<DataGroupRelDO> buildQueryWrapper(DataGroupRelQueryDTO dto) {
        return dataGroupRelDAO.wrapper()
                .eq(DataGroupRelDO::getId, dto.getId())
                .eq(DataGroupRelDO::getGroupId, dto.getGroupId())
                .eq(DataGroupRelDO::getRelId, dto.getRelId())
                .eq(DataGroupRelDO::getType, dto.getType())
                .eq(DataGroupRelDO::getSort, dto.getSort());
    }

}
