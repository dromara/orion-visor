package com.orion.ops.module.asset.service;

import com.orion.ops.module.asset.entity.request.asset.AssetAuthorizedDataRequest;
import com.orion.ops.module.asset.entity.vo.HostGroupTreeVO;

import java.util.List;

/**
 * 资产模块 授权数据服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/30 18:33
 */
public interface AssetAuthorizedDataService {

    /**
     * 获取已授权的数据
     *
     * @param request request
     * @return dataId
     */
    List<Long> getAuthorizedData(AssetAuthorizedDataRequest request);

    /**
     * 查询用户已授权的主机分组和主机
     *
     * @param userId userId
     * @return group
     */
    List<HostGroupTreeVO> getUserAuthorizedHostGroup(Long userId);

}
