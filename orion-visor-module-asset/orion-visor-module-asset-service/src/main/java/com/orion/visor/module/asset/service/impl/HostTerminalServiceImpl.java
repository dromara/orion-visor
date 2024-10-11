/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orion.visor.module.asset.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.orion.lang.id.UUIds;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.constant.ExtraFieldConst;
import com.orion.visor.framework.common.security.LoginUser;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.redis.core.utils.RedisStrings;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.module.asset.dao.HostDAO;
import com.orion.visor.module.asset.dao.HostIdentityDAO;
import com.orion.visor.module.asset.dao.HostKeyDAO;
import com.orion.visor.module.asset.define.cache.HostTerminalCacheKeyDefine;
import com.orion.visor.module.asset.entity.domain.HostDO;
import com.orion.visor.module.asset.entity.domain.HostIdentityDO;
import com.orion.visor.module.asset.entity.domain.HostKeyDO;
import com.orion.visor.module.asset.entity.dto.HostTerminalAccessDTO;
import com.orion.visor.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.visor.module.asset.entity.dto.HostTerminalTransferDTO;
import com.orion.visor.module.asset.entity.vo.HostTerminalThemeVO;
import com.orion.visor.module.asset.enums.*;
import com.orion.visor.module.asset.handler.host.config.model.HostSshConfigModel;
import com.orion.visor.module.asset.handler.host.extra.model.HostSshExtraModel;
import com.orion.visor.module.asset.service.HostConfigService;
import com.orion.visor.module.asset.service.HostExtraService;
import com.orion.visor.module.asset.service.HostTerminalService;
import com.orion.visor.module.infra.api.DataPermissionApi;
import com.orion.visor.module.infra.api.DictValueApi;
import com.orion.visor.module.infra.enums.DataPermissionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 主机连接服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 14:27
 */
@Slf4j
@Service
public class HostTerminalServiceImpl implements HostTerminalService {

    private static final String THEME_DICT_KEY = "terminalTheme";

    @Resource
    private HostConfigService hostConfigService;

    @Resource
    private HostExtraService hostExtraService;

    @Resource
    private AssetAuthorizedDataServiceImpl assetAuthorizedDataService;

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private DataPermissionApi dataPermissionApi;

    @Resource
    private DictValueApi dictValueApi;

