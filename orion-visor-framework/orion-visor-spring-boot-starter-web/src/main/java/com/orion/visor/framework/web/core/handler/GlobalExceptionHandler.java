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
package com.orion.visor.framework.web.core.handler;

import com.orion.lang.define.wrapper.HttpWrapper;
import com.orion.lang.exception.*;
import com.orion.lang.exception.argument.CodeArgumentException;
import com.orion.lang.exception.argument.HttpWrapperException;
import com.orion.lang.exception.argument.InvalidArgumentException;
import com.orion.lang.exception.argument.RpcWrapperException;
import com.orion.lang.utils.Exceptions;
import com.orion.lang.utils.Strings;
import com.orion.visor.framework.common.constant.Const;
import com.orion.visor.framework.common.constant.ErrorCode;
import com.orion.visor.framework.common.constant.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.sql.SQLException;
import java.util.Objects;

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

    // -------------------- 默认 --------------------

    @ExceptionHandler(value = Exception.class)
    public HttpWrapper<?> defaultExceptionHandler(Exception ex) {
        log.error("defaultExceptionHandler", ex);
        return ErrorCode.INTERNAL_SERVER_ERROR.wrapper();
    }

    // -------------------- http 异常 --------------------

    @ExceptionHandler(value = {
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            ValidationException.class,
            MissingRequestValueException.class
    })
    public HttpWrapper<?> badRequestExceptionHandler(Exception ex) {
        log.error("badRequestExceptionHandler", ex);
        return ErrorCode.BAD_REQUEST.getWrapper();
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public HttpWrapper<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        String message = Strings.format(ErrorMessage.MISSING, ex.getParameterName());
        log.error("missingServletRequestParameterExceptionHandler {}", message);
        return ErrorCode.BAD_REQUEST.wrapper().msg(message);
    }

    @ExceptionHandler(value = BindException.class)
    public HttpWrapper<?> paramBindExceptionHandler(BindException ex) {
        FieldError error = Objects.requireNonNull(ex.getFieldError());
        String message = error.getField() + Const.SPACE + error.getDefaultMessage();
        log.error("paramBindExceptionHandler {}", message);
        return ErrorCode.BAD_REQUEST.wrapper().msg(message);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public HttpWrapper<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        ConstraintViolation<?> violation = Objects.requireNonNull(ex.getConstraintViolations().iterator().next());
        String message = violation.getPropertyPath().toString() + Const.SPACE + violation.getMessage();
        log.error("constraintViolationExceptionHandler {}", message);
        return ErrorCode.BAD_REQUEST.wrapper().msg(message);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public HttpWrapper<?> httpRequestMethodNotSupportedExceptionHandler(Exception ex) {
        log.error("httpRequestMethodNotSupportedExceptionHandler {}", ex.getMessage());
        return ErrorCode.METHOD_NOT_ALLOWED.getWrapper();
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public HttpWrapper<?> maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException ex) {
        log.error("maxUploadSizeExceededExceptionHandler", ex);
        return ErrorCode.PAYLOAD_TOO_LARGE.wrapper();
    }

    // -------------------- 框架异常 --------------------

    @ExceptionHandler(value = DataAccessResourceFailureException.class)
    public HttpWrapper<?> dataAccessResourceFailureExceptionHandler(Exception ex) {
        log.error("dataAccessResourceFailureExceptionHandler", ex);
        return ErrorCode.NETWORK_FLUCTUATION.getWrapper();
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public void accessDeniedExceptionHandler(AccessDeniedException ex) throws AccessDeniedException {
        // 会拦截异常导致 Security 策略不生效 需要重新抛出
        throw ex;
    }

    // -------------------- 业务异常 --------------------

    @ExceptionHandler(value = {
            InvalidArgumentException.class,
            IllegalArgumentException.class
    })
    public HttpWrapper<?> invalidArgumentExceptionHandler(Exception ex) {
        log.error("invalidArgumentExceptionHandler {}", ex.getMessage(), ex);
        return ErrorCode.BAD_REQUEST.wrapper().msg(ex.getMessage());
    }

    @ExceptionHandler(value = {
            TimeoutException.class,
            java.util.concurrent.TimeoutException.class
    })
    public HttpWrapper<?> timeoutExceptionHandler(Exception ex) {
        log.error("timeoutExceptionHandler", ex);
        return ErrorCode.REQUEST_TIMEOUT.wrapper();
    }

    @ExceptionHandler(value = {
            InterruptedException.class,
            InterruptedRuntimeException.class,
            InterruptedIOException.class
    })
    public HttpWrapper<?> interruptExceptionHandler(Exception ex) {
        log.error("interruptExceptionHandler", ex);
        return ErrorCode.INTERRUPT_ERROR.wrapper();
    }

    @ExceptionHandler(value = {
            IOException.class,
            IORuntimeException.class
    })
    public HttpWrapper<?> ioExceptionHandler(Exception ex) {
        log.error("ioExceptionHandler", ex);
        return ErrorCode.IO_EXCEPTION.wrapper();
    }

    @ExceptionHandler(value = SQLException.class)
    public HttpWrapper<?> sqlExceptionHandler(Exception ex) {
        log.error("sqlExceptionHandler", ex);
        return ErrorCode.SQL_EXCEPTION.getWrapper();
    }

    @ExceptionHandler(value = ApplicationException.class)
    public HttpWrapper<?> applicationExceptionHandler(Exception ex) {
        log.error("applicationExceptionHandler", ex);
        return HttpWrapper.error(ex.getMessage());
    }

    @ExceptionHandler(value = DisabledException.class)
    public HttpWrapper<?> disabledExceptionHandler(Exception ex) {
        log.error("disabledExceptionHandler", ex);
        return ErrorCode.DIABLED_ERROR.getWrapper();
    }

    @ExceptionHandler(value = {
            SftpException.class,
            com.jcraft.jsch.SftpException.class
    })
    public HttpWrapper<?> sftpExceptionHandler(Exception ex) {
        log.error("sftpExceptionHandler", ex);
        return ErrorCode.SFTP_EXCEPTION.wrapper();
    }

    @ExceptionHandler(value = ParseRuntimeException.class)
    public HttpWrapper<?> parseExceptionHandler(Exception ex) {
        log.error("parseExceptionHandler", ex);
        if (Exceptions.isCausedBy(ex, EncryptedDocumentException.class)) {
            // excel 密码错误
            return ErrorCode.EXCEL_PASSWORD_ERROR.wrapper();
        } else {
            return ErrorCode.PASER_FAILED.wrapper();
        }
    }

    @ExceptionHandler(value = EncryptException.class)
    public HttpWrapper<?> encryptExceptionHandler(Exception ex) {
        log.error("encryptExceptionHandler", ex);
        return ErrorCode.ENCRYPT_ERROR.wrapper();
    }

    @ExceptionHandler(value = DecryptException.class)
    public HttpWrapper<?> decryptExceptionHandler(Exception ex) {
        log.error("decryptExceptionHandler", ex);
        return ErrorCode.DECRYPT_ERROR.wrapper();
    }

    @ExceptionHandler(value = {HttpRequestException.class})
    public HttpWrapper<?> httpApiRequestExceptionHandler(Exception ex) {
        log.error("httpApiRequestExceptionHandler", ex);
        return ErrorCode.HTTP_API_REQUEST_ERROR.getWrapper();
    }

    @ExceptionHandler(value = VcsException.class)
    public HttpWrapper<?> vcsExceptionHandler(Exception ex) {
        log.error("vcsExceptionHandler", ex);
        return ErrorCode.VCS_OPETATOR_ERROR.wrapper();
    }

    @ExceptionHandler(value = {
            TaskExecuteException.class,
            ExecuteException.class
    })
    public HttpWrapper<?> taskExceptionHandler(Exception ex) {
        log.error("taskExceptionHandler", ex);
        return ErrorCode.TASK_EXECUTE_ERROR.wrapper();
    }

    @ExceptionHandler(value = ConnectionRuntimeException.class)
    public HttpWrapper<?> connectionExceptionHandler(Exception ex) {
        log.error("connectionExceptionHandler", ex);
        return ErrorCode.CONNECT_ERROR.wrapper();
    }

    @ExceptionHandler(value = UnsafeException.class)
    public HttpWrapper<?> unsafeExceptionHandler(Exception ex) {
        log.error("unsafeExceptionHandler", ex);
        return ErrorCode.UNSAFE_OPERATOR.wrapper();
    }

    @ExceptionHandler(value = LogException.class)
    public HttpWrapper<?> logExceptionHandler(LogException ex) {
        log.error("logExceptionHandler", ex);
        return ErrorCode.INTERNAL_SERVER_ERROR.wrapper();
    }

    @ExceptionHandler(value = CodeArgumentException.class)
    public HttpWrapper<?> codeArgumentExceptionHandler(CodeArgumentException ex) {
        return HttpWrapper.of(ex.getCode(), ex.getMessage());
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
