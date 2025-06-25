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
package org.dromara.visor.common.session.ssh;

import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.net.host.SessionHolder;
import cn.orionsec.kit.net.host.SessionLogger;
import cn.orionsec.kit.net.host.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.AppConst;
import org.dromara.visor.common.session.SessionMessage;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.common.utils.AesEncryptUtils;

import java.util.Optional;

/**
 * sessionStore 工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/7/11 16:58
 */
@Slf4j
public class SessionStores {

    /**
     * 打开 sessionStore
     *
     * @param config config
     * @return sessionStore
     */
    public static SessionStore openSessionStore(SshConnectConfig config) {
        Long hostId = config.getHostId();
        String address = config.getHostAddress();
        String username = config.getUsername();
        log.info("SessionStores-open-start hostId: {}, address: {}, username: {}", hostId, address, username);
        try {
            // 创建会话
            SessionHolder sessionHolder = SessionHolder.create();
            sessionHolder.setLogger(SessionLogger.INFO);
            SessionStore session = createSessionStore(config, sessionHolder);
            // 设置版本
            session.getSession().setClientVersion("SSH-2.0-ORION_VISOR_V" + AppConst.VERSION);
            // 连接
            session.connect();
            log.info("SessionStores-open-success hostId: {}, address: {}, username: {}", hostId, address, username);
            return session;
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("SessionStores-open-error hostId: {}, address: {}, username: {}, message: {}", hostId, address, username, message, e);
            throw Exceptions.app(SessionMessage.getErrorMessage(address, e), e);
        }
    }

    /**
     * 创建 sessionStore
     *
     * @param config        config
     * @param sessionHolder sessionHolder
     * @return sessionStore
     */
    private static SessionStore createSessionStore(SshConnectConfig config, SessionHolder sessionHolder) {
        final boolean useKey = config.getKeyId() != null;
        // 使用密钥认证
        if (useKey) {
            // 加载密钥
            String publicKey = Optional.ofNullable(config.getPublicKey())
                    .map(AesEncryptUtils::decryptAsString)
                    .orElse(null);
            String privateKey = Optional.ofNullable(config.getPrivateKey())
                    .map(AesEncryptUtils::decryptAsString)
                    .orElse(null);
            String password = Optional.ofNullable(config.getPrivateKeyPassword())
                    .map(AesEncryptUtils::decryptAsString)
                    .orElse(null);
            sessionHolder.addIdentityValue(String.valueOf(config.getKeyId()),
                    privateKey,
                    publicKey,
                    password);
        }
        // 获取会话
        SessionStore session = sessionHolder.getSession(config.getHostAddress(), config.getHostPort(), config.getUsername());
        // 使用密码认证
        if (!useKey) {
            String password = config.getPassword();
            if (!Strings.isEmpty(password)) {
                session.password(AesEncryptUtils.decryptAsString(password));
            }
        }
        // 超时时间
        session.timeout(config.getTimeout());
        return session;
    }

}
