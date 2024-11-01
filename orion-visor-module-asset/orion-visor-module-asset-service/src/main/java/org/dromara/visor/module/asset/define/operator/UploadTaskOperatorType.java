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
package org.dromara.visor.module.asset.define.operator;

import org.dromara.visor.framework.biz.operator.log.core.annotation.Module;
import org.dromara.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel;
import org.dromara.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import org.dromara.visor.framework.biz.operator.log.core.model.OperatorType;

/**
 * 上传任务 操作日志类型
 *
 * @author Jiahang Li
 * @version 1.0.7
 * @since 2024-5-7 22:15
 */
@Module("asset:upload-task")
public class UploadTaskOperatorType extends InitializingOperatorTypes {

    public static final String UPLOAD = "upload-task:upload";

    public static final String CANCEL = "upload-task:cancel";

    public static final String DELETE = "upload-task:delete";

    public static final String CLEAR = "upload-task:clear";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(OperatorRiskLevel.M, UPLOAD, "批量上传文件 <sb>${count}</sb> 个 (${name})"),
                new OperatorType(OperatorRiskLevel.M, CANCEL, "取消上传文件 <sb>${name}</sb>"),
                new OperatorType(OperatorRiskLevel.H, DELETE, "删除上传记录 <sb>${count}</sb>条"),
                new OperatorType(OperatorRiskLevel.H, CLEAR, "清理上传记录 <sb>${count}</sb>条"),
        };
    }

}
