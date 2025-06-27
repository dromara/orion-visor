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
package org.dromara.visor.module.terminal.service.impl;

import cn.orionsec.kit.lang.constant.StandardContentType;
import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.define.wrapper.HttpWrapper;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.Exceptions;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.Streams;
import cn.orionsec.kit.net.host.SessionStore;
import cn.orionsec.kit.net.host.sftp.SftpExecutor;
import cn.orionsec.kit.web.servlet.web.Servlets;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.common.session.ssh.SessionStores;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.asset.api.HostConnectApi;
import org.dromara.visor.module.infra.api.OperatorLogApi;
import org.dromara.visor.module.infra.entity.dto.operator.OperatorLogQueryDTO;
import org.dromara.visor.module.terminal.convert.TerminalSftpLogConvert;
import org.dromara.visor.module.terminal.define.cache.TerminalCacheKeyDefine;
import org.dromara.visor.module.terminal.define.operator.TerminalOperatorType;
import org.dromara.visor.module.terminal.entity.dto.SftpGetContentCacheDTO;
import org.dromara.visor.module.terminal.entity.dto.SftpSetContentCacheDTO;
import org.dromara.visor.module.terminal.entity.request.terminal.TerminalSftpLogQueryRequest;
import org.dromara.visor.module.terminal.entity.vo.TerminalSftpLogVO;
import org.dromara.visor.module.terminal.handler.transfer.manager.TerminalTransferManager;
import org.dromara.visor.module.terminal.handler.transfer.session.DownloadSession;
import org.dromara.visor.module.terminal.service.TerminalSftpService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

/**
 * SFTP 操作 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/4 23:35
 */
@Slf4j
@Service
public class TerminalSftpServiceImpl implements TerminalSftpService {

    @Resource
    private OperatorLogApi operatorLogApi;

    @Resource
    private HostConnectApi hostConnectApi;

    @Resource
    private TerminalTransferManager terminalTransferManager;

    @Override
    public DataGrid<TerminalSftpLogVO> getTerminalSftpLogPage(TerminalSftpLogQueryRequest request) {
        // 查询
        OperatorLogQueryDTO query = this.buildQueryInfo(request);
        // 转换
        return operatorLogApi.getOperatorLogPage(query)
                .map(s -> {
                    JSONObject extra = JSON.parseObject(s.getExtra());
                    TerminalSftpLogVO vo = TerminalSftpLogConvert.MAPPER.to(s);
                    vo.setHostId(extra.getLong(ExtraFieldConst.HOST_ID));
                    vo.setHostName(extra.getString(ExtraFieldConst.HOST_NAME));
                    vo.setHostAddress(extra.getString(ExtraFieldConst.ADDRESS));
                    String[] paths = Optional.ofNullable(extra.getString(ExtraFieldConst.PATH))
                            .map(p -> p.split("\\|"))
                            .orElse(new String[0]);
                    vo.setPaths(paths);
                    vo.setExtra(extra);
                    return vo;
                });
    }

    @Override
    public Long getTerminalSftpLogCount(TerminalSftpLogQueryRequest request) {
        // 查询
        OperatorLogQueryDTO query = this.buildQueryInfo(request);
        // 转换
        return operatorLogApi.getOperatorLogCount(query);
    }

