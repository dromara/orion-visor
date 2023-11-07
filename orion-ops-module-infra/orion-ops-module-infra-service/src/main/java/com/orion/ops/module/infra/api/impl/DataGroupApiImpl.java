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
import com.orion.ops.module.infra.entity.domain.DataGroupDO;
import com.orion.ops.module.infra.dao.DataGroupDAO;
import com.orion.ops.module.infra.service.DataGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据分组 对外服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
@Slf4j
@Service
public class DataGroupApiImpl implements DataGroupApi {

    @Resource
    private DataGroupService dataGroupService;

    @Resource
    private DataGroupDAO dataGroupDAO;

    @Override
    public Long createDataGroup(DataGroupCreateDTO dto) {
        log.info("DataGroupApi.createDataGroup dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 转换
        DataGroupCreateRequest request = DataGroupProviderConvert.MAPPER.toRequest(dto);
        // 创建
        return dataGroupService.createDataGroup(request);
    }

    @Override
    public Integer updateDataGroupById(DataGroupUpdateDTO dto) {
        log.info("DataGroupApi.updateDataGroupById dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 转换
        DataGroupUpdateRequest request = DataGroupProviderConvert.MAPPER.toRequest(dto);
        // 修改
        return dataGroupService.updateDataGroupById(request);
    }

    @Override
    public Integer updateDataGroup(DataGroupQueryDTO query, DataGroupUpdateDTO update) {
        log.info("DataGroupApi.updateDataGroup query: {}, update: {}", JSON.toJSONString(query), JSON.toJSONString(update));
        Valid.valid(query);
        Valid.valid(update);
        // 更新
        int effect = dataGroupService.updateDataGroup(DataGroupProviderConvert.MAPPER.toRequest(query),
                DataGroupProviderConvert.MAPPER.toRequest(update));
        log.info("DataGroupApi.updateDataGroup effect: {}", effect);
        return effect;
    }

    @Override
    public DataGroupDTO getDataGroupById(Long id) {
        log.info("DataGroupApi.getDataGroupById id: {}", id);
        Valid.notNull(id, ErrorMessage.ID_MISSING);
        // 修改
        DataGroupDO record = dataGroupDAO.selectById(id);
        if (record == null) {
            return null;
        }
        // 转换
        return DataGroupProviderConvert.MAPPER.to(record);
    }

    @Override
    public List<DataGroupDTO> getDataGroupByIdList(List<Long> idList) {
        log.info("DataGroupApi.getDataGroupByIdList idList: {}", idList);
        if (Lists.isEmpty(idList)) {
            return new ArrayList<>();
        }
        // 查询
        List<DataGroupDO> rows = dataGroupDAO.selectBatchIds(idList);
        // 转换
        return DataGroupProviderConvert.MAPPER.toList(rows);
    }

    @Override
    public List<DataGroupDTO> getDataGroupList(DataGroupQueryDTO dto) {
        log.info("DataGroupApi.getDataGroupList dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 条件
        LambdaQueryWrapper<DataGroupDO> wrapper = this.buildQueryWrapper(dto);
        // 查询
        return dataGroupDAO.of(wrapper).list(DataGroupProviderConvert.MAPPER::to);
    }

    @Override
    public List<DataGroupDTO> getDataGroupListByCache() {
        return dataGroupService.getDataGroupListByCache()
                .stream()
                .map(DataGroupProviderConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    public Long getDataGroupCount(DataGroupQueryDTO dto) {
        log.info("DataGroupApi.getDataGroupCount dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 条件
        LambdaQueryWrapper<DataGroupDO> wrapper = this.buildQueryWrapper(dto);
        // 查询
        return dataGroupDAO.selectCount(wrapper);
    }

    @Override
    public Integer deleteDataGroupById(Long id) {
        log.info("DataGroupApi.deleteDataGroupById id: {}", id);
        Valid.notNull(id, ErrorMessage.ID_MISSING);
        // 删除
        Integer effect = dataGroupService.deleteDataGroupById(id);
        log.info("DataGroupApi.deleteDataGroupById id: {}, effect: {}", id, effect);
        return effect;
    }

    @Override
    public Integer deleteDataGroupByIdList(List<Long> idList) {
        log.info("DataGroupApi.deleteDataGroupByIdList idList: {}", idList);
        Valid.notEmpty(idList, ErrorMessage.ID_MISSING);
        // 删除
        Integer effect = dataGroupService.deleteDataGroupByIdList(idList);
        log.info("DataGroupApi.deleteDataGroupByIdList effect: {}", effect);
        return effect;
    }

    @Override
    public Integer deleteDataGroup(DataGroupQueryDTO dto) {
        log.info("DataGroupApi.deleteDataGroup dto: {}", JSON.toJSONString(dto));
        Valid.valid(dto);
        // 删除
        Integer effect = dataGroupService.deleteDataGroup(DataGroupProviderConvert.MAPPER.toRequest(dto));
        log.info("DataGroupApi.deleteDataGroup effect: {}", effect);
        return effect;
    }

    /**
     * 构建查询 wrapper
     *
     * @param dto dto
     * @return wrapper
     */
    private LambdaQueryWrapper<DataGroupDO> buildQueryWrapper(DataGroupQueryDTO dto) {
        return dataGroupDAO.wrapper()
                .eq(DataGroupDO::getId, dto.getId())
                .eq(DataGroupDO::getParentId, dto.getParentId())
                .eq(DataGroupDO::getName, dto.getName())
                .eq(DataGroupDO::getType, dto.getType())
                .eq(DataGroupDO::getSort, dto.getSort());
    }

}
