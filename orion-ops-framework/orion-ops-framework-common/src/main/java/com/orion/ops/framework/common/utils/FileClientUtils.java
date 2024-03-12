package com.orion.ops.framework.common.utils;

import com.orion.lang.utils.Exceptions;
import com.orion.ops.framework.common.file.FileClient;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件客户端工具
 * <p>
 * PrimaryFileClient 代理类工具
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/21 12:05
 */
public class FileClientUtils {

    private static FileClient delegate;

    private FileClientUtils() {
    }

    /**
     * 上传文件
     *
     * @param path    文件路径
     * @param content 文件内容
     * @return 路径
     * @throws Exception Exception
     */
    public static String upload(String path, byte[] content) throws Exception {
        return delegate.upload(path, content);
    }

    /**
     * 上传文件
     *
     * @param path            文件路径
     * @param content         文件内容
     * @param overrideIfExist 文件存在是否覆盖
     * @return 路径
     * @throws Exception Exception
     */
    public static String upload(String path, byte[] content, boolean overrideIfExist) throws Exception {
        return delegate.upload(path, content, overrideIfExist);
    }

    /**
     * 上传文件
     *
     * @param path 文件路径
     * @param in   in
     * @return 路径
     * @throws Exception Exception
     */
    public static String upload(String path, InputStream in) throws Exception {
        return delegate.upload(path, in);
    }

    /**
     * 上传文件
     *
     * @param path      文件路径
     * @param in        in
     * @param autoClose autoClose
     * @return 路径
     * @throws Exception Exception
     */
    public static String upload(String path, InputStream in, boolean autoClose) throws Exception {
        return delegate.upload(path, in, autoClose);
    }

    /**
     * 上传文件
     *
     * @param path            文件路径
     * @param in              in
     * @param autoClose       autoClose
     * @param overrideIfExist 文件存在是否覆盖
     * @return 路径
     * @throws Exception Exception
     */
    public static String upload(String path, InputStream in, boolean autoClose, boolean overrideIfExist) throws Exception {
        return delegate.upload(path, in, autoClose, overrideIfExist);
    }

    /**
     * 检查文件是否存在
     *
     * @param path path
     * @return 是否存在
     */
    public static boolean isExists(String path) {
        return delegate.isExists(path);
    }

    /**
     * 删除文件
     *
     * @param path 路径
     * @return 是否删除
     * @throws Exception Exception
     */
    public static boolean delete(String path) throws Exception {
        return delegate.delete(path);
    }

    /**
     * 获取文件内容
     *
     * @param path path
     * @return bytes
     * @throws Exception Exception
     */
    public static byte[] getContent(String path) throws Exception {
        return delegate.getContent(path);
    }

    /**
     * 获取文件输入流
     *
     * @param path path
     * @return stream
     * @throws Exception Exception
     */
    public static InputStream getContentInputStream(String path) throws Exception {
        return delegate.getContentInputStream(path);
    }

    /**
     * 获取文件输出流
     *
     * @param path path
     * @return stream
     * @throws Exception Exception
     */
    public static OutputStream getContentOutputStream(String path) throws Exception {
        return delegate.getContentOutputStream(path);
    }

    /**
     * 获取文件输出流
     *
     * @param path   path
     * @param append append
     * @return stream
     * @throws Exception Exception
     */
    public static OutputStream getContentOutputStream(String path, boolean append) throws Exception {
        return delegate.getContentOutputStream(path, append);
    }

    /**
     * 获取返回路径 用于客户端返回
     *
     * @param path path
     * @return returnPath
     */
    public static String getReturnPath(String path) {
        return delegate.getReturnPath(path);
    }

    /**
     * 获取实际存储路径 用于服务端的存储
     *
     * @param returnPath returnPath
     * @return absolutePath
     */
    public static String getAbsolutePath(String returnPath) {
        return delegate.getAbsolutePath(returnPath);
    }

    public static void setDelegate(FileClient delegate) {
        if (FileClientUtils.delegate != null) {
            // unmodified
            throw Exceptions.state();
        }
        FileClientUtils.delegate = delegate;
    }

}
