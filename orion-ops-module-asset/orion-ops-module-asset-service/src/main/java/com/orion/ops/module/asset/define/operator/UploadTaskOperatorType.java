package com.orion.ops.module.asset.define.operator;

import com.orion.ops.framework.biz.operator.log.core.annotation.Module;
import com.orion.ops.framework.biz.operator.log.core.factory.InitializingOperatorTypes;
import com.orion.ops.framework.biz.operator.log.core.model.OperatorType;

import static com.orion.ops.framework.biz.operator.log.core.enums.OperatorRiskLevel.*;

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

    public static final String DELETE = "upload-task:delete";

    @Override
    public OperatorType[] types() {
        return new OperatorType[]{
                new OperatorType(M, UPLOAD, "批量上传文件"),
                new OperatorType(H, DELETE, "删除批量上传记录"),
        };
    }

}