    @Override
    public List<HostTerminalThemeVO> getTerminalThemes() {
        // if (true) {
        //     String arr = "";
        //     return JSON.parseArray(arr, HostTerminalThemeVO.class);
        // }
        List<JSONObject> themes = dictValueApi.getDictValue(THEME_DICT_KEY);
        return themes.stream()
                .map(s -> HostTerminalThemeVO.builder()
                        .name(s.getString(ExtraFieldConst.LABEL))
                        .dark(s.getBoolean(ExtraFieldConst.DARK))
                        .schema(s.getJSONObject(ExtraFieldConst.VALUE))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public String getTerminalAccessToken() {
        LoginUser user = Valid.notNull(SecurityUtils.getLoginUser());
        log.info("HostConnectService.getHostAccessToken userId: {}", user.getId());
        String accessToken = UUIds.random19();
        HostTerminalAccessDTO access = HostTerminalAccessDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build();
        // 设置 access 缓存
        String key = HostTerminalCacheKeyDefine.HOST_TERMINAL_ACCESS.format(accessToken);
        RedisStrings.setJson(key, HostTerminalCacheKeyDefine.HOST_TERMINAL_ACCESS, access);
        return accessToken;
    }

    @Override
    public String getTerminalTransferToken() {
        LoginUser user = Valid.notNull(SecurityUtils.getLoginUser());
        log.info("HostConnectService.getTerminalTransferToken userId: {}", user.getId());
        String transferToken = UUIds.random19();
        HostTerminalTransferDTO transfer = HostTerminalTransferDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build();
        // 设置 transfer 缓存
        String key = HostTerminalCacheKeyDefine.HOST_TERMINAL_TRANSFER.format(transferToken);
        RedisStrings.setJson(key, HostTerminalCacheKeyDefine.HOST_TERMINAL_TRANSFER, transfer);
        return transferToken;
    }

    @Override
    public HostTerminalAccessDTO getAccessInfoByToken(String token) {
        // 获取缓存
        String key = HostTerminalCacheKeyDefine.HOST_TERMINAL_ACCESS.format(token);
        HostTerminalAccessDTO access = RedisStrings.getJson(key, HostTerminalCacheKeyDefine.HOST_TERMINAL_ACCESS);
        // 删除缓存
        if (access != null) {
            RedisStrings.delete(key);
        }
        return access;
    }

    @Override
    public HostTerminalTransferDTO getTransferInfoByToken(String token) {
        // 获取缓存
        String key = HostTerminalCacheKeyDefine.HOST_TERMINAL_TRANSFER.format(token);
        HostTerminalTransferDTO transfer = RedisStrings.getJson(key, HostTerminalCacheKeyDefine.HOST_TERMINAL_TRANSFER);
        // 删除缓存
        if (transfer != null) {
            RedisStrings.delete(key);
        }
        return transfer;
    }

    @Override
    public HostTerminalConnectDTO getTerminalConnectInfo(Long hostId) {
        log.info("HostConnectService.getTerminalConnectInfo-withHost hostId: {}", hostId);
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        // 查询主机配置
        HostSshConfigModel config = hostConfigService.buildHostConfig(host, HostTypeEnum.SSH);
        // 获取配置
        return this.getHostConnectInfo(host, config, null);
    }

    @Override
    public HostTerminalConnectDTO getTerminalConnectInfo(Long userId, Long hostId) {
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 获取配置
        return this.getTerminalConnectInfo(userId, host);
    }

    @Override
    public HostTerminalConnectDTO getTerminalConnectInfo(Long userId, HostDO host) {
        Long hostId = host.getId();
        log.info("HostConnectService.getTerminalConnectInfo hostId: {}, userId: {}", hostId, userId);
        // 验证主机是否有权限
        List<Long> hostIdList = assetAuthorizedDataService.getUserAuthorizedHostId(userId);
        Valid.isTrue(hostIdList.contains(hostId),
                ErrorMessage.ANY_NO_PERMISSION,
                DataPermissionTypeEnum.HOST_GROUP.getPermissionName());
        // 获取主机配置
        HostSshConfigModel config = hostConfigService.buildHostConfig(host, HostTypeEnum.SSH);
        Valid.notNull(config, ErrorMessage.CONFIG_ABSENT);
        // 查询主机额外配置
        HostSshExtraModel extra = hostExtraService.getHostExtra(userId, hostId, HostExtraItemEnum.SSH);
        if (extra != null) {
            HostExtraSshAuthTypeEnum extraAuthType = HostExtraSshAuthTypeEnum.of(extra.getAuthType());
            if (HostExtraSshAuthTypeEnum.CUSTOM_KEY.equals(extraAuthType)) {
                // 验证主机密钥是否有权限
                Valid.notNull(extra.getKeyId(), ErrorMessage.KEY_ABSENT);
                Valid.isTrue(dataPermissionApi.hasPermission(DataPermissionTypeEnum.HOST_KEY, userId, extra.getKeyId()),
                        ErrorMessage.ANY_NO_PERMISSION,
                        DataPermissionTypeEnum.HOST_KEY.getPermissionName());
            } else if (HostExtraSshAuthTypeEnum.CUSTOM_IDENTITY.equals(extraAuthType)) {
                // 验证主机身份是否有权限
                Valid.notNull(extra.getIdentityId(), ErrorMessage.IDENTITY_ABSENT);
                Valid.isTrue(dataPermissionApi.hasPermission(DataPermissionTypeEnum.HOST_IDENTITY, userId, extra.getIdentityId()),
                        ErrorMessage.ANY_NO_PERMISSION,
                        DataPermissionTypeEnum.HOST_IDENTITY.getPermissionName());
            }
        }
        // 获取连接配置
        return this.getHostConnectInfo(host, config, extra);
    }

    /**
     * 获取主机会话连接配置
     *
     * @param host   host
     * @param config config
     * @param extra  extra
     * @return session
     */
    private HostTerminalConnectDTO getHostConnectInfo(HostDO host,
                                                      HostSshConfigModel config,
                                                      HostSshExtraModel extra) {
        // 填充认证信息
        HostTerminalConnectDTO conn = new HostTerminalConnectDTO();
        conn.setHostId(host.getId());
        conn.setHostName(host.getName());
        conn.setHostAddress(host.getAddress());
        conn.setHostPort(host.getPort());
        conn.setOsType(config.getOsType());
        conn.setTimeout(config.getConnectTimeout());
        conn.setCharset(config.getCharset());
        conn.setFileNameCharset(config.getFileNameCharset());
        conn.setFileContentCharset(config.getFileContentCharset());

        // 获取自定义认证方式
        HostExtraSshAuthTypeEnum extraAuthType = Optional.ofNullable(extra)
                .map(HostSshExtraModel::getAuthType)
                .map(HostExtraSshAuthTypeEnum::of)
                .orElse(null);
        if (HostExtraSshAuthTypeEnum.CUSTOM_KEY.equals(extraAuthType)) {
            // 自定义密钥
            config.setAuthType(HostSshAuthTypeEnum.KEY.name());
            config.setKeyId(extra.getKeyId());
            if (extra.getUsername() != null) {
                config.setUsername(extra.getUsername());
            }
        } else if (HostExtraSshAuthTypeEnum.CUSTOM_IDENTITY.equals(extraAuthType)) {
            // 自定义身份
            config.setAuthType(HostSshAuthTypeEnum.IDENTITY.name());
            config.setIdentityId(extra.getIdentityId());
        }

        // 身份认证
        HostSshAuthTypeEnum authType = HostSshAuthTypeEnum.of(config.getAuthType());
        if (HostSshAuthTypeEnum.IDENTITY.equals(authType)) {
            // 身份认证
            Valid.notNull(config.getIdentityId(), ErrorMessage.IDENTITY_ABSENT);
            HostIdentityDO identity = hostIdentityDAO.selectById(config.getIdentityId());
            Valid.notNull(identity, ErrorMessage.IDENTITY_ABSENT);
            config.setUsername(identity.getUsername());
            HostIdentityTypeEnum identityType = HostIdentityTypeEnum.of(identity.getType());
            if (HostIdentityTypeEnum.PASSWORD.equals(identityType)) {
                // 密码类型
                authType = HostSshAuthTypeEnum.PASSWORD;
                config.setPassword(identity.getPassword());
            } else if (HostIdentityTypeEnum.KEY.equals(identityType)) {
                // 密钥类型
                authType = HostSshAuthTypeEnum.KEY;
                config.setKeyId(identity.getKeyId());
            }
        }

        // 填充认证信息
        conn.setUsername(config.getUsername());
        if (HostSshAuthTypeEnum.PASSWORD.equals(authType)) {
            // 密码认证
            conn.setPassword(config.getPassword());
        } else if (HostSshAuthTypeEnum.KEY.equals(authType)) {
            // 密钥认证
            Long keyId = config.getKeyId();
            Valid.notNull(keyId, ErrorMessage.KEY_ABSENT);
            HostKeyDO key = hostKeyDAO.selectById(keyId);
            Valid.notNull(key, ErrorMessage.KEY_ABSENT);
            conn.setKeyId(keyId);
            conn.setPublicKey(key.getPublicKey());
            conn.setPrivateKey(key.getPrivateKey());
            conn.setPrivateKeyPassword(key.getPassword());
        }
        return conn;
    }

}
