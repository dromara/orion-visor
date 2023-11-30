package com.orion.ops.module.asset.controller;

import com.orion.ops.framework.log.core.annotation.IgnoreLog;
import com.orion.ops.framework.log.core.enums.IgnoreLogMode;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.framework.web.core.annotation.RestWrapper;
import com.orion.ops.module.asset.entity.request.asset.AssetAuthorizedDataRequest;
import com.orion.ops.module.asset.entity.vo.HostGroupTreeVO;
import com.orion.ops.module.asset.service.AssetAuthorizedDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资产模块 授权数据服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/11/23 14:10
 */
@Tag(name = "asset - 授权数据服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/authorized-data")
public class AssetAuthorizedDataServiceController {

    // FIXME 字典 菜单 http api


    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/host-group")
    @Operation(summary = "查询已授权的主机分组")
    public List<HostGroupTreeVO> getAuthorizedHostGroup() {
        return assetAuthorizedDataService.getUserAuthorizedHostGroup(SecurityUtils.getLoginUserId());
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-host-group")
    @Operation(summary = "获取已授权的分组")
    @PreAuthorize("@ss.hasPermission('asset:host-group:grant')")
    public List<Long> getAuthorizedHostGroup(AssetAuthorizedDataRequest request) {
        return assetAuthorizedDataService.getAuthorizedData(request);
    }


}
