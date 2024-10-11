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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orion.lang.constant.StandardContentType;
import com.orion.lang.define.wrapper.DataGrid;
import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.lang.utils.Arrays1;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.Valid;
import com.orion.lang.utils.io.Files1;
import com.orion.lang.utils.io.Streams;
import com.orion.net.host.SessionStore;
import com.orion.net.host.sftp.SftpExecutor;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.constant.ExtraFieldConst;
import com.orion.visor.framework.redis.core.utils.RedisStrings;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.module.asset.convert.HostSftpLogConvert;
import com.orion.visor.module.asset.define.cache.HostTerminalCacheKeyDefine;
import com.orion.visor.module.asset.define.operator.HostTerminalOperatorType;
import com.orion.visor.module.asset.entity.dto.HostTerminalConnectDTO;
import com.orion.visor.module.asset.entity.dto.SftpGetContentCacheDTO;
import com.orion.visor.module.asset.entity.dto.SftpSetContentCacheDTO;
import com.orion.visor.module.asset.entity.request.host.HostSftpLogQueryRequest;
import com.orion.visor.module.asset.entity.vo.HostSftpLogVO;
import com.orion.visor.module.asset.handler.host.jsch.SessionStores;
import com.orion.visor.module.asset.handler.host.transfer.manager.HostTransferManager;
import com.orion.visor.module.asset.handler.host.transfer.session.DownloadSession;
import com.orion.visor.module.asset.service.HostSftpService;
import com.orion.visor.module.asset.service.HostTerminalService;
import com.orion.visor.module.infra.api.OperatorLogApi;
import com.orion.visor.module.infra.entity.dto.operator.OperatorLogQueryDTO;
import com.orion.web.servlet.web.Servlets;
import lombok.extern.slf4j.Slf4j;
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
public class HostSftpServiceImpl implements HostSftpService {

    @Resource
    private OperatorLogApi operatorLogApi;

    @Resource
    private HostTransferManager hostTransferManager;

    @Resource
    private HostTerminalService hostTerminalService;

    @Override
    public DataGrid<HostSftpLogVO> getHostSftpLogPage(HostSftpLogQueryRequest request) {
        // 查询
        OperatorLogQueryDTO query = this.buildQueryInfo(request);
        // 转换
        return operatorLogApi.getOperatorLogPage(query)
                .map(s -> {
                    JSONObject extra = JSON.parseObject(s.getExtra());
                    HostSftpLogVO vo = HostSftpLogConvert.MAPPER.to(s);
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
    public Integer deleteHostSftpLog(List<Long> idList) {
        log.info("HostSftpLogService.deleteSftpLog start {}", JSON.toJSONString(idList));
        Integer effect = operatorLogApi.deleteOperatorLog(idList);
        log.info("HostSftpLogService.deleteSftpLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public void getFileContentByToken(String token, HttpServletResponse response) throws IOException {
        // 解析 token
        String key = HostTerminalCacheKeyDefine.SFTP_GET_CONTENT.format(token);
        SftpGetContentCacheDTO cache = RedisStrings.getJson(key, HostTerminalCacheKeyDefine.SFTP_GET_CONTENT);
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
            HostTerminalConnectDTO connectInfo = hostTerminalService.getTerminalConnectInfo(SecurityUtils.getLoginUserId(), cache.getHostId());
            sessionStore = SessionStores.openSessionStore(connectInfo);
            executor = sessionStore.getSftpExecutor(connectInfo.getFileNameCharset());
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
        String key = HostTerminalCacheKeyDefine.SFTP_SET_CONTENT.format(token);
        SftpSetContentCacheDTO cache = RedisStrings.getJson(key, HostTerminalCacheKeyDefine.SFTP_SET_CONTENT);
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
            HostTerminalConnectDTO connectInfo = hostTerminalService.getTerminalConnectInfo(SecurityUtils.getLoginUserId(), cache.getHostId());
            sessionStore = SessionStores.openSessionStore(connectInfo);
            executor = sessionStore.getSftpExecutor(connectInfo.getFileNameCharset());
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
                .map(hostTransferManager::getHandler)
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
    private OperatorLogQueryDTO buildQueryInfo(HostSftpLogQueryRequest request) {
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
        if (Strings.isBlank(type)) {
            // 查询全部 SFTP 类型
            query.setTypeList(HostTerminalOperatorType.SFTP_TYPES);
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
