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
package org.dromara.visor.module.exec.handler.exec.command.handler;

import cn.orionsec.kit.lang.support.timeout.TimeoutChecker;
import cn.orionsec.kit.lang.support.timeout.TimeoutEndpoint;
import cn.orionsec.kit.lang.utils.ansi.AnsiAppender;
import cn.orionsec.kit.lang.utils.ansi.style.color.AnsiForeground;
import cn.orionsec.kit.lang.utils.time.Dates;
import cn.orionsec.kit.net.host.ssh.ExitCode;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.enums.BooleanBit;
import org.dromara.visor.module.exec.entity.domain.ExecLogDO;
import org.dromara.visor.module.exec.enums.ExecHostStatusEnum;

import java.util.Map;

/**
 * 命令执行器 ansi 日志输出
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/25 18:19
 */
public class ExecCommandDetailHandler extends BaseExecCommandHandler {

    public ExecCommandDetailHandler(Long id,
                                    ExecLogDO execLog,
                                    Map<String, Object> builtParams,
                                    TimeoutChecker<TimeoutEndpoint> timeoutChecker) {
        super(id, execLog, builtParams, timeoutChecker);
    }

    @Override
    protected void initLogOutputStream() throws Exception {
        super.initLogOutputStream();
        // 拼接启动日志
        AnsiAppender appender = AnsiAppender.create()
                .append(AnsiForeground.BRIGHT_GREEN, "> 准备执行命令    ")
                .append(this.getCurrentTime())
                .newLine()
                .append(AnsiForeground.BRIGHT_BLUE, "执行记录: ")
                .append(execLog.getId())
                .newLine()
                .append(AnsiForeground.BRIGHT_BLUE, "执行描述: ")
                .append(execLog.getDescription())
                .newLine()
                .append(AnsiForeground.BRIGHT_BLUE, "执行用户: ")
                .append(execLog.getUsername());
        // 非系统用户执行添加 userId
        if (Const.SYSTEM_USER_ID.equals(execLog.getUserId())) {
            appender.newLine();
        } else {
            appender.append(" (")
                    .append(execLog.getUserId())
                    .append(")")
                    .newLine();
        }
        // 执行序列
        if (execLog.getExecSeq() != null) {
            appender.append(AnsiForeground.BRIGHT_BLUE, "执行序列: ")
                    .append('#')
                    .append(execLog.getExecSeq())
                    .newLine();
        }
        appender.append(AnsiForeground.BRIGHT_BLUE, "执行主机: ")
                .append(execHostLog.getHostName())
                .append(" (")
                .append(execHostLog.getHostId())
                .append(")")
                .newLine()
                .append(AnsiForeground.BRIGHT_BLUE, "主机地址: ")
                .append(execHostLog.getHostAddress())
                .newLine()
                .append(AnsiForeground.BRIGHT_BLUE, "超时时间: ")
                .append(execLog.getTimeout())
                .newLine()
                .append(AnsiForeground.BRIGHT_BLUE, "脚本执行: ")
                .append(execLog.getScriptExec())
                .newLine()
                .newLine()
                .append(AnsiForeground.BRIGHT_GREEN, "> 执行命令        ")
                .newLine()
                .append(execHostLog.getCommand())
                .newLine()
                .newLine();
        // 非脚本执行拼接开始执行日志
        if (!BooleanBit.toBoolean(execLog.getScriptExec())) {
            appender.append(AnsiForeground.BRIGHT_GREEN, "> 开始执行命令    ")
                    .append(this.getCurrentTime())
                    .newLine();
        }
        this.appendLog(appender);
    }

    @Override
    protected void uploadScriptFile() {
        try {
            // 拼接上传日志
            AnsiAppender startAppender = AnsiAppender.create()
                    .newLine()
                    .append(AnsiForeground.BRIGHT_GREEN, "> 准备上传脚本    ")
                    .append(this.getCurrentTime())
                    .newLine()
                    .append(AnsiForeground.BRIGHT_BLUE, "文件路径: ")
                    .append(execHostLog.getScriptPath())
                    .newLine();
            this.appendLog(startAppender);
            // 上传脚本文件
            super.uploadScriptFile();
            // 拼接完成日志
            AnsiAppender finishAppender = AnsiAppender.create()
                    .append(AnsiForeground.BRIGHT_GREEN, "< 脚本上传成功    ")
                    .append(this.getCurrentTime())
                    .newLine()
                    .newLine()
                    .append(AnsiForeground.BRIGHT_GREEN, "> 开始执行脚本    ")
                    .append(this.getCurrentTime())
                    .newLine();
            this.appendLog(finishAppender);
        } catch (Exception e) {
            // 拼接失败日志
            AnsiAppender errorAppender = AnsiAppender.create()
                    .append(AnsiForeground.BRIGHT_RED, "< 脚本上传失败    ")
                    .append(this.getCurrentTime())
                    .newLine();
            this.appendLog(errorAppender);
            throw e;
        }
    }

    @Override
    protected void onFinishCallback(Exception e) {
        // 执行完成回调
        super.onFinishCallback(e);
        // 拼接日志
        AnsiAppender appender = AnsiAppender.create()
                .newLine();
        if (this.status == ExecHostStatusEnum.INTERRUPTED) {
            // 中断执行
            appender.append(AnsiForeground.BRIGHT_YELLOW, "< 命令执行中断    ")
                    .append(this.getCurrentTime())
                    .newLine();
        } else if (this.status == ExecHostStatusEnum.FAILED) {
            // 执行失败
            appender.append(AnsiForeground.BRIGHT_RED, "< 命令执行失败    ")
                    .append(this.getCurrentTime())
                    .newLine()
                    .append(AnsiForeground.BRIGHT_RED, "错误原因: ")
                    .append(this.getErrorMessage(e))
                    .newLine();
        } else if (this.status == ExecHostStatusEnum.TIMEOUT) {
            // 更新执行超时
            appender.append(AnsiForeground.BRIGHT_YELLOW, "< 命令执行超时    ")
                    .append(this.getCurrentTime())
                    .newLine();
        } else if (this.status == ExecHostStatusEnum.COMPLETED) {
            long ms = execHostLog.getFinishTime().getTime() - execHostLog.getStartTime().getTime();
            Integer exitCode = execHostLog.getExitCode();
            // 执行完成
            appender.append(AnsiForeground.BRIGHT_GREEN, "< 命令执行完成    ")
                    .append(this.getCurrentTime())
                    .newLine()
                    .append(AnsiForeground.BRIGHT_BLUE, "exit:     ");
            if (ExitCode.isSuccess(exitCode)) {
                appender.append(AnsiForeground.BRIGHT_GREEN, exitCode);
            } else {
                appender.append(AnsiForeground.BRIGHT_RED, exitCode);
            }
            appender.newLine()
                    .append(AnsiForeground.BRIGHT_BLUE, "used:     ")
                    .append(Dates.interval(ms, false, "d ", "h ", "m ", "s"))
                    .append(" (")
                    .append(ms)
                    .append(" ms)")
                    .newLine();
        }
        // 拼接日志
        this.appendLog(appender);
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    private String getCurrentTime() {
        return Const.SPACE + Dates.current();
    }

}
