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
package org.dromara.visor.module.asset.handler.host.jsch;

import cn.orionsec.kit.lang.exception.AuthenticationException;
import cn.orionsec.kit.lang.exception.argument.InvalidArgumentException;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.net.host.SessionHolder;
import cn.orionsec.kit.net.host.SessionLogger;
import cn.orionsec.kit.net.host.SessionStore;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.AppConst;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.utils.CryptoUtils;
import org.dromara.visor.module.asset.entity.dto.TerminalConnectDTO;

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

    protected static final ThreadLocal<String> CURRENT_ADDRESS = new ThreadLocal<>();

    /**
     * 打开 sessionStore
     *
     * @param conn conn
     * @return sessionStore
     */
    public static SessionStore openSessionStore(TerminalConnectDTO conn) {
        Long hostId = conn.getHostId();
        String address = conn.getHostAddress();
        String username = conn.getUsername();
        log.info("SessionStores-open-start hostId: {}, address: {}, username: {}", hostId, address, username);
        try {
            CURRENT_ADDRESS.set(address);
            // 创建会话
            SessionHolder sessionHolder = SessionHolder.create();
            sessionHolder.setLogger(SessionLogger.INFO);
            SessionStore session = createSessionStore(conn, sessionHolder);
            // 设置版本
            session.getSession().setClientVersion("SSH-2.0-ORION_VISOR_V" + AppConst.VERSION);
            // 连接
            session.connect();
            log.info("SessionStores-open-success hostId: {}, address: {}, username: {}", hostId, address, username);
            return session;
        } catch (Exception e) {
            String message = e.getMessage();
            log.error("SessionStores-open-error hostId: {}, address: {}, username: {}, message: {}", hostId, address, username, message, e);
            throw Exceptions.app(getErrorMessage(e), e);
        } finally {
            CURRENT_ADDRESS.remove();
        }
    }

    /**
     * 创建 sessionStore
     *
     * @param conn          conn
     * @param sessionHolder sessionHolder
     * @return sessionStore
     */
    private static SessionStore createSessionStore(TerminalConnectDTO conn, SessionHolder sessionHolder) {
        final boolean useKey = conn.getKeyId() != null;
        // 使用密钥认证
        if (useKey) {
            // 加载密钥
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
        SessionStore session = sessionHolder.getSession(conn.getHostAddress(), conn.getHostPort(), conn.getUsername());
        // 使用密码认证
        if (!useKey) {
            String password = conn.getPassword();
            if (!Strings.isEmpty(password)) {
                session.password(CryptoUtils.decryptAsString(password));
            }
        }
        // 超时时间
        session.timeout(conn.getTimeout());
        return session;
    }

    /**
     * 获取错误信息
     *
     * @param e e
     * @return errorMessage
     */
    private static String getErrorMessage(Exception e) {
        if (e == null) {
            return null;
        }
        String host = CURRENT_ADDRESS.get();
        String message = e.getMessage();
        if (Strings.contains(message, Const.TIMEOUT)) {
            // 连接超时
            return Strings.format(SessionMessage.CONNECTION_TIMEOUT, host);
        } else if (Exceptions.isCausedBy(e, AuthenticationException.class)) {
            // 认证失败
            return Strings.format(SessionMessage.AUTHENTICATION_FAILURE, host);
        } else if (Exceptions.isCausedBy(e, InvalidArgumentException.class)) {
            // 参数错误
            if (Strings.isBlank(message)) {
                return Strings.format(SessionMessage.SERVER_UNREACHABLE, host);
            } else {
                return message;
            }
        } else {
            // 其他错误
            return Strings.format(SessionMessage.SERVER_UNREACHABLE, host);
        }
    }

}