    @Override
    public Integer deleteTerminalSftpLog(List<Long> idList) {
        log.info("TerminalSftpService.deleteSftpLog start {}", JSON.toJSONString(idList));
        Integer effect = operatorLogApi.deleteOperatorLog(idList);
        log.info("TerminalSftpService.deleteSftpLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public void getFileContentByToken(String token, HttpServletResponse response) throws IOException {
        // 解析 token
        String key = TerminalCacheKeyDefine.TERMINAL_SFTP_GET_CONTENT.format(token);
        SftpGetContentCacheDTO cache = RedisStrings.getJson(key, TerminalCacheKeyDefine.TERMINAL_SFTP_GET_CONTENT);
        if (cache == null) {
            Servlets.writeHttpWrapper(response, HttpWrapper.error(ErrorMessage.FILE_ABSENT));
            return;
        }
        // 删除缓存
        RedisStrings.delete(key);
        // 获取文件内容
        SessionStore sessionStore = null;
        SftpExecutor executor = null;
        InputStream in = null;
        try {
            // 获取终端连接信息
            SshConnectConfig connectConfig = hostConnectApi.getSshConnectConfig(cache.getHostId(), SecurityUtils.getLoginUserId());
            sessionStore = SessionStores.openSessionStore(connectConfig);
            executor = sessionStore.getSftpExecutor(connectConfig.getFileNameCharset());
            executor.connect();
            // 读取文件
            in = executor.openInputStream(cache.getPath());
            // 设置返回
            Servlets.setContentType(response, StandardContentType.TEXT_PLAIN);
            Servlets.transfer(response, in);
        } catch (Exception e) {
            Servlets.writeHttpWrapper(response, HttpWrapper.error(ErrorMessage.FILE_READ_ERROR));
        } finally {
            Streams.close(executor);
            Streams.close(sessionStore);
            Streams.close(in);
        }
    }

    @Override
    public void setFileContentByToken(String token, MultipartFile file) {
        // 解析 token
        String key = TerminalCacheKeyDefine.TERMINAL_SFTP_SET_CONTENT.format(token);
        SftpSetContentCacheDTO cache = RedisStrings.getJson(key, TerminalCacheKeyDefine.TERMINAL_SFTP_SET_CONTENT);
        Valid.notNull(cache, ErrorMessage.FILE_ABSENT);
        // 删除缓存
        RedisStrings.delete(key);
        // 写入文件内容
        SessionStore sessionStore = null;
        SftpExecutor executor = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            // 获取终端连接信息
            SshConnectConfig connectConfig = hostConnectApi.getSshConnectConfig(cache.getHostId(), SecurityUtils.getLoginUserId());
            sessionStore = SessionStores.openSessionStore(connectConfig);
            executor = sessionStore.getSftpExecutor(connectConfig.getFileNameCharset());
            executor.connect();
            // 写入文件
            out = executor.openOutputStream(cache.getPath());
            Streams.transfer(in = file.getInputStream(), out);
        } catch (Exception e) {
            throw Exceptions.app(ErrorMessage.OPERATE_ERROR);
        } finally {
            Streams.close(executor);
            Streams.close(sessionStore);
            Streams.close(out);
            Streams.close(in);
        }
    }

    @Override
    public StreamingResponseBody downloadWithTransferToken(String channelId, String transferToken, HttpServletResponse response) {
        // 获取会话
        DownloadSession session = (DownloadSession) Optional.ofNullable(channelId)
                .map(terminalTransferManager::getHandler)
                .map(s -> s.getSessionByToken(transferToken))
                .filter(s -> s instanceof DownloadSession)
                .orElse(null);
        // 响应会话
        if (session == null) {
            Servlets.setContentType(response, StandardContentType.TEXT_HTML);
            Servlets.setCharset(response, Const.UTF_8);
            return outputStream -> outputStream.write(Strings.bytes(ErrorMessage.SESSION_ABSENT));
        }
        // 响应文件
        Servlets.setAttachmentHeader(response, Files1.getFileName(session.getPath()));
        return session;
    }

    /**
     * 构建查询对象
     *
     * @param request request
     * @return query
     */
    private OperatorLogQueryDTO buildQueryInfo(TerminalSftpLogQueryRequest request) {
        Long hostId = request.getHostId();
        String type = request.getType();
        // 构建参数
        OperatorLogQueryDTO query = OperatorLogQueryDTO.builder()
                .userId(request.getUserId())
                .result(request.getResult())
                .startTimeStart(Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .startTimeEnd(Arrays1.getIfPresent(request.getStartTimeRange(), 1))
                .build();
        query.setPage(request.getPage());
        query.setLimit(request.getLimit());
        query.setOrder(request.getOrder());
        if (Strings.isBlank(type)) {
            // 查询全部 SFTP 类型
            query.setTypeList(TerminalOperatorType.SFTP_TYPES);
        } else {
            query.setType(type);
        }
        // 模糊查询
        if (hostId != null) {
            query.setExtra("\"hostId\": " + hostId + ",");
        }
        return query;
    }

}
