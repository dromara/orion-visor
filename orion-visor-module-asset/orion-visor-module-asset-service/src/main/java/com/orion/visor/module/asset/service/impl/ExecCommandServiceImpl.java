package com.orion.visor.module.asset.service.impl;

import com.alibaba.fastjson.JSON;
import com.orion.lang.function.Functions;
import com.orion.lang.id.UUIds;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.collect.Maps;
import com.orion.lang.utils.json.matcher.NoMatchStrategy;
import com.orion.lang.utils.json.matcher.ReplacementFormatter;
import com.orion.lang.utils.json.matcher.ReplacementFormatters;
import com.orion.lang.utils.time.Dates;
import com.orion.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.visor.framework.common.annotation.Keep;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.constant.PathConst;
import com.orion.visor.framework.common.file.FileClient;
import com.orion.visor.framework.common.security.LoginUser;
import com.orion.visor.framework.common.utils.PathUtils;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.framework.security.core.utils.SecurityUtils;
import com.orion.visor.module.asset.convert.ExecConvert;
import com.orion.visor.module.asset.convert.ExecHostLogConvert;
import com.orion.visor.module.asset.convert.ExecLogConvert;
import com.orion.visor.module.asset.dao.ExecHostLogDAO;
import com.orion.visor.module.asset.dao.ExecLogDAO;
import com.orion.visor.module.asset.dao.HostDAO;
import com.orion.visor.module.asset.entity.domain.ExecHostLogDO;
import com.orion.visor.module.asset.entity.domain.ExecLogDO;
import com.orion.visor.module.asset.entity.domain.HostDO;
import com.orion.visor.module.asset.entity.dto.ExecCommandExecDTO;
import com.orion.visor.module.asset.entity.dto.ExecParameterSchemaDTO;
import com.orion.visor.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.visor.module.asset.entity.vo.ExecHostLogVO;
import com.orion.visor.module.asset.entity.vo.ExecLogVO;
import com.orion.visor.module.asset.enums.*;
import com.orion.visor.module.asset.handler.host.config.model.HostSshConfigModel;
import com.orion.visor.module.asset.handler.host.exec.command.ExecTaskExecutors;
import com.orion.visor.module.asset.handler.host.exec.command.model.ExecCommandDTO;
import com.orion.visor.module.asset.handler.host.exec.command.model.ExecCommandHostDTO;
import com.orion.visor.module.asset.service.AssetAuthorizedDataService;
import com.orion.visor.module.asset.service.ExecCommandService;
import com.orion.visor.module.asset.service.HostConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 批量执行服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 12:03
 */
@Slf4j
@Service
public class ExecCommandServiceImpl implements ExecCommandService {

    private static final ReplacementFormatter FORMATTER = ReplacementFormatters.create("@{{ ", " }}")
            .noMatchStrategy(NoMatchStrategy.KEEP);

    private static final int DESC_OMIT = 60;

    @Keep
    @Resource
    private FileClient logsFileClient;

    @Resource
    private ExecLogDAO execLogDAO;

    @Resource
    private ExecHostLogDAO execHostLogDAO;

    @Resource
    private HostDAO hostDAO;

