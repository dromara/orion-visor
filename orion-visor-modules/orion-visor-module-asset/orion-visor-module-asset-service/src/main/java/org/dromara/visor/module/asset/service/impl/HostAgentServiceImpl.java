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
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.lang.utils.collect.Maps;
import cn.orionsec.kit.lang.utils.crypto.enums.HashDigest;
import cn.orionsec.kit.lang.utils.io.FileReaders;
import cn.orionsec.kit.lang.utils.io.Files1;
import cn.orionsec.kit.lang.utils.io.compress.CompressTypeEnum;
import cn.orionsec.kit.lang.utils.io.compress.FileDecompressor;
import cn.orionsec.kit.lang.utils.net.IPs;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.constant.ExtraFieldConst;
import org.dromara.visor.common.constant.FileConst;
import org.dromara.visor.common.utils.PathUtils;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.redis.core.utils.RedisStrings;
import org.dromara.visor.module.asset.convert.HostConvert;
import org.dromara.visor.module.asset.dao.HostAgentLogDAO;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.define.cache.HostCacheKeyDefine;
import org.dromara.visor.module.asset.entity.domain.HostAgentLogDO;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.request.host.HostAgentInstallRequest;
import org.dromara.visor.module.asset.entity.vo.HostAgentStatusVO;
import org.dromara.visor.module.asset.enums.*;
import org.dromara.visor.module.asset.handler.agent.intstall.AgentInstaller;
import org.dromara.visor.module.asset.handler.agent.model.AgentInstallParams;
import org.dromara.visor.module.asset.service.HostAgentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 主机探针 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2025/8/28 10:10
 */
@Slf4j
@Service
public class HostAgentServiceImpl implements HostAgentService {

    private String localVersion;

    @Value("${orion.api.expose.token}")
    private String exposeToken;

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostAgentLogDAO hostAgentLogDAO;

    /**
     * 读取本地探针版本
     */
    @PostConstruct
    public void readLocalAgentVersion() {
        log.info("HostAgentService-readLocalAgentVersion start");
        // 文件路径
        String path = PathUtils.getOrionPath(FileConst.INSTANCE_AGENT_RELEASE + Const.SLASH + FileConst.VERSION);
        log.info("HostAgentService-readLocalAgentVersion path: {}", path);
        try {
            if (!Files1.isFile(path)) {
                log.error("HostAgentService-readLocalAgentVersion not file");
                return;
            }
            // 读取文件内容
            byte[] bytes = FileReaders.readAllBytesFast(path);
            this.localVersion = new String(bytes).trim();
            log.info("HostAgentService-readLocalAgentVersion version: {}", localVersion);
        } catch (Exception e) {
            log.error("HostAgentService-readLocalAgentVersion error", e);
        }
    }

    @Override
    public List<HostAgentStatusVO> getAgentStatus(List<Long> idList) {
        if (Lists.isEmpty(idList)) {
            return Lists.empty();
        }
        return hostDAO.of()
                .createWrapper()
                .select(HostDO::getId,
                        HostDO::getAgentVersion,
                        HostDO::getAgentInstallStatus,
                        HostDO::getAgentOnlineStatus)
                .in(HostDO::getId, idList)
                .then()
                .stream()
                .map(HostConvert.MAPPER::toAgentStatus)
                .peek(s -> s.setLatestVersion(localVersion))
                .collect(Collectors.toList());
    }

