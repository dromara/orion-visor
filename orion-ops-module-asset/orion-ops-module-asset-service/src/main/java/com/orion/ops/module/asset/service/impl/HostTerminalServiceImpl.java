package com.orion.ops.module.asset.service.impl;

import com.orion.lang.exception.AuthenticationException;
import com.orion.lang.id.UUIds;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.net.host.SessionHolder;
import com.orion.net.host.SessionStore;
import com.orion.ops.framework.biz.operator.log.core.uitls.OperatorLogs;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.utils.CryptoUtils;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.redis.core.utils.RedisStrings;
import com.orion.ops.module.asset.dao.HostDAO;
import com.orion.ops.module.asset.dao.HostIdentityDAO;
import com.orion.ops.module.asset.dao.HostKeyDAO;
import com.orion.ops.module.asset.define.cache.HostTerminalCacheKeyDefine;
import com.orion.ops.module.asset.entity.domain.HostDO;
import com.orion.ops.module.asset.entity.domain.HostIdentityDO;
import com.orion.ops.module.asset.entity.domain.HostKeyDO;
import com.orion.ops.module.asset.entity.dto.HostSshConnectDTO;
import com.orion.ops.module.asset.enums.HostConfigTypeEnum;
import com.orion.ops.module.asset.enums.HostExtraItemEnum;
import com.orion.ops.module.asset.enums.HostExtraSshAuthTypeEnum;
import com.orion.ops.module.asset.enums.HostSshAuthTypeEnum;
import com.orion.ops.module.asset.handler.host.config.model.HostSshConfigModel;
import com.orion.ops.module.asset.handler.host.extra.model.HostSshExtraModel;
import com.orion.ops.module.asset.service.HostConfigService;
import com.orion.ops.module.asset.service.HostConnectLogService;
import com.orion.ops.module.asset.service.HostExtraService;
import com.orion.ops.module.asset.service.HostTerminalService;
import com.orion.ops.module.infra.api.DataPermissionApi;
import com.orion.ops.module.infra.api.SystemUserApi;
import com.orion.ops.module.infra.entity.dto.user.SystemUserDTO;
import com.orion.ops.module.infra.enums.DataPermissionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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

    @Resource
    private HostConfigService hostConfigService;

    @Resource
    private HostExtraService hostExtraService;

    @Resource
    private AssetAuthorizedDataServiceImpl assetAuthorizedDataService;

    @Resource
    private HostConnectLogService hostConnectLogService;

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostIdentityDAO hostIdentityDAO;

    @Resource
    private HostKeyDAO hostKeyDAO;

    @Resource
    private DataPermissionApi dataPermissionApi;

    @Resource
    private SystemUserApi systemUserApi;

    @Override
    public String getHostAccessToken(Long hostId, Long userId) {
        log.info("HostConnectService.getHostAccessToken hostId: {}, userId: {}", hostId, userId);
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 查询用户
        SystemUserDTO user = systemUserApi.getUserById(userId);
        Valid.notNull(user, ErrorMessage.USER_ABSENT);
        // 查询主机配置
        HostSshConfigModel config = hostConfigService.getHostConfig(hostId, HostConfigTypeEnum.SSH);
        Valid.notNull(config, ErrorMessage.CONFIG_ABSENT);
        // 查询主机额外配置
        HostSshExtraModel extra = hostExtraService.getHostExtra(userId, hostId, HostExtraItemEnum.SSH);
        // 非管理员检查权限
        if (!systemUserApi.isAdminUser(userId)) {
            // 验证主机是否有权限
            List<Long> hostIdList = assetAuthorizedDataService.getUserAuthorizedHostId(userId);
            Valid.isTrue(hostIdList.contains(hostId),
                    ErrorMessage.ANY_NO_PERMISSION,
                    DataPermissionTypeEnum.HOST_GROUP.getPermissionName());
            // 检查额外配置权限
            if (extra != null) {
                HostExtraSshAuthTypeEnum extraAuthType = HostExtraSshAuthTypeEnum.of(extra.getAuthType());
                if (HostExtraSshAuthTypeEnum.CUSTOM_KEY.equals(extraAuthType)) {
                    // 验证主机秘钥是否有权限
                    Valid.isTrue(dataPermissionApi.hasPermission(DataPermissionTypeEnum.HOST_KEY, userId, extra.getKeyId()),
                            ErrorMessage.ANY_NO_PERMISSION,
                            DataPermissionTypeEnum.HOST_KEY.getPermissionName());
                } else if (HostExtraSshAuthTypeEnum.CUSTOM_IDENTITY.equals(extraAuthType)) {
                    // 验证主机身份是否有权限
                    Valid.isTrue(dataPermissionApi.hasPermission(DataPermissionTypeEnum.HOST_IDENTITY, userId, extra.getIdentityId()),
                            ErrorMessage.ANY_NO_PERMISSION,
                            DataPermissionTypeEnum.HOST_IDENTITY.getPermissionName());
                }
            }
        }
        String token = UUIds.random32();
        // 获取连接配置
        HostSshConnectDTO connect = this.getHostConnectInfo(host, config, extra);
        connect.setUserId(userId);
        connect.setUsername(user.getUsername());
        connect.setToken(token);
        // 设置缓存
        String key = HostTerminalCacheKeyDefine.HOST_TERMINAL_CONNECT.format(token);
        RedisStrings.setJson(key, HostTerminalCacheKeyDefine.HOST_TERMINAL_CONNECT, connect);
        // 设置日志参数
        OperatorLogs.add(connect);
        return token;
    }

    @Override
    public HostSshConnectDTO getConnectInfoByToken(String token) {
        String key = HostTerminalCacheKeyDefine.HOST_TERMINAL_CONNECT.format(token);
        return RedisStrings.getJson(key, HostTerminalCacheKeyDefine.HOST_TERMINAL_CONNECT);
    }

    @Override
    public SessionStore openSessionStore(Long hostId) {
        log.info("HostConnectService.openSessionStore-withHost hostId: {}", hostId);
        // 查询主机
        HostDO host = hostDAO.selectById(hostId);
        Valid.notNull(host, ErrorMessage.HOST_ABSENT);
        // 查询主机配置
        HostSshConfigModel model = hostConfigService.getHostConfig(hostId, HostConfigTypeEnum.SSH);
        Valid.notNull(model, ErrorMessage.CONFIG_ABSENT);
        // 获取配置
        HostSshConnectDTO connect = this.getHostConnectInfo(host, model, null);
        // 打开连接
        return this.openSessionStore(connect);
    }

    @Override
    public SessionStore openSessionStore(HostSshConnectDTO conn) {
        Long hostId = conn.getHostId();
        String address = conn.getAddress();
        String username = conn.getUsername();
        log.info("HostConnectService-openSessionStore-start hostId: {}, address: {}, username: {}", hostId, address, username);
        try {
            SessionHolder sessionHolder = new SessionHolder();
            final boolean useKey = conn.getKeyId() != null;
            // 使用秘钥认证
            if (useKey) {
                // 加载秘钥
                String publicKey = Optional.ofNullable(conn.getPublicKey())
                        .map(CryptoUtils::decryptAsString)
                        .orElse(null);
                String privateKey = Optional.ofNullable(conn.getPrivateKey())
                        .map(CryptoUtils::decryptAsString)
                        .orElse(null);
                String password = Optional.ofNullable(conn.getPrivateKeyPassword())
                        .map(CryptoUtils::decryptAsString)
                        .orElse(null);
                sessionHolder.addIdentityValue(String.valueOf(conn.getKeyId()),
                        privateKey,
                        publicKey,
                        password);
            }
            // 获取会话
            SessionStore session = sessionHolder.getSession(address, conn.getPort(), username);
            // 使用密码认证
            if (!useKey) {
                session.password(CryptoUtils.decryptAsString(conn.getPassword()));
            }
            // 连接
            session.connect(conn.getTimeout());
            log.info("HostConnectService-openSessionStore-success hostId: {}, address: {}, username: {}", hostId, address, username);
            return session;
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("HostConnectService-openSessionStore-error hostId: {}, address: {}, username: {}, message: {}", hostId, address, username, message, e);
            if (Strings.contains(message, Const.TIMEOUT)) {
                // 连接超时
                throw Exceptions.timeout(message, e);
            } else if (e instanceof AuthenticationException) {
                // 认证失败
                throw Exceptions.authentication(message, e);
            } else {
                throw e;
            }
        }
    }

    /**
     * 获取主机会话连接配置
     *
     * @param host   host
     * @param config config
     * @param extra  extra
     * @return session
     */
    private HostSshConnectDTO getHostConnectInfo(HostDO host,
                                                 HostSshConfigModel config,
                                                 HostSshExtraModel extra) {
        // 获取认证方式
        HostSshAuthTypeEnum authType = HostSshAuthTypeEnum.of(config.getAuthType());
        HostExtraSshAuthTypeEnum extraAuthType = Optional.ofNullable(extra)
                .map(HostSshExtraModel::getAuthType)
                .map(HostExtraSshAuthTypeEnum::of)
                .orElse(HostExtraSshAuthTypeEnum.DEFAULT);
        if (HostExtraSshAuthTypeEnum.CUSTOM_KEY.equals(extraAuthType)) {
            // 自定义秘钥
            authType = HostSshAuthTypeEnum.KEY;
            config.setKeyId(extra.getKeyId());
            if (extra.getUsername() != null) {
                config.setUsername(extra.getUsername());
            }
        } else if (HostExtraSshAuthTypeEnum.CUSTOM_IDENTITY.equals(extraAuthType)) {
            // 自定义身份
            authType = HostSshAuthTypeEnum.IDENTITY;
            config.setIdentityId(extra.getIdentityId());
        }
        Long keyId = null;
        // 填充认证信息
        HostSshConnectDTO conn = new HostSshConnectDTO();
        conn.setHostId(host.getId());
        conn.setHostName(host.getName());
        conn.setAddress(host.getAddress());
        conn.setPort(config.getPort());
        conn.setTimeout(config.getConnectTimeout());
        conn.setUsername(config.getUsername());
        // 填充身份信息
        if (HostSshAuthTypeEnum.PASSWORD.equals(authType)) {
            conn.setPassword(config.getPassword());
        } else if (HostSshAuthTypeEnum.KEY.equals(authType)) {
            // 秘钥认证
            keyId = config.getKeyId();
        } else if (HostSshAuthTypeEnum.IDENTITY.equals(authType)) {
            // 身份认证
            HostIdentityDO identity = hostIdentityDAO.selectById(config.getIdentityId());
            Valid.notNull(identity, ErrorMessage.IDENTITY_ABSENT);
            keyId = identity.getKeyId();
            conn.setUsername(identity.getUsername());
            conn.setPassword(identity.getPassword());
        }
        // 设置秘钥信息
        if (keyId != null) {
            HostKeyDO key = hostKeyDAO.selectById(keyId);
            Valid.notNull(key, ErrorMessage.KEY_ABSENT);
            conn.setPublicKey(key.getPublicKey());
            conn.setPrivateKey(key.getPrivateKey());
            conn.setPrivateKeyPassword(key.getPassword());
        }
        // 连接
        return conn;
    }

}
