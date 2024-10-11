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
package com.orion.visor.framework.websocket.core.session;

import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * web socket 同步会话
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/5/20 10:12
 */
public class WebSocketSyncSession implements WebSocketSession {

    private final WebSocketSession delegate;

    public WebSocketSyncSession(WebSocketSession delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getId() {
        return this.delegate.getId();
    }

    @Override
    public URI getUri() {
        return this.delegate.getUri();
    }

    @Override
    public HttpHeaders getHandshakeHeaders() {
        return this.delegate.getHandshakeHeaders();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.delegate.getAttributes();
    }

    @Override
    public Principal getPrincipal() {
        return this.delegate.getPrincipal();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return this.delegate.getLocalAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return this.delegate.getRemoteAddress();
    }

    @Override
    public String getAcceptedProtocol() {
        return this.delegate.getAcceptedProtocol();
    }

    @Override
    public void setTextMessageSizeLimit(int messageSizeLimit) {
        this.delegate.setTextMessageSizeLimit(messageSizeLimit);
    }

    @Override
    public int getTextMessageSizeLimit() {
        return this.delegate.getTextMessageSizeLimit();
    }

    @Override
    public void setBinaryMessageSizeLimit(int messageSizeLimit) {
        this.delegate.setBinaryMessageSizeLimit(messageSizeLimit);
    }

    @Override
    public int getBinaryMessageSizeLimit() {
        return this.delegate.getBinaryMessageSizeLimit();
    }

    @Override
    public List<WebSocketExtension> getExtensions() {
        return this.delegate.getExtensions();
    }

    @Override
    public void sendMessage(WebSocketMessage<?> message) throws IOException {
        synchronized (this.delegate) {
            this.delegate.sendMessage(message);
        }
    }

    @Override
    public boolean isOpen() {
        return this.delegate.isOpen();
    }

    @Override
    public void close() throws IOException {
        this.delegate.close();
    }

    @Override
    public void close(CloseStatus status) throws IOException {
        this.delegate.close(status);
    }

}