    @Override
    public void installAgent(HostAgentInstallRequest request) {
        // 查询主机信息
        List<Long> idList = request.getIdList();
        List<HostDO> hosts = hostDAO.selectBatchIds(idList);
        Valid.eq(hosts.size(), idList.size(), ErrorMessage.HOST_ABSENT);

        // 检查并创建安装任务参数
        List<AgentInstallParams> installTaskParams = this.createInstallTaskParams(hosts);

        // 查询当前状态
        boolean hasRunning = hostAgentLogDAO.of()
                .createWrapper()
                .in(HostAgentLogDO::getHostId, idList)
                .eq(HostAgentLogDO::getType, AgentLogTypeEnum.INSTALL.name())
                .in(HostAgentLogDO::getStatus, AgentLogStatusEnum.WAIT.name(), AgentLogStatusEnum.RUNNING.name())
                .then()
                .present();
        Valid.isFalse(hasRunning, ErrorMessage.ILLEGAL_STATUS);

        // 创建日志记录
        List<HostAgentLogDO> agentLogs = hosts.stream()
                .map(s -> HostAgentLogDO.builder()
                        .hostId(s.getId())
                        .agentKey(s.getAgentKey())
                        .type(AgentLogTypeEnum.INSTALL.name())
                        .status(AgentLogStatusEnum.WAIT.name())
                        .build())
                .collect(Collectors.toList());
        hostAgentLogDAO.insertBatch(agentLogs);

        // 设置缓存
        for (HostAgentLogDO agentLog : agentLogs) {
            String key = HostCacheKeyDefine.HOST_INSTALL_LOG.format(agentLog.getHostId());
            RedisStrings.set(key, HostCacheKeyDefine.HOST_INSTALL_LOG, agentLog.getId());
        }

        // 获取替换变量
        Map<String, String> replaceVars = this.getReplaceVars();

        // 提交任务
        for (int i = 0; i < installTaskParams.size(); i++) {
            AgentInstallParams params = installTaskParams.get(i);
            HostAgentLogDO agentLog = agentLogs.get(i);
            params.setLogId(agentLog.getId());
            params.setReplaceVars(this.getHostReplaceVars(replaceVars, params));
            // 执行任务
            AgentInstaller.start(params);
        }
    }

    @Override
    public void uploadAgentRelease(MultipartFile file) {
        // 检查文件名
        String fileName = Optional.of(file)
                .map(MultipartFile::getOriginalFilename)
                .map(String::toLowerCase)
                .orElse(Const.EMPTY);
        Valid.notBlank(fileName, ErrorMessage.FILE_EXTENSION_TYPE);
        Valid.isTrue(fileName.endsWith(Const.SUFFIX_TAR_GZ), ErrorMessage.FILE_EXTENSION_TYPE);
        // 保存文件
        String releaseDir = PathUtils.getOrionPath(FileConst.INSTANCE_AGENT_RELEASE);
        String releaseTempDir = PathUtils.getOrionPath(FileConst.INSTANCE_AGENT_RELEASE_TEMP);
        File releaseTempFile = new File(releaseTempDir + Const.SLASH + FileConst.INSTANCE_AGENT_RELEASE_TAR_GZ);
        log.info("HostAgentService.installAgent start releaseTempDir: {}, releaseTempFile: {}", releaseTempDir, releaseTempFile.getAbsolutePath());
        try {
            // 创建目录
            Files1.mkdirs(releaseTempFile.getParentFile());
            // 传输文件
            file.transferTo(releaseTempFile);
        } catch (IOException e) {
            throw Exceptions.app(ErrorMessage.FILE_UPLOAD_ERROR, e);
        }
        // 计算签名
        try {
            String sign = Files1.sign(releaseTempFile, HashDigest.SHA256);
            OperatorLogs.add(ExtraFieldConst.SIGN, sign);
            OperatorLogs.add(ExtraFieldConst.SIGN_SHORT, sign.substring(0, 8));
            log.error("HostAgentService.installAgent calc sha256 sign: {}", sign);
        } catch (Exception e) {
            log.error("HostAgentService.installAgent calc sha256 error", e);
            throw Exceptions.app(ErrorMessage.CALC_SIGN_FAILED, e);
        }
        // 解压缩文件
        try {
            FileDecompressor decompressor = CompressTypeEnum.TAR_GZ.decompressor().get();
            decompressor.setDecompressFile(releaseTempFile);
            decompressor.setDecompressTargetPath(releaseTempDir);
            decompressor.decompress();
            log.info("HostAgentService.installAgent decompress success");
        } catch (Exception e) {
            log.error("HostAgentService.installAgent decompress error", e);
            throw Exceptions.app(ErrorMessage.CALC_SIGN_FAILED, e);
        }
        // 获取全部文件
        List<File> decompressFiles = Files1.listFiles(releaseTempDir);
        log.info("HostAgentService.installAgent decompressFiles: {}", Lists.map(decompressFiles, File::getName));
        // 检查版本文件
        String versionFile = releaseTempDir + Const.SLASH + FileConst.VERSION;
        Valid.isTrue(Files1.isFile(versionFile), ErrorMessage.DECOMPRESS_FILE_ABSENT + Const.SPACE + FileConst.VERSION);
        // 移动文件
        for (File decompressFile : decompressFiles) {
            String releaseFile = releaseDir + Const.SLASH + decompressFile.getName();
            // 删除原始文件
            Files1.deleteFile(releaseFile);
            // 复制文件
            Files1.copy(decompressFile.getAbsolutePath(), releaseFile);
            log.info("HostAgentService.installAgent move: {}", releaseFile);
        }
        // 删除临时文件夹
        Files1.delete(releaseTempDir);
        // 重新加载版本
        this.readLocalAgentVersion();
    }