    @Resource
    private HostConfigService hostConfigService;

    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecLogVO execCommand(ExecCommandRequest request) {
        log.info("ExecService.startExecCommand start params: {}", JSON.toJSONString(request));
        Valid.valid(ScriptExecEnum::of, request.getScriptExec());
        LoginUser user = Valid.notNull(SecurityUtils.getLoginUser());
        Long userId = user.getId();
        List<Long> hostIdList = request.getHostIdList();
        // 检查主机权限
        List<Long> authorizedHostIdList = assetAuthorizedDataService.getUserAuthorizedHostIdWithEnabledConfig(userId, HostConfigTypeEnum.SSH);
        hostIdList.removeIf(s -> !authorizedHostIdList.contains(s));
        log.info("ExecService.startExecCommand host hostList: {}", hostIdList);
        Valid.notEmpty(hostIdList, ErrorMessage.CHECK_AUTHORIZED_HOST);
        // 执行命令
        ExecCommandExecDTO execRequest = ExecConvert.MAPPER.to(request);
        execRequest.setUserId(userId);
        execRequest.setUsername(user.getUsername());
        execRequest.setSource(ExecSourceEnum.BATCH.name());
        return this.execCommandWithSource(execRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecLogVO execCommandWithSource(ExecCommandExecDTO request) {
        String command = request.getCommand();
        List<Long> hostIdList = request.getHostIdList();
        // 查询主机信息
        List<HostDO> hosts = hostDAO.selectBatchIds(hostIdList);
        // 查询主机配置
        Map<Long, HostSshConfigModel> hostConfigMap = hostConfigService.getHostConfigMap(hostIdList, HostConfigTypeEnum.SSH);
        // 插入日志
        ExecLogDO execLog = ExecLogDO.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
                .source(request.getSource())
                .sourceId(request.getSourceId())
                .execSeq(request.getExecSeq())
                .description(Strings.ifBlank(request.getDescription(), () -> {
                    if (command.length() < DESC_OMIT + 3) {
                        return command;
                    } else {
                        return command.substring(0, DESC_OMIT) + Const.OMIT;
                    }
                }))
                .command(command)
                .parameterSchema(request.getParameterSchema())
                .timeout(request.getTimeout())
                .scriptExec(request.getScriptExec())
                .status(ExecStatusEnum.WAITING.name())
                .build();
        execLogDAO.insert(execLog);
        Long execId = execLog.getId();
        // 获取内置参数
        Map<String, Object> builtinsParams = this.getBaseBuiltinsParams(execId, request);
        // 设置主机日志
        List<ExecHostLogDO> execHostLogs = hosts.stream()
                .map(s -> this.convertExecHostLog(s, execLog, hostConfigMap.get(s.getId()), builtinsParams))
                .collect(Collectors.toList());
        execHostLogDAO.insertBatch(execHostLogs);
        // 操作日志
        OperatorLogs.add(OperatorLogs.LOG_ID, execId);
        // 开始执行
        this.startExec(execLog, execHostLogs, hostConfigMap);
        // 返回
        ExecLogVO result = ExecLogConvert.MAPPER.to(execLog);
        List<ExecHostLogVO> resultHosts = ExecHostLogConvert.MAPPER.to(execHostLogs);
        result.setHosts(resultHosts);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecLogVO reExecCommand(Long logId) {
        log.info("ExecService.reExecCommand start logId: {}", logId);
        // 获取执行记录
        ExecLogDO execLog = execLogDAO.selectByIdSource(logId, ExecSourceEnum.BATCH.name());
        Valid.notNull(execLog, ErrorMessage.DATA_ABSENT);
        // 获取执行主机
        List<ExecHostLogDO> hostLogs = execHostLogDAO.selectByLogId(logId);
        Valid.notEmpty(hostLogs, ErrorMessage.DATA_ABSENT);
        List<Long> hostIdList = hostLogs.stream()
                .map(ExecHostLogDO::getHostId)
                .collect(Collectors.toList());
        // 调用执行方法
        ExecCommandRequest request = ExecCommandRequest.builder()
                .description(execLog.getDescription())
                .timeout(execLog.getTimeout())
                .scriptExec(execLog.getScriptExec())
                .command(execLog.getCommand())
                .parameterSchema(execLog.getParameterSchema())
                .hostIdList(hostIdList)
                .build();
        return this.execCommand(request);
    }

    /**
     * 开始执行命令
     *
     * @param execLog       execLog
     * @param execHostLogs  hostLogs
     * @param hostConfigMap hostConfigMap
     */
    private void startExec(ExecLogDO execLog,
                           List<ExecHostLogDO> execHostLogs,
                           Map<Long, HostSshConfigModel> hostConfigMap) {
        // 执行主机
        List<ExecCommandHostDTO> hosts = execHostLogs.stream()
                .map(s -> {
                    HostSshConfigModel config = hostConfigMap.get(s.getHostId());
                    return ExecCommandHostDTO.builder()
                            .hostId(s.getHostId())
                            .hostLogId(s.getId())
                            .hostName(s.getHostName())
                            .hostAddress(s.getHostAddress())
                            .command(s.getCommand())
                            .logPath(s.getLogPath())
                            .scriptPath(s.getScriptPath())
                            .username(config.getUsername())
                            .charset(config.getCharset())
                            .fileNameCharset(config.getFileNameCharset())
                            .fileContentCharset(config.getFileContentCharset())
                            .build();
                }).collect(Collectors.toList());
        // 执行信息
        ExecCommandDTO exec = ExecCommandDTO.builder()
                .logId(execLog.getId())
                .userId(execLog.getUserId())
                .username(execLog.getUsername())
                .description(execLog.getDescription())
                .execSeq(execLog.getExecSeq())
                .timeout(execLog.getTimeout())
                .scriptExec(ScriptExecEnum.isEnabled(execLog.getScriptExec()))
                .hosts(hosts)
                .build();
        // 开始执行
        ExecTaskExecutors.start(exec);
    }

    /**
     * 转换为 execHostLog
     *
     * @param host           host
     * @param execLog        execLog
     * @param config         config
     * @param builtinsParams builtinsParams
     * @return execHostLog
     */
    private ExecHostLogDO convertExecHostLog(HostDO host,
                                             ExecLogDO execLog,
                                             HostSshConfigModel config,
                                             Map<String, Object> builtinsParams) {
        Long execId = execLog.getId();
        Long hostId = host.getId();
        // 脚本路径
        String scriptPath = null;
        if (ScriptExecEnum.isEnabled(execLog.getScriptExec())) {
            scriptPath = this.buildScriptPath(config.getUsername(), config.getOsType(), execId, hostId);
        }
        // 获取参数
        String parameter = JSON.toJSONString(this.getHostParams(builtinsParams, host, config, scriptPath));
        return ExecHostLogDO.builder()
                .logId(execId)
                .hostId(hostId)
                .hostName(host.getName())
                .hostAddress(host.getAddress())
                .status(ExecHostStatusEnum.WAITING.name())
                .command(FORMATTER.format(execLog.getCommand(), parameter))
                .parameter(parameter)
                .logPath(this.buildLogPath(execId, hostId))
                .scriptPath(scriptPath)
                .build();
    }

    /**
     * 获取基础内置参数
     *
     * @param execId  execId
     * @param request request
     * @return params
     */
    private Map<String, Object> getBaseBuiltinsParams(Long execId, ExecCommandExecDTO request) {
        String uuid = UUIds.random();
        Date date = new Date();
        // 输入参数
        Map<String, Object> params = this.extraSchemaParams(request.getParameterSchema());
        // 添加内置参数
        params.put("userId", request.getUserId());
        params.put("username", request.getUsername());
        params.put("source", request.getSource());
        params.put("sourceId", request.getSourceId());
        params.put("seq", request.getExecSeq());
        params.put("execId", execId);
        params.put("scriptExec", request.getScriptExec());
        params.put("uuid", uuid);
        params.put("uuidShort", uuid.replace("-", Strings.EMPTY));
        params.put("timestampMillis", date.getTime());
        params.put("timestamp", date.getTime() / Dates.SECOND_STAMP);
        params.put("date", Dates.format(date, Dates.YMD));
        params.put("datetime", Dates.format(date, Dates.YMD_HMS));
        return params;
    }

    /**
     * 获取主机参数
     *
     * @param baseParams baseParams
     * @param host       host
     * @param config     config
     * @param scriptPath scriptPath
     * @return params
     */
    private Map<String, Object> getHostParams(Map<String, Object> baseParams,
                                              HostDO host,
                                              HostSshConfigModel config,
                                              String scriptPath) {
        String uuid = UUIds.random();
        Map<String, Object> params = Maps.newMap(baseParams);
        params.put("hostId", host.getId());
        params.put("hostName", host.getName());
        params.put("hostCode", host.getCode());
        params.put("hostAddress", host.getAddress());
        params.put("hostUuid", uuid);
        params.put("hostUuidShort", uuid.replace("-", Strings.EMPTY));
        params.put("hostUsername", config.getUsername());
        params.put("osType", config.getOsType());
        params.put("port", config.getPort());
        params.put("charset", config.getCharset());
        params.put("scriptPath", scriptPath);
        return params;
    }

    /**
     * 提取参数
     *
     * @param parameterSchema parameterSchema
     * @return params
     */
    private Map<String, Object> extraSchemaParams(String parameterSchema) {
        List<ExecParameterSchemaDTO> schemaList = JSON.parseArray(parameterSchema, ExecParameterSchemaDTO.class);
        if (Lists.isEmpty(schemaList)) {
            return Maps.newMap();
        }
        // 解析参数
        return schemaList.stream()
                .collect(Collectors.toMap(ExecParameterSchemaDTO::getName,
                        s -> {
                            Object value = s.getValue();
                            if (value == null) {
                                value = s.getDefaultValue();
                            }
                            if (value == null) {
                                value = Const.EMPTY;
                            }
                            return value;
                        },
                        Functions.right()));
    }

    /**
     * 构建日志路径
     *
     * @param logId  logId
     * @param hostId hostId
     * @return logPath
     */
    private String buildLogPath(Long logId, Long hostId) {
        String logFile = "/" + PathConst.EXEC + "/" + logId + "/" + logId + "_" + hostId + ".log";
        return logsFileClient.getReturnPath(logFile);
    }

    /**
     * 侯建脚本路径
     *
     * @param username username
     * @param osType   osType
     * @param logId    logId
     * @param hostId   hostId
     * @return scriptPath
     */
    private String buildScriptPath(String username, String osType, Long logId, Long hostId) {
        HostSshOsTypeEnum os = HostSshOsTypeEnum.of(osType);
        String name = PathConst.EXEC
                + "_" + logId
                + "_" + hostId
                + os.getScriptSuffix();
        return PathUtils.buildAppPath(HostSshOsTypeEnum.WINDOWS.equals(os), username, PathConst.SCRIPT, name);
    }

}
