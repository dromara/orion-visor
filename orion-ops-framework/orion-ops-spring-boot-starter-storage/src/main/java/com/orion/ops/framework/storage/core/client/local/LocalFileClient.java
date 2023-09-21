package com.orion.ops.framework.storage.core.client.local;

import com.orion.lang.utils.io.FileWriters;
import com.orion.lang.utils.io.Files1;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.storage.core.client.AbstractFileClient;

import java.io.InputStream;

/**
 * 本地文件客户端
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/30 17:21
 */
public class LocalFileClient extends AbstractFileClient<LocalFileClientConfig> {

    public LocalFileClient(LocalFileClientConfig config) {
        super(config);
    }

    @Override
    protected String doUpload(String path, InputStream in, boolean autoClose, boolean overrideIfExist) {
        // 获取返回文件路径
        String returnPath = this.getReturnPath(path);
        // 检查文件是否存在
        if (!overrideIfExist && this.isExists(returnPath)) {
            return returnPath;
        }
        // 获取实际文件路径
        String absolutePath = this.getAbsolutePath(returnPath);
        // 上传文件
        FileWriters.writeFast(absolutePath, in, autoClose);
        return returnPath;
    }

    @Override
    protected InputStream doDownload(String path) throws Exception {
        return Files1.openInputStreamFast(this.getAbsolutePath(path));
    }

    @Override
    public boolean isExists(String path) {
        return Files1.isFile(this.getAbsolutePath(path));
    }

    @Override
    public boolean delete(String path) {
        return Files1.delete(this.getAbsolutePath(path));
    }

    @Override
    protected String getReturnPath(String path) {
        // 拼接公共路径
        return Files1.getPath(config.getBasePath() + Const.SLASH + this.getFilePath(path));
    }

    @Override
    protected String getAbsolutePath(String returnPath) {
        // 拼接存储路径
        return Files1.getPath(config.getStoragePath() + Const.SLASH + returnPath);
    }

}