    @Override
    public String getAgentVersion() {
        return localVersion;
    }

    /**
     * 检查并创建安装任务参数
     *
     * @param hosts hosts
     * @return taskParams
     */
    private List<AgentInstallParams> createInstallTaskParams(List<HostDO> hosts) {
        List<AgentInstallParams> taskParams = new ArrayList<>();
        // 待检查的文件列表
        Set<String> checkFileList = new HashSet<>();
        // 任务参数
        for (HostDO host : hosts) {
            // 是否启用
            Valid.eq(HostStatusEnum.ENABLED.name(), host.getStatus(), ErrorMessage.HOST_NOT_ENABLED, host.getName());
            // 是否支持 ssh
            boolean supportSsh = HostTypeEnum.SSH.contains(host.getTypes());
            Valid.isTrue(supportSsh, ErrorMessage.PLEASE_CHECK_HOST_SSH, host.getName());
            // 文件名称
            HostOsTypeEnum os = HostOsTypeEnum.of(host.getOsType());
            String agentFileName = Strings.format(FileConst.INSTANCE_AGENT_FILE_FORMAT,
                    os.name().toLowerCase(),
                    host.getArchType().toLowerCase(),
                    os.getBinarySuffix());
            // 安装参数
            AgentInstallParams params = AgentInstallParams.builder()
                    .hostId(host.getId())
                    .osType(host.getOsType())
                    .agentKey(host.getAgentKey())
                    .agentFilePath(PathUtils.getOrionPath(FileConst.INSTANCE_AGENT_RELEASE + Const.SLASH + agentFileName))
                    .configFilePath(PathUtils.getOrionPath(FileConst.INSTANCE_AGENT_RELEASE + Const.SLASH + FileConst.CONFIG_YAML))
                    .startScriptPath(PathUtils.getOrionPath(FileConst.INSTANCE_AGENT_RELEASE + Const.SLASH + Const.START + os.getScriptSuffix()))
                    .build();
            taskParams.add(params);
            // 添加待检查文件
            checkFileList.add(params.getAgentFilePath());
            checkFileList.add(params.getStartScriptPath());
            checkFileList.add(params.getConfigFilePath());
        }

        // 检查文件是否存在
        for (String file : checkFileList) {
            Valid.isTrue(Files1.isFile(file), ErrorMessage.FILE_ABSENT + Const.SPACE + file);
        }
        return taskParams;
    }

    /**
     * 获取替换变量
     *
     * @return vars
     */
    private Map<String, String> getReplaceVars() {
        Map<String, String> map = new HashMap<>();
        map.put("SERVER_HOST", IPs.IP);
        map.put("SERVER_TOKEN", exposeToken);
        return map;
    }

    /**
     * 获取主机替换变量
     *
     * @param vars vars
     * @return vars
     */
    private Map<String, String> getHostReplaceVars(Map<String, String> vars, AgentInstallParams params) {
        vars = Maps.newMap(vars);
        vars.put("AGENT_KEY", params.getAgentKey());
        return vars;
    }

}
