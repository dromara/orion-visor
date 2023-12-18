// package com.orion.ops.module.infra.api.impl;
//
// import com.alibaba.fastjson.JSON;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.orion.lang.utils.collect.Lists;
// import com.orion.ops.framework.common.constant.ErrorMessage;
// import com.orion.ops.framework.common.utils.Valid;
// import com.orion.ops.module.infra.entity.vo.*;
// import com.orion.ops.module.infra.entity.request.data.*;
// import com.orion.ops.module.infra.convert.*;
// import com.orion.ops.module.infra.entity.dto.*;
// import com.orion.ops.module.infra.define.cache.*;
// import com.orion.ops.module.infra.define.operator.*;
// import com.orion.ops.module.infra.api.*;
// import com.orion.ops.module.infra.api.impl.*;
// import com.orion.ops.module.infra.entity.dto.data.*;
// import com.orion.ops.module.infra.convert.*;
// import com.orion.ops.module.infra.entity.domain.DataAliasDO;
// import com.orion.ops.module.infra.dao.DataAliasDAO;
// import com.orion.ops.module.infra.service.DataAliasService;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Service;
//
// import javax.annotation.Resource;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * 数据别名 对外服务实现类
//  *
//  * @author Jiahang Li
//  * @version 1.0.0
//  * @since 2023-12-18 17:37
//  */
// @Slf4j
// @Service
// public class DataAliasApiImpl implements DataAliasApi {
//
//     @Resource
//     private DataAliasService dataAliasService;
//
//     @Resource
//     private DataAliasDAO dataAliasDAO;
//
//     @Override
//     public Long createDataAlias(DataAliasCreateDTO dto) {
//         log.info("DataAliasApi.createDataAlias dto: {}", JSON.toJSONString(dto));
//         Valid.valid(dto);
//         // 转换
//         DataAliasCreateRequest request = DataAliasProviderConvert.MAPPER.toRequest(dto);
//         // 创建
//         return dataAliasService.createDataAlias(request);
//     }
//
//     @Override
//     public Integer updateDataAliasById(DataAliasUpdateDTO dto) {
//         log.info("DataAliasApi.updateDataAliasById dto: {}", JSON.toJSONString(dto));
//         Valid.valid(dto);
//         // 转换
//         DataAliasUpdateRequest request = DataAliasProviderConvert.MAPPER.toRequest(dto);
//         // 修改
//         return dataAliasService.updateDataAliasById(request);
//     }
//
//
//     @Override
//     public DataAliasDTO getDataAliasById(Long id) {
//         log.info("DataAliasApi.getDataAliasById id: {}", id);
//         Valid.notNull(id, ErrorMessage.ID_MISSING);
//         // 修改
//         DataAliasDO record = dataAliasDAO.selectById(id);
//         if (record == null) {
//             return null;
//         }
//         // 转换
//         return DataAliasProviderConvert.MAPPER.to(record);
//     }
//
//     @Override
//     public List<DataAliasDTO> getDataAliasByIdList(List<Long> idList) {
//         log.info("DataAliasApi.getDataAliasByIdList idList: {}", idList);
//         if (Lists.isEmpty(idList)) {
//             return new ArrayList<>();
//         }
//         // 查询
//         List<DataAliasDO> rows = dataAliasDAO.selectBatchIds(idList);
//         // 转换
//         return DataAliasProviderConvert.MAPPER.toList(rows);
//     }
//
//     @Override
//     public List<DataAliasDTO> getDataAliasList(DataAliasQueryDTO dto) {
//         log.info("DataAliasApi.getDataAliasList dto: {}", JSON.toJSONString(dto));
//         Valid.valid(dto);
//         // 条件
//         LambdaQueryWrapper<DataAliasDO> wrapper = this.buildQueryWrapper(dto);
//         // 查询
//         return dataAliasDAO.of(wrapper).list(DataAliasProviderConvert.MAPPER::to);
//     }
//
//     @Override
//     public List<DataAliasDTO> getDataAliasListByCache() {
//         return dataAliasService.getDataAliasListByCache()
//                 .stream()
//                 .map(DataAliasProviderConvert.MAPPER::to)
//                 .collect(Collectors.toList());
//     }
//
//     @Override
//     public Long getDataAliasCount(DataAliasQueryDTO dto) {
//         log.info("DataAliasApi.getDataAliasCount dto: {}", JSON.toJSONString(dto));
//         Valid.valid(dto);
//         // 条件
//         LambdaQueryWrapper<DataAliasDO> wrapper = this.buildQueryWrapper(dto);
//         // 查询
//         return dataAliasDAO.selectCount(wrapper);
//     }
//
//     @Override
//     public Integer deleteDataAliasById(Long id) {
//         log.info("DataAliasApi.deleteDataAliasById id: {}", id);
//         Valid.notNull(id, ErrorMessage.ID_MISSING);
//         // 删除
//         Integer effect = dataAliasService.deleteDataAliasById(id);
//         log.info("DataAliasApi.deleteDataAliasById id: {}, effect: {}", id, effect);
//         return effect;
//     }
//
//     @Override
//     public Integer deleteDataAliasByIdList(List<Long> idList) {
//         log.info("DataAliasApi.deleteDataAliasByIdList idList: {}", idList);
//         Valid.notEmpty(idList, ErrorMessage.ID_MISSING);
//         // 删除
//         Integer effect = dataAliasService.deleteDataAliasByIdList(idList);
//         log.info("DataAliasApi.deleteDataAliasByIdList effect: {}", effect);
//         return effect;
//     }
//
//     @Override
//     public Integer deleteDataAlias(DataAliasQueryDTO dto) {
//         log.info("DataAliasApi.deleteDataAlias dto: {}", JSON.toJSONString(dto));
//         Valid.valid(dto);
//         // 删除
//         Integer effect = dataAliasService.deleteDataAlias(DataAliasProviderConvert.MAPPER.toRequest(dto));
//         log.info("DataAliasApi.deleteDataAlias effect: {}", effect);
//         return effect;
//     }
//
//     /**
//      * 构建查询 wrapper
//      *
//      * @param dto dto
//      * @return wrapper
//      */
//     private LambdaQueryWrapper<DataAliasDO> buildQueryWrapper(DataAliasQueryDTO dto) {
//         return dataAliasDAO.wrapper()
//                 .eq(DataAliasDO::getId, dto.getId())
//                 .eq(DataAliasDO::getUserId, dto.getUserId())
//                 .eq(DataAliasDO::getRelId, dto.getRelId())
//                 .eq(DataAliasDO::getType, dto.getType())
//                 .eq(DataAliasDO::getAlias, dto.getAlias());
//     }
//
// }
