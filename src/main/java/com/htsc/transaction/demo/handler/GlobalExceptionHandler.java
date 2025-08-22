package com.htsc.transaction.demo.handler;

import com.htsc.transaction.demo.common.ErrorCodeEnums;
import com.htsc.transaction.demo.exception.DuplicateTransactionException;
import com.htsc.transaction.demo.exception.TransactionNotFoundException;
import com.htsc.transaction.demo.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ author: chaote
 * @ date: 2025/8/20  19:30
 * @ <p><p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 参数校验异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();

        Map<String, String> errorMap = bindingResult.getFieldErrors().stream().collect(
                Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse("")
                )
        );
        return Response.validFail(errorMap.toString());
    }

    /**
     * 交易不存在异常处理
     */
    @ExceptionHandler(TransactionNotFoundException.class)
    public Response<String> notFoundExceptionHandler(TransactionNotFoundException ex){
        return Response.fail(ErrorCodeEnums.TRANS_NOT_FOUND);
    }

    /**
     * 重复交易异常处理
     */
    @ExceptionHandler(DuplicateTransactionException.class)
    public Response<String> DuplicateTransactionExceptionHandler(DuplicateTransactionException ex){
        return Response.fail(ErrorCodeEnums.TRANS_DUPLICATE);
    }

    /**
     * 其他未知异常处理
     */
    @ExceptionHandler(Exception.class)
    public Response<String> ExceptionHandler(Exception ex){
        logger.error(ex.getMessage(),ex);
        return Response.fail(ErrorCodeEnums.TRANS_ERROR);
    }
}
