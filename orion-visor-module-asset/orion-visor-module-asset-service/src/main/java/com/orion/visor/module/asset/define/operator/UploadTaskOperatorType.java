package com.orion.visor.module.asset.define.operator;

import com.orion.visor.framework.biz.operator.log.core.annotation.Module;
import com.orion.visor.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.visor.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.H;
import static com.orion.visor.framework.biz.operator.log.core.enums.OperatorRiskLevel.M;

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
                new OperatorType(M, UPLOAD, "批量上传文件 <sb>${count}</sb>个 (${name})"),
                new OperatorType(M, CANCEL, "取消上传文件 <sb>${name}</sb>"),
                new OperatorType(H, DELETE, "删除上传记录 <sb>${count}</sb>条"),
                new OperatorType(H, CLEAR, "清理上传记录 <sb>${count}</sb>条"),
        };
    }

}
