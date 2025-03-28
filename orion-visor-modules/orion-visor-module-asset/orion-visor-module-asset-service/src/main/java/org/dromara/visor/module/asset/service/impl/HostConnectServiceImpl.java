/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
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
package org.dromara.visor.module.asset.service.impl;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.dao.HostIdentityDAO;
import org.dromara.visor.module.asset.dao.HostKeyDAO;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.domain.HostIdentityDO;
import org.dromara.visor.module.asset.entity.domain.HostKeyDO;
import org.dromara.visor.module.asset.entity.dto.TerminalConnectDTO;
import org.dromara.visor.module.asset.entity.request.host.HostTestConnectRequest;
import org.dromara.visor.module.asset.enums.*;
import org.dromara.visor.module.asset.handler.host.config.model.HostSshConfigModel;
import org.dromara.visor.module.asset.handler.host.extra.model.HostSshExtraModel;
import org.dromara.visor.module.asset.handler.host.jsch.SessionStores;
import org.dromara.visor.module.asset.service.AssetAuthorizedDataService;
import org.dromara.visor.module.asset.service.HostConfigService;
import org.dromara.visor.module.asset.service.HostConnectService;
import org.dromara.visor.module.asset.service.HostExtraService;
import org.dromara.visor.module.infra.api.DataPermissionApi;
import org.dromara.visor.module.infra.enums.DataPermissionTypeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 主机连接服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/10/12 23:58
 */
@Slf4j
@Service
public class HostConnectServiceImpl implements HostConnectService {

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private HostConfigService hostConfigService;

    @Resource
    private HostExtraService hostExtraService;

    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @Resource
    private DataPermissionApi dataPermissionApi;

    @Override
    public void testHostConnect(HostTestConnectRequest request) {
        Long id = request.getId();
        HostTypeEnum type = HostTypeEnum.of(request.getType());
        if (HostTypeEnum.SSH.equals(type)) {
            // SSH 连接测试
            SessionStore sessionStore = null;
            try {
                TerminalConnectDTO info = this.getSshConnectInfo(id);
                sessionStore = SessionStores.openSessionStore(info);
            } catch (Exception e) {
                throw Exceptions.app(e.getMessage(), e);
            } finally {
                Streams.close(sessionStore);
            }
        }
    }

    @Override
    public TerminalConnectDTO getSshConnectInfo(Long hostId) {
        log.info("HostConnectService.getSshConnectInfo-withHost hostId: {}", hostId);
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        // 查询主机配置
        HostSshConfigModel config = hostConfigService.getHostConfig(hostId, HostTypeEnum.SSH.name());
        // 获取配置
        return this.getHostConnectInfo(host, config, null);
    }

    @Override
    public TerminalConnectDTO getSshConnectInfo(Long hostId, Long userId) {
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 获取配置
        return this.getSshConnectInfo(host, userId);
    }

    @Override
    public TerminalConnectDTO getSshConnectInfo(HostDO host, Long userId) {
        Long hostId = host.getId();
        log.info("HostConnectService.getSshConnectInfo hostId: {}, userId: {}", hostId, userId);
        // 验证主机是否有权限
        List<Long> hostIdList = assetAuthorizedDataService.getUserAuthorizedHostId(userId);
        Valid.isTrue(hostIdList.contains(hostId),
                ErrorMessage.ANY_NO_PERMISSION,
                DataPermissionTypeEnum.HOST_GROUP.getPermissionName());
        // 获取主机配置
        HostSshConfigModel config = hostConfigService.getHostConfig(hostId, HostTypeEnum.SSH.name());
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
     * 获取主机 SSH 连接配置
     *
     * @param host   host
     * @param config config
     * @param extra  extra
     * @return session
     */
    private TerminalConnectDTO getHostConnectInfo(HostDO host,
                                                  HostSshConfigModel config,
                                                  HostSshExtraModel extra) {
        // 填充认证信息
        TerminalConnectDTO conn = new TerminalConnectDTO();
        conn.setOsType(host.getOsType());
        conn.setArchType(host.getArchType());
        conn.setHostId(host.getId());
        conn.setHostName(host.getName());
        conn.setHostCode(host.getCode());
        conn.setHostAddress(host.getAddress());
        conn.setHostPort(config.getPort());
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
