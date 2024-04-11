package com.orion.ops.module.asset.service.impl;

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
import com.orion.ops.framework.biz.operator.log.core.utils.OperatorLogs;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.common.constant.ErrorMessage;
import com.orion.ops.framework.common.file.FileClient;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.utils.Valid;
import com.orion.ops.framework.security.core.utils.SecurityUtils;
import com.orion.ops.module.asset.convert.ExecConvert;
import com.orion.ops.module.asset.convert.ExecHostLogConvert;
import com.orion.ops.module.asset.convert.ExecLogConvert;
import com.orion.ops.module.asset.dao.ExecHostLogDAO;
import com.orion.ops.module.asset.dao.ExecLogDAO;
import com.orion.ops.module.asset.dao.HostDAO;
import com.orion.ops.module.asset.entity.domain.ExecHostLogDO;
import com.orion.ops.module.asset.entity.domain.ExecLogDO;
import com.orion.ops.module.asset.entity.domain.HostDO;
import com.orion.ops.module.asset.entity.dto.ExecParameterSchemaDTO;
import com.orion.ops.module.asset.entity.request.exec.ExecCommandExecRequest;
import com.orion.ops.module.asset.entity.request.exec.ExecCommandRequest;
import com.orion.ops.module.asset.entity.vo.ExecHostLogVO;
import com.orion.ops.module.asset.entity.vo.ExecLogVO;
import com.orion.ops.module.asset.enums.ExecHostStatusEnum;
import com.orion.ops.module.asset.enums.ExecSourceEnum;
import com.orion.ops.module.asset.enums.ExecStatusEnum;
import com.orion.ops.module.asset.enums.HostConfigTypeEnum;
import com.orion.ops.module.asset.handler.host.exec.command.ExecTaskExecutors;
import com.orion.ops.module.asset.handler.host.exec.command.dto.ExecCommandDTO;
import com.orion.ops.module.asset.handler.host.exec.command.dto.ExecCommandHostDTO;
import com.orion.ops.module.asset.service.AssetAuthorizedDataService;
import com.orion.ops.module.asset.service.ExecCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Resource
    private FileClient logsFileClient;

    @Resource
    private ExecLogDAO execLogDAO;

    @Resource
    private ExecHostLogDAO execHostLogDAO;

    @Resource
    private HostDAO hostDAO;

    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecLogVO execCommand(ExecCommandRequest request) {
        log.info("ExecService.startExecCommand start params: {}", JSON.toJSONString(request));
        LoginUser user = Objects.requireNonNull(SecurityUtils.getLoginUser());
        Long userId = user.getId();
        String command = request.getCommand();
        List<Long> hostIdList = request.getHostIdList();
        // 检查主机权限
        List<Long> authorizedHostIdList = assetAuthorizedDataService.getUserAuthorizedHostId(userId, HostConfigTypeEnum.SSH);
        hostIdList.removeIf(s -> !authorizedHostIdList.contains(s));
        log.info("ExecService.startExecCommand host hostList: {}", hostIdList);
        Valid.notEmpty(hostIdList, ErrorMessage.CHECK_AUTHORIZED_HOST);
        // 执行命令
        ExecCommandExecRequest execRequest = ExecConvert.MAPPER.to(request);
        execRequest.setUserId(userId);
        execRequest.setUsername(user.getUsername());
        execRequest.setSource(ExecSourceEnum.BATCH.name());
        return this.execCommandWithSource(execRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecLogVO execCommandWithSource(ExecCommandExecRequest request) {
        String command = request.getCommand();
        List<Long> hostIdList = request.getHostIdList();
        List<HostDO> hosts = hostDAO.selectBatchIds(hostIdList);
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
                .status(ExecStatusEnum.WAITING.name())
                .build();
        execLogDAO.insert(execLog);
        Long execId = execLog.getId();
        // 获取内置参数
        Map<String, Object> builtinsParams = this.getBaseBuiltinsParams(execId, request);
        // 设置主机日志
        List<ExecHostLogDO> execHostLogs = hosts.stream()
                .map(s -> {
                    String parameter = JSON.toJSONString(this.getHostParams(builtinsParams, s));
                    return ExecHostLogDO.builder()
                            .logId(execId)
                            .hostId(s.getId())
                            .hostName(s.getName())
                            .hostAddress(s.getAddress())
                            .status(ExecHostStatusEnum.WAITING.name())
                            .command(FORMATTER.format(command, parameter))
                            .parameter(parameter)
                            .logPath(this.buildLogPath(execId, s.getId()))
                            .build();
                }).collect(Collectors.toList());
        execHostLogDAO.insertBatch(execHostLogs);
        // 操作日志
        OperatorLogs.add(OperatorLogs.LOG_ID, execId);
        // 开始执行
        this.startExec(execLog, execHostLogs);
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
                .command(execLog.getCommand())
                .parameterSchema(execLog.getParameterSchema())
                .hostIdList(hostIdList)
                .build();
        return this.execCommand(request);
    }

    /**
     * 开始执行命令
     *
     * @param execLog      execLog
     * @param execHostLogs hostLogs
     */
    private void startExec(ExecLogDO execLog, List<ExecHostLogDO> execHostLogs) {
        ExecCommandDTO exec = ExecCommandDTO.builder()
                .logId(execLog.getId())
                .timeout(execLog.getTimeout())
                .hosts(execHostLogs.stream()
                        .map(s -> ExecCommandHostDTO.builder()
                                .hostId(s.getHostId())
                                .hostLogId(s.getId())
                                .command(s.getCommand())
                                .timeout(execLog.getTimeout())
                                .logPath(s.getLogPath())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        ExecTaskExecutors.start(exec);
    }

    /**
     * 获取基础内置参数
     *
     * @param execId  execId
     * @param request request
     * @return params
     */
    private Map<String, Object> getBaseBuiltinsParams(Long execId, ExecCommandExecRequest request) {
        String uuid = UUIds.random();
        Date date = new Date();
        // 输入参数
        Map<String, Object> params = this.extraSchemaParams(request.getParameterSchema());
        // 添加内置参数
        params.put("userId", request.getUserId());
        params.put("username", request.getUsername());
        params.put("sourceId", request.getSourceId());
        params.put("seq", request.getExecSeq());
        params.put("execId", execId);
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
     * @return params
     */
    private Map<String, Object> getHostParams(Map<String, Object> baseParams, HostDO host) {
        String uuid = UUIds.random();
        Map<String, Object> params = Maps.newMap(baseParams);
        params.put("hostId", host.getId());
        params.put("hostName", host.getName());
        params.put("hostCode", host.getCode());
        params.put("hostAddress", host.getAddress());
        params.put("hostUuid", uuid);
        params.put("hostUuidShort", uuid.replace("-", Strings.EMPTY));
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
                        ExecParameterSchemaDTO::getValue,
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
        String logFile = "/exec/" + logId + "/" + logId + "_" + hostId + ".log";
        return logsFileClient.getReturnPath(logFile);
    }

}
