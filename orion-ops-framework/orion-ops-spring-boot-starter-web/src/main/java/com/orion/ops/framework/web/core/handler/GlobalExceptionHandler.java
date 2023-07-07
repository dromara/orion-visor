package com.orion.ops.framework.web.core.handler;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.lang.exception.*;
import com.orion.lang.exception.argument.CodeArgumentException;
import com.orion.lang.exception.argument.HttpWrapperException;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.exception.argument.RpcWrapperException;
import com.orion.lang.utils.Exceptions;
import com.orion.ops.framework.common.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
    public HttpWrapper<?> defaultExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("defaultExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.INTERNAL_SERVER_ERROR.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = ApplicationException.class)
    public HttpWrapper<?> applicationExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("applicationExceptionHandler url: {}", request.getRequestURI(), ex);
        return HttpWrapper.error(ex.getMessage());
    }

    @ExceptionHandler(value = DataAccessResourceFailureException.class)
    public HttpWrapper<?> dataAccessResourceFailureExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("dataAccessResourceFailureExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.NETWORK_FLUCTUATION.getWrapper();
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public void accessDeniedExceptionHandler(AccessDeniedException ex) throws AccessDeniedException {
        // 会拦截异常导致 Security 策略不生效 需要重新抛出
        throw ex;
    }

    // FIXME @validated
    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class, MethodArgumentNotValidException.class, BindException.class})
    public HttpWrapper<?> httpRequestParameterExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("httpRequestParameterExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.BAD_REQUEST.getWrapper();
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public HttpWrapper<?> httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("httpRequestMethodNotSupportedExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.METHOD_NOT_ALLOWED.getWrapper();
    }

    @ExceptionHandler(value = {HttpRequestException.class})
    public HttpWrapper<?> httpApiRequestExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("httpApiRequestExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.HTTP_API.getWrapper();
    }

    @ExceptionHandler(value = {InvalidArgumentException.class, IllegalArgumentException.class, DisabledException.class})
    public HttpWrapper<?> invalidArgumentExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("invalidArgumentExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.BAD_REQUEST.wrapper().msg(ex.getMessage());
    }

    @ExceptionHandler(value = {IOException.class, IORuntimeException.class})
    public HttpWrapper<?> ioExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("ioExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.IO_EXCEPTION.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = SQLException.class)
    public HttpWrapper<?> sqlExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("sqlExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.SQL_EXCEPTION.getWrapper();
    }

    @ExceptionHandler(value = {SftpException.class, com.jcraft.jsch.SftpException.class})
    public HttpWrapper<?> sftpExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("sftpExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.SFTP_EXCEPTION.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = ParseRuntimeException.class)
    public HttpWrapper<?> parseExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("parseExceptionHandler url: {}", request.getRequestURI(), ex);
        if (Exceptions.isCausedBy(ex, EncryptedDocumentException.class)) {
            // excel 密码错误
            return ErrorCode.EXCEL_PASSWORD_ERROR.wrapper(ex.getMessage());
        } else {
            return ErrorCode.PASER_FAILED.wrapper(ex.getMessage());
        }
    }

    @ExceptionHandler(value = EncryptException.class)
    public HttpWrapper<?> encryptExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("encryptExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.ENCRYPT_ERROR.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = DecryptException.class)
    public HttpWrapper<?> decryptExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("decryptExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.DECRYPT_ERROR.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = VcsException.class)
    public HttpWrapper<?> vcsExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("vcsExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.VCS_OPETATOR_ERROR.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = {TaskExecuteException.class, ExecuteException.class})
    public HttpWrapper<?> taskExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("taskExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.TASK_EXECUTE_ERROR.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = ConnectionRuntimeException.class)
    public HttpWrapper<?> connectionExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("connectionExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.CONNECT_ERROR.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = {TimeoutException.class, java.util.concurrent.TimeoutException.class})
    public HttpWrapper<?> timeoutExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("timeoutExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.REQUEST_TIMEOUT.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = {InterruptedException.class, InterruptedRuntimeException.class, InterruptedIOException.class})
    public HttpWrapper<?> interruptExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("interruptExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.INTERRUPT_ERROR.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = UnsafeException.class)
    public HttpWrapper<?> unsafeExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error("unsafeExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.UNSAFE_OPERATOR.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = LogException.class)
    public HttpWrapper<?> logExceptionHandler(HttpServletRequest request, LogException ex) {
        log.error("logExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.INTERNAL_SERVER_ERROR.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = ParseCronException.class)
    public HttpWrapper<?> parseCronExceptionHandler(ParseCronException ex) {
        return ErrorCode.EXPRESSION_ERROR.wrapper(ex.getMessage());
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public HttpWrapper<?> maxUploadSizeExceededExceptionHandler(HttpServletRequest request, MaxUploadSizeExceededException ex) {
        log.error("maxUploadSizeExceededExceptionHandler url: {}", request.getRequestURI(), ex);
        return ErrorCode.PAYLOAD_TOO_LARGE.wrapper(ex.getMessage());
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
