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

import cn.orionsec.kit.lang.utils.Booleans;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.session.config.BaseConnectConfig;
import org.dromara.visor.common.session.config.RdpConnectConfig;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.common.session.config.VncConnectConfig;
import org.dromara.visor.common.session.ssh.SessionStores;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.dao.HostIdentityDAO;
import org.dromara.visor.module.asset.dao.HostKeyDAO;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.domain.HostIdentityDO;
import org.dromara.visor.module.asset.entity.domain.HostKeyDO;
import org.dromara.visor.module.asset.entity.dto.host.HostRdpConfigDTO;
import org.dromara.visor.module.asset.entity.dto.host.HostSshConfigDTO;
import org.dromara.visor.module.asset.entity.dto.host.HostVncConfigDTO;
import org.dromara.visor.module.asset.entity.request.host.HostTestConnectRequest;
import org.dromara.visor.module.asset.enums.HostAuthTypeEnum;
import org.dromara.visor.module.asset.enums.HostExtraAuthTypeEnum;
import org.dromara.visor.module.asset.enums.HostIdentityTypeEnum;
import org.dromara.visor.module.asset.enums.HostTypeEnum;
import org.dromara.visor.module.asset.handler.host.extra.HostExtraItemEnum;
import org.dromara.visor.module.asset.handler.host.extra.model.HostRdpExtraModel;
import org.dromara.visor.module.asset.handler.host.extra.model.HostSshExtraModel;
import org.dromara.visor.module.asset.handler.host.extra.model.HostVncExtraModel;
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
                SshConnectConfig config = this.getSshConnectConfig(id);
                sessionStore = SessionStores.openSessionStore(config);
            } catch (Exception e) {
                throw Exceptions.app(e.getMessage(), e);
            } finally {
                Streams.close(sessionStore);
            }
        }
    }

    @Override
    public SshConnectConfig getSshConnectConfig(Long hostId) {
        log.info("HostConnectService.getSshConnectConfig-withHost hostId: {}", hostId);
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        // 查询主机配置
        HostSshConfigDTO config = hostConfigService.getHostConfig(hostId, HostTypeEnum.SSH.name());
        // 获取配置
        return this.getSshConnectConfig(host, config, null);
    }

    @Override
    public SshConnectConfig getSshConnectConfig(Long hostId, Long userId) {
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 获取配置
        return this.getSshConnectConfig(host, userId);
    }

    @Override
    public SshConnectConfig getSshConnectConfig(HostDO host, Long userId) {
        Long hostId = host.getId();
        log.info("HostConnectService.getSshConnectConfig hostId: {}, userId: {}", hostId, userId);
        // 验证权限
        this.validHostAuthorized(userId, hostId);
        // 获取主机配置
        HostSshConfigDTO config = hostConfigService.getHostConfig(hostId, HostTypeEnum.SSH.name());
        Valid.notNull(config, ErrorMessage.CONFIG_ABSENT);
        // 查询主机额外配置
        HostSshExtraModel extra = hostExtraService.getHostExtra(userId, hostId, HostExtraItemEnum.SSH);
        if (extra != null) {
            // 验证额外认证方式
            this.validExtraAuthentication(userId, extra.getAuthType(), extra.getKeyId(), extra.getIdentityId());
        }
        // 获取连接配置
        return this.getSshConnectConfig(host, config, extra);
    }

    @Override
    public RdpConnectConfig getRdpConnectConfig(Long hostId) {
        log.info("HostConnectService.getRdpConnectConfig-withHost hostId: {}", hostId);
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        // 查询主机配置
        HostRdpConfigDTO config = hostConfigService.getHostConfig(hostId, HostTypeEnum.RDP.name());
        // 获取配置
        return this.getRdpConnectConfig(host, config, null);
    }

    @Override
    public RdpConnectConfig getRdpConnectConfig(Long hostId, Long userId) {
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 获取配置
        return this.getRdpConnectConfig(host, userId);
    }

    @Override
    public RdpConnectConfig getRdpConnectConfig(HostDO host, Long userId) {
        Long hostId = host.getId();
        log.info("HostConnectService.getRdpConnectConfig hostId: {}, userId: {}", hostId, userId);
        // 验证权限
        this.validHostAuthorized(userId, hostId);
        // 获取主机配置
        HostRdpConfigDTO config = hostConfigService.getHostConfig(hostId, HostTypeEnum.RDP.name());
        Valid.notNull(config, ErrorMessage.CONFIG_ABSENT);
        // 查询主机额外配置
        HostRdpExtraModel extra = hostExtraService.getHostExtra(userId, hostId, HostExtraItemEnum.RDP);
        if (extra != null) {
            // 验证额外认证方式
            this.validExtraAuthentication(userId, extra.getAuthType(), null, extra.getIdentityId());
        }
        // 获取连接配置
        return this.getRdpConnectConfig(host, config, extra);
    }

    @Override
    public VncConnectConfig getVncConnectConfig(Long hostId) {
        log.info("HostConnectService.getVncConnectConfig-withHost hostId: {}", hostId);
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        // 查询主机配置
        HostVncConfigDTO config = hostConfigService.getHostConfig(hostId, HostTypeEnum.VNC.name());
        // 获取配置
        return this.getVncConnectConfig(host, config, null);
    }

    @Override
    public VncConnectConfig getVncConnectConfig(Long hostId, Long userId) {
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 获取配置
        return this.getVncConnectConfig(host, userId);
    }

    @Override
    public VncConnectConfig getVncConnectConfig(HostDO host, Long userId) {
        Long hostId = host.getId();
        log.info("HostConnectService.getVncConnectConfig hostId: {}, userId: {}", hostId, userId);
        // 验证权限
        this.validHostAuthorized(userId, hostId);
        // 获取主机配置
        HostVncConfigDTO config = hostConfigService.getHostConfig(hostId, HostTypeEnum.VNC.name());
        Valid.notNull(config, ErrorMessage.CONFIG_ABSENT);
        // 查询主机额外配置
        HostVncExtraModel extra = hostExtraService.getHostExtra(userId, hostId, HostExtraItemEnum.VNC);
        // 获取连接配置
        return this.getVncConnectConfig(host, config, extra);
    }

    /**
     * 获取主机 SSH 连接配置
     *
     * @param host   host
     * @param config config
     * @param extra  extra
     * @return info
     */
    private SshConnectConfig getSshConnectConfig(HostDO host,
                                                 HostSshConfigDTO config,
                                                 HostSshExtraModel extra) {
        SshConnectConfig connectConfig = SshConnectConfig.builder()
                .hostPort(config.getPort())
                .timeout(config.getConnectTimeout())
                .charset(config.getCharset())
                .fileNameCharset(config.getFileNameCharset())
                .fileContentCharset(config.getFileContentCharset())
                .build();
        // 填充基础主机信息
        this.setBaseConnectConfig(connectConfig, host);
        // 获取自定义认证方式
        HostExtraAuthTypeEnum extraAuthType = Optional.ofNullable(extra)
                .map(HostSshExtraModel::getAuthType)
                .map(HostExtraAuthTypeEnum::of)
                .orElse(null);
        if (HostExtraAuthTypeEnum.CUSTOM_KEY.equals(extraAuthType)) {
            // 自定义密钥
            config.setAuthType(HostAuthTypeEnum.KEY.name());
            config.setKeyId(extra.getKeyId());
            if (extra.getUsername() != null) {
                config.setUsername(extra.getUsername());
            }
        } else if (HostExtraAuthTypeEnum.CUSTOM_IDENTITY.equals(extraAuthType)) {
            // 自定义身份
            config.setAuthType(HostAuthTypeEnum.IDENTITY.name());
            config.setIdentityId(extra.getIdentityId());
        }

        // 身份认证
        HostAuthTypeEnum authType = HostAuthTypeEnum.of(config.getAuthType());
        if (HostAuthTypeEnum.IDENTITY.equals(authType)) {
            // 身份认证
            Valid.notNull(config.getIdentityId(), ErrorMessage.IDENTITY_ABSENT);
            HostIdentityDO identity = hostIdentityDAO.selectById(config.getIdentityId());
            Valid.notNull(identity, ErrorMessage.IDENTITY_ABSENT);
            config.setUsername(identity.getUsername());
            HostIdentityTypeEnum identityType = HostIdentityTypeEnum.of(identity.getType());
            if (HostIdentityTypeEnum.PASSWORD.equals(identityType)) {
                // 密码类型
                authType = HostAuthTypeEnum.PASSWORD;
                config.setPassword(identity.getPassword());
            } else if (HostIdentityTypeEnum.KEY.equals(identityType)) {
                // 密钥类型
                authType = HostAuthTypeEnum.KEY;
                config.setKeyId(identity.getKeyId());
            }
        }

        // 填充认证信息
        connectConfig.setUsername(config.getUsername());
        if (HostAuthTypeEnum.PASSWORD.equals(authType)) {
            // 密码认证
            connectConfig.setPassword(config.getPassword());
        } else if (HostAuthTypeEnum.KEY.equals(authType)) {
            // 密钥认证
            this.setSshKey(config.getKeyId(), connectConfig);
        }
        return connectConfig;
    }

    /**
     * 获取 RDP 连接信息
     *
     * @param host   host
     * @param config config
     * @return info
     */
    private RdpConnectConfig getRdpConnectConfig(HostDO host,
                                                 HostRdpConfigDTO config,
                                                 HostRdpExtraModel extra) {
        // 填充认证信息
        RdpConnectConfig connectConfig = RdpConnectConfig.builder()
                .hostPort(config.getPort())
                .username(config.getUsername())
                .password(config.getPassword())
                .versionGt81(config.getVersionGt81())
                .timezone(config.getTimezone())
                .keyboardLayout(config.getKeyboardLayout())
                .clipboardNormalize(config.getClipboardNormalize())
                .domain(config.getDomain())
                .preConnectionId(config.getPreConnectionId())
                .preConnectionBlob(config.getPreConnectionBlob())
                .remoteApp(config.getRemoteApp())
                .remoteAppDir(config.getRemoteAppDir())
                .remoteAppArgs(config.getRemoteAppArgs())
                .build();
        // 填充基础主机信息
        this.setBaseConnectConfig(connectConfig, host);
        if (extra != null) {
            // 设置额外配置信息
            connectConfig.setLowBandwidthMode(extra.getLowBandwidthMode());
            connectConfig.setInitialProgram(extra.getInitialProgram());
            // 获取自定义认证方式
            HostExtraAuthTypeEnum extraAuthType = HostExtraAuthTypeEnum.of(extra.getAuthType());
            if (HostExtraAuthTypeEnum.CUSTOM_IDENTITY.equals(extraAuthType)) {
                // 自定义身份
                config.setAuthType(HostAuthTypeEnum.IDENTITY.name());
                config.setIdentityId(extra.getIdentityId());
            }
        }
        // 填充身份认证信息
        if (HostAuthTypeEnum.IDENTITY.name().equals(config.getAuthType())) {
            this.setIdentityPasswordAuthorization(config.getIdentityId(), connectConfig);
        }
        return connectConfig;
    }

    /**
     * 获取 VNC 连接信息
     *
     * @param host   host
     * @param config config
     * @return info
     */
    private VncConnectConfig getVncConnectConfig(HostDO host,
                                                 HostVncConfigDTO config,
                                                 HostVncExtraModel extra) {
        // 填充认证信息
        VncConnectConfig connectConfig = VncConnectConfig.builder()
                .hostPort(config.getPort())
                .username(config.getUsername())
                .password(config.getPassword())
                .timezone(config.getTimezone())
                .clipboardEncoding(config.getClipboardEncoding())
                .build();
        // 填充基础主机信息
        this.setBaseConnectConfig(connectConfig, host);
        if (extra != null) {
            // 设置额外配置信息
            connectConfig.setLowBandwidthMode(extra.getLowBandwidthMode());
            connectConfig.setSwapRedBlue(extra.getSwapRedBlue());
            // 设置自定义端口
            Integer extraPort = extra.getPort();
            if (extraPort != null) {
                connectConfig.setHostPort(extraPort);
            }
        }
        // 填充身份认证信息
        if (HostAuthTypeEnum.IDENTITY.name().equals(config.getAuthType())) {
            this.setIdentityPasswordAuthorization(config.getIdentityId(), connectConfig);
        }
        // 无用户名
        if (Booleans.isTrue(config.getNoUsername())) {
            connectConfig.setUsername(null);
        }
        // 无密码
        if (Booleans.isTrue(config.getNoPassword())) {
            connectConfig.setPassword(null);
        }
        return connectConfig;
    }

    /**
     * 验证主机权限
     *
     * @param userId userId
     * @param hostId hostId
     */
    private void validHostAuthorized(Long userId, Long hostId) {
        // 验证主机是否有权限
        List<Long> hostIdList = assetAuthorizedDataService.getUserAuthorizedHostId(userId);
        Valid.isTrue(hostIdList.contains(hostId),
                ErrorMessage.ANY_NO_PERMISSION,
                DataPermissionTypeEnum.HOST_GROUP.getPermissionName());
    }

    /**
     * 验证额外认证方式
     *
     * @param userId     userId
     * @param authType   authType
     * @param keyId      keyId
     * @param identityId identityId
     */
    private void validExtraAuthentication(Long userId, String authType, Long keyId, Long identityId) {
        HostExtraAuthTypeEnum extraAuthType = HostExtraAuthTypeEnum.of(authType);
        if (HostExtraAuthTypeEnum.CUSTOM_KEY.equals(extraAuthType)) {
            // 验证主机密钥是否有权限
            Valid.notNull(keyId, ErrorMessage.KEY_ABSENT);
            Valid.isTrue(dataPermissionApi.hasPermission(DataPermissionTypeEnum.HOST_KEY, userId, keyId),
                    ErrorMessage.ANY_NO_PERMISSION,
                    DataPermissionTypeEnum.HOST_KEY.getPermissionName());
        } else if (HostExtraAuthTypeEnum.CUSTOM_IDENTITY.equals(extraAuthType)) {
            // 验证主机身份是否有权限
            Valid.notNull(identityId, ErrorMessage.IDENTITY_ABSENT);
            Valid.isTrue(dataPermissionApi.hasPermission(DataPermissionTypeEnum.HOST_IDENTITY, userId, identityId),
                    ErrorMessage.ANY_NO_PERMISSION,
                    DataPermissionTypeEnum.HOST_IDENTITY.getPermissionName());
        }
    }

    /**
     * 设置密码认证
     *
     * @param identityId    identityId
     * @param connectConfig connectConfig
     */
    private void setIdentityPasswordAuthorization(Long identityId, BaseConnectConfig connectConfig) {
        if (identityId == null) {
            return;
        }
        // 查询身份信息
        HostIdentityDO identity = hostIdentityDAO.selectById(identityId);
        Valid.notNull(identity, ErrorMessage.IDENTITY_ABSENT);
        // 设置身份信息
        connectConfig.setUsername(identity.getUsername());
        connectConfig.setPassword(identity.getPassword());
    }

    /**
     * 设置 SSH 密钥信息
     *
     * @param keyId         keyId
     * @param connectConfig connectConfig
     */
    private void setSshKey(Long keyId, SshConnectConfig connectConfig) {
        Valid.notNull(keyId, ErrorMessage.KEY_ABSENT);
        // 查询密钥信息
        HostKeyDO key = hostKeyDAO.selectById(keyId);
        Valid.notNull(key, ErrorMessage.KEY_ABSENT);
        connectConfig.setKeyId(keyId);
        connectConfig.setPublicKey(key.getPublicKey());
        connectConfig.setPrivateKey(key.getPrivateKey());
        connectConfig.setPrivateKeyPassword(key.getPassword());
    }

    /**
     * 设置基础主机信息
     *
     * @param config config
     * @param host   host
     */
    private void setBaseConnectConfig(BaseConnectConfig config, HostDO host) {
        config.setOsType(host.getOsType());
        config.setArchType(host.getArchType());
        config.setHostId(host.getId());
        config.setHostName(host.getName());
        config.setHostCode(host.getCode());
        config.setHostAddress(host.getAddress());
        config.setAgentKey(host.getAgentKey());
    }

}
