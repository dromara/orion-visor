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
// import com.orion.ops.module.infra.define.operator.*;
// import com.orion.ops.module.infra.api.*;
// import com.orion.ops.module.infra.api.impl.*;
// import com.orion.ops.module.infra.entity.dto.data.*;
// import com.orion.ops.module.infra.convert.*;
// import com.orion.ops.module.infra.entity.domain.DataPermissionDO;
// import com.orion.ops.module.infra.dao.DataPermissionDAO;
// import com.orion.ops.module.infra.service.DataPermissionService;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Service;
//
// import javax.annotation.Resource;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * 数据权限 对外服务实现类
//  *
//  * @author Jiahang Li
//  * @version 1.0.0
//  * @since 2023-11-21 10:32
//  */
// @Slf4j
// @Service
// public class DataPermissionApiImpl implements DataPermissionApi {
//
//     @Resource
//     private DataPermissionService dataPermissionService;
//
//     @Resource
//     private DataPermissionDAO dataPermissionDAO;
//
//     @Override
//     public Long createDataPermission(DataPermissionCreateDTO dto) {
//         log.info("DataPermissionApi.createDataPermission dto: {}", JSON.toJSONString(dto));
//         Valid.valid(dto);
//         // 转换
//         DataPermissionCreateRequest request = DataPermissionProviderConvert.MAPPER.toRequest(dto);
//         // 创建
//         return dataPermissionService.createDataPermission(request);
//     }
//
//     @Override
//     public Integer updateDataPermissionById(DataPermissionUpdateDTO dto) {
//         log.info("DataPermissionApi.updateDataPermissionById dto: {}", JSON.toJSONString(dto));
//         Valid.valid(dto);
//         // 转换
//         DataPermissionUpdateRequest request = DataPermissionProviderConvert.MAPPER.toRequest(dto);
//         // 修改
//         return dataPermissionService.updateDataPermissionById(request);
//     }
//
//     @Override
//     public Integer updateDataPermission(DataPermissionQueryDTO query, DataPermissionUpdateDTO update) {
//         log.info("DataPermissionApi.updateDataPermission query: {}, update: {}", JSON.toJSONString(query), JSON.toJSONString(update));
//         Valid.valid(query);
//         Valid.valid(update);
//         // 更新
//         int effect = dataPermissionService.updateDataPermission(DataPermissionProviderConvert.MAPPER.toRequest(query),
//                 DataPermissionProviderConvert.MAPPER.toRequest(update));
//         log.info("DataPermissionApi.updateDataPermission effect: {}", effect);
//         return effect;
//     }
//
//     @Override
//     public DataPermissionDTO getDataPermissionById(Long id) {
//         log.info("DataPermissionApi.getDataPermissionById id: {}", id);
//         Valid.notNull(id, ErrorMessage.ID_MISSING);
//         // 修改
//         DataPermissionDO record = dataPermissionDAO.selectById(id);
//         if (record == null) {
//             return null;
//         }
//         // 转换
//         return DataPermissionProviderConvert.MAPPER.to(record);
//     }
//
//     @Override
//     public List<DataPermissionDTO> getDataPermissionByIdList(List<Long> idList) {
//         log.info("DataPermissionApi.getDataPermissionByIdList idList: {}", idList);
//         if (Lists.isEmpty(idList)) {
//             return new ArrayList<>();
//         }
//         // 查询
//         List<DataPermissionDO> rows = dataPermissionDAO.selectBatchIds(idList);
//         // 转换
//         return DataPermissionProviderConvert.MAPPER.toList(rows);
//     }
//
//     @Override
//     public List<DataPermissionDTO> getDataPermissionList(DataPermissionQueryDTO dto) {
//         log.info("DataPermissionApi.getDataPermissionList dto: {}", JSON.toJSONString(dto));
//         Valid.valid(dto);
//         // 条件
//         LambdaQueryWrapper<DataPermissionDO> wrapper = this.buildQueryWrapper(dto);
//         // 查询
//         return dataPermissionDAO.of(wrapper).list(DataPermissionProviderConvert.MAPPER::to);
//     }
//
//     @Override
//     public Long getDataPermissionCount(DataPermissionQueryDTO dto) {
//         log.info("DataPermissionApi.getDataPermissionCount dto: {}", JSON.toJSONString(dto));
//         Valid.valid(dto);
//         // 条件
//         LambdaQueryWrapper<DataPermissionDO> wrapper = this.buildQueryWrapper(dto);
//         // 查询
//         return dataPermissionDAO.selectCount(wrapper);
//     }
//
//     @Override
//     public Integer deleteDataPermissionById(Long id) {
//         log.info("DataPermissionApi.deleteDataPermissionById id: {}", id);
//         Valid.notNull(id, ErrorMessage.ID_MISSING);
//         // 删除
//         Integer effect = dataPermissionService.deleteDataPermissionById(id);
//         log.info("DataPermissionApi.deleteDataPermissionById id: {}, effect: {}", id, effect);
//         return effect;
//     }
//
//     @Override
//     public Integer deleteDataPermissionByIdList(List<Long> idList) {
//         log.info("DataPermissionApi.deleteDataPermissionByIdList idList: {}", idList);
//         Valid.notEmpty(idList, ErrorMessage.ID_MISSING);
//         // 删除
//         Integer effect = dataPermissionService.deleteDataPermissionByIdList(idList);
//         log.info("DataPermissionApi.deleteDataPermissionByIdList effect: {}", effect);
//         return effect;
//     }
//
//     @Override
//     public Integer deleteDataPermission(DataPermissionQueryDTO dto) {
//         log.info("DataPermissionApi.deleteDataPermission dto: {}", JSON.toJSONString(dto));
//         Valid.valid(dto);
//         // 删除
//         Integer effect = dataPermissionService.deleteDataPermission(DataPermissionProviderConvert.MAPPER.toRequest(dto));
//         log.info("DataPermissionApi.deleteDataPermission effect: {}", effect);
//         return effect;
//     }
//
//     /**
//      * 构建查询 wrapper
//      *
//      * @param dto dto
//      * @return wrapper
//      */
//     private LambdaQueryWrapper<DataPermissionDO> buildQueryWrapper(DataPermissionQueryDTO dto) {
//         return dataPermissionDAO.wrapper()
//                 .eq(DataPermissionDO::getId, dto.getId())
//                 .eq(DataPermissionDO::getUserId, dto.getUserId())
//                 .eq(DataPermissionDO::getRoleId, dto.getRoleId())
//                 .eq(DataPermissionDO::getRelId, dto.getRelId())
//                 .eq(DataPermissionDO::getType, dto.getType());
//     }
//
// }
