package com.orion.ops.framework.web.core.handler;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.lang.exception.*;
import com.orion.lang.exception.argument.CodeArgumentException;
import com.orion.lang.exception.argument.HttpWrapperException;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.exception.argument.RpcWrapperException;
import com.orion.lang.utils.Exceptions;
import com.orion.ops.framework.common.enums.ExceptionMessageConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.sql.SQLException;

/**
 * 全局异常处理器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/6/15 17:19
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public HttpWrapper<?> normalExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("normalExceptionHandler url: {}, 抛出异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.EXCEPTION_MESSAGE).data(ex.getMessage());
    }

    @ExceptionHandler(value = ApplicationException.class)
    public HttpWrapper<?> applicationExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("applicationExceptionHandler url: {}, 抛出异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ex.getMessage());
    }

    @ExceptionHandler(value = DataAccessResourceFailureException.class)
    public HttpWrapper<?> dataAccessResourceFailureExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("dataAccessResourceFailureExceptionHandler url: {}, 抛出异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.NETWORK_FLUCTUATION);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class, MethodArgumentNotValidException.class, BindException.class})
    public HttpWrapper<?> httpRequestExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("httpRequestExceptionHandler url: {}, http请求异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.INVALID_PARAM);
    }

    @ExceptionHandler(value = {HttpRequestException.class})
    public HttpWrapper<?> httpApiRequestExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("httpApiRequestExceptionHandler url: {}, http-api请求异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.HTTP_API);
    }

    @ExceptionHandler(value = {InvalidArgumentException.class, IllegalArgumentException.class, DisabledException.class})
    public HttpWrapper<?> invalidArgumentExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("invalidArgumentExceptionHandler url: {}, 参数异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ex.getMessage());
    }

    @ExceptionHandler(value = {IOException.class, IORuntimeException.class})
    public HttpWrapper<?> ioExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("ioExceptionHandler url: {}, io异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.IO_EXCEPTION_MESSAGE).data(ex.getMessage());
    }

    @ExceptionHandler(value = SQLException.class)
    public HttpWrapper<?> sqlExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("sqlExceptionHandler url: {}, sql异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.SQL_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler(value = {SftpException.class, com.jcraft.jsch.SftpException.class})
    public HttpWrapper<?> sftpExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("sftpExceptionHandler url: {}, sftp处理异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.OPERATOR_ERROR).data(ex.getMessage());
    }

    @ExceptionHandler(value = ParseRuntimeException.class)
    public HttpWrapper<?> parseExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("parseExceptionHandler url: {}, 解析异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        if (Exceptions.isCausedBy(ex, EncryptedDocumentException.class)) {
            // excel 密码错误
            return HttpWrapper.error(ExceptionMessageConst.OPEN_TEMPLATE_ERROR).data(ex.getMessage());
        } else {
            return HttpWrapper.error(ExceptionMessageConst.PARSE_TEMPLATE_DATA_ERROR).data(ex.getMessage());
        }
    }

    @ExceptionHandler(value = EncryptException.class)
    public HttpWrapper<?> encryptExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("encryptExceptionHandler url: {}, 数据加密异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.ENCRYPT_ERROR).data(ex.getMessage());
    }

    @ExceptionHandler(value = DecryptException.class)
    public HttpWrapper<?> decryptExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("decryptExceptionHandler url: {}, 数据解密异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.DECRYPT_ERROR).data(ex.getMessage());
    }

    @ExceptionHandler(value = VcsException.class)
    public HttpWrapper<?> vcsExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("vcsExceptionHandler url: {}, vcs处理异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.REPOSITORY_OPERATOR_ERROR).data(ex.getMessage());
    }

    @ExceptionHandler(value = {TaskExecuteException.class, ExecuteException.class})
    public HttpWrapper<?> taskExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("taskExceptionHandler url: {}, task处理异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.TASK_ERROR).data(ex.getMessage());
    }

    @ExceptionHandler(value = ConnectionRuntimeException.class)
    public HttpWrapper<?> connectionExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("connectionExceptionHandler url: {}, connect异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.CONNECT_ERROR).data(ex.getMessage());
    }

    @ExceptionHandler(value = {TimeoutException.class, java.util.concurrent.TimeoutException.class})
    public HttpWrapper<?> timeoutExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("timeoutExceptionHandler url: {}, timeout异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.TIMEOUT_ERROR).data(ex.getMessage());
    }

    @ExceptionHandler(value = {InterruptedException.class, InterruptedRuntimeException.class, InterruptedIOException.class})
    public HttpWrapper<?> interruptExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("interruptExceptionHandler url: {}, interrupt异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.INTERRUPT_ERROR).data(ex.getMessage());
    }

    @ExceptionHandler(value = UnsafeException.class)
    public HttpWrapper<?> unsafeExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("unsafeExceptionHandler url: {}, unsafe异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.UNSAFE_OPERATOR).data(ex.getMessage());
    }

    @ExceptionHandler(value = LogException.class)
    public HttpWrapper<?> logExceptionHandler(HttpServletRequest request, LogException ex) {
        log.error("logExceptionHandler url: {}, 处理异常打印日志: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.EXCEPTION_MESSAGE).data(ex.getMessage());
    }

    @ExceptionHandler(value = ParseCronException.class)
    public HttpWrapper<?> parseCronExceptionHandler(ParseCronException ex) {
        return HttpWrapper.error(ExceptionMessageConst.ERROR_EXPRESSION).data(ex.getMessage());
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public HttpWrapper<?> maxUploadSizeExceededExceptionHandler(HttpServletRequest request, MaxUploadSizeExceededException ex) {
        log.error("maxUploadSizeExceededExceptionHandler url: {}, 上传异常: {}, message: {}", request.getRequestURI(), ex.getClass(), ex.getMessage(), ex);
        return HttpWrapper.error(ExceptionMessageConst.FILE_TOO_LARGE).data(ex.getMessage());
    }

    @ExceptionHandler(value = CodeArgumentException.class)
    public HttpWrapper<?> codeArgumentExceptionHandler(CodeArgumentException ex) {
        return HttpWrapper.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = HttpWrapperException.class)
    public HttpWrapper<?> httpWrapperExceptionHandler(HttpWrapperException ex) {
        return ex.getWrapper();
    }

    @ExceptionHandler(value = RpcWrapperException.class)
    public HttpWrapper<?> rpcWrapperExceptionHandler(RpcWrapperException ex) {
        return ex.getWrapper().toHttpWrapper();
    }

}
