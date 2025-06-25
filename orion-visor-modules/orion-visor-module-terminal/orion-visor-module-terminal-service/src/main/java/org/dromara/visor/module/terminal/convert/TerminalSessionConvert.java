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
package org.dromara.visor.module.terminal.convert;

import org.dromara.visor.common.session.config.RdpConnectConfig;
import org.dromara.visor.common.session.config.SshConnectConfig;
import org.dromara.visor.module.terminal.handler.terminal.model.config.TerminalSessionRdpConfig;
import org.dromara.visor.module.terminal.handler.terminal.model.config.TerminalSessionSftpConfig;
import org.dromara.visor.module.terminal.handler.terminal.model.config.TerminalSessionSshConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 终端会话 内部对象转换器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Mapper
public interface TerminalSessionConvert {

    TerminalSessionConvert MAPPER = Mappers.getMapper(TerminalSessionConvert.class);

    TerminalSessionSshConfig toSsh(SshConnectConfig request);

    TerminalSessionSftpConfig toSftp(SshConnectConfig request);

    TerminalSessionRdpConfig toRdp(RdpConnectConfig domain);

}
