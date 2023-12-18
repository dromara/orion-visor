// package com.orion.ops.module.infra.api;
//
// import com.orion.ops.module.infra.entity.dto.data.*;
//
// import java.util.List;
//
// /**
//  * 数据别名 对外服务类
//  *
//  * @author Jiahang Li
//  * @version 1.0.0
//  * @since 2023-12-18 17:37
//  */
// public interface DataAliasApi {
//
//     /**
//      * 创建数据别名
//      *
//      * @param dto dto
//      * @return id
//      */
//     Long createDataAlias(DataAliasCreateDTO dto);
//
//     /**
//      * 更新数据别名
//      *
//      * @param dto dto
//      * @return effect
//      */
//     Integer updateDataAliasById(DataAliasUpdateDTO dto);
//
//     /**
//      * 查询数据别名
//      *
//      * @param id id
//      * @return row
//      */
//     DataAliasDTO getDataAliasById(Long id);
//
//     /**
//      * 批量查询数据别名
//      *
//      * @param idList idList
//      * @return rows
//      */
//     List<DataAliasDTO> getDataAliasByIdList(List<Long> idList);
//
//     /**
//      * 查询全部数据别名
//      *
//      * @param dto dto
//      * @return rows
//      */
//     List<DataAliasDTO> getDataAliasList(DataAliasQueryDTO dto);
//
//     /**
//      * 通过缓存查询数据别名
//      *
//      * @return rows
//      */
//     List<DataAliasDTO> getDataAliasListByCache();
//
//     /**
//      * 查询数据别名数量
//      *
//      * @param dto dto
//      * @return count
//      */
//     Long getDataAliasCount(DataAliasQueryDTO dto);
//
//     /**
//      * 删除数据别名
//      *
//      * @param id id
//      * @return effect
//      */
//     Integer deleteDataAliasById(Long id);
//
//     /**
//      * 批量删除数据别名
//      *
//      * @param idList idList
//      * @return effect
//      */
//     Integer deleteDataAliasByIdList(List<Long> idList);
//
//     /**
//      * 根据条件删除数据别名
//      *
//      * @param dto dto
//      * @return effect
//      */
//     Integer deleteDataAlias(DataAliasQueryDTO dto);
//
// }
