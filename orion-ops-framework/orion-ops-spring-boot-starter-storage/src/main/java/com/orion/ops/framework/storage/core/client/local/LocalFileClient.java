package com.orion.ops.framework.storage.core.client.local;

import com.orion.lang.utils.io.FileWriters;
import com.orion.lang.utils.io.Files1;
import com.orion.ops.framework.common.constant.Const;
import com.orion.ops.framework.storage.core.client.AbstractFileClient;

import java.io.InputStream;
import java.io.OutputStream;

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
    public InputStream getContentInputStream(String path) throws Exception {
        return Files1.openInputStreamFast(this.getAbsolutePath(path));
    }

    @Override
    public OutputStream getContentOutputStream(String path, boolean append) throws Exception {
        return Files1.openOutputStreamFast(this.getAbsolutePath(path), append);
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
    public boolean isExists(String path) {
        return Files1.isFile(this.getAbsolutePath(path));
    }

    @Override
    public boolean delete(String path) {
        return Files1.delete(this.getAbsolutePath(path));
    }

    @Override
    public String getReturnPath(String path) {
        // 拼接公共路径
        return Files1.getPath(config.getBasePath() + Const.SLASH + this.getFilePath(path));
    }

    @Override
    public String getAbsolutePath(String returnPath) {
        // 拼接存储路径
        return Files1.getPath(config.getStoragePath() + Const.SLASH + returnPath);
    }

}
