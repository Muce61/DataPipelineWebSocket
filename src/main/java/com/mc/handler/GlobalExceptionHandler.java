package com.mc.handler;


import cn.hutool.core.util.ArrayUtil;
import com.mc.common.Result;
import com.mc.enums.RespEnum;
import com.mc.util.I18nUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.net.ConnectException;
import java.sql.SQLException;

@RestControllerAdvice(annotations = {RestController.class, Controller.class, Component.class})
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Resource
    private I18nUtil i18nUtil;

    /**
     * 系统出现异常（能捕获所有的）
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> systemError(Exception e) {
        this.printException(e);
        return formatResult(RespEnum.SYSTEM_ERROR);
    }

    /**
     * 数据库操作出现异常（特定异常）
     */
    @ExceptionHandler(value = {SQLException.class, DataAccessException.class})
    @ResponseBody
    public Result<?> databaseError(Exception e) {
        this.printException(e);
        return formatResult(RespEnum.DATABASE_ERROR);
    }

    /**
     * 远程连接失败（特定异常）
     */
    @ExceptionHandler(value = {ConnectException.class})
    @ResponseBody
    public Result<?> connectError(Exception e) {
        this.printException(e);
        return formatResult(RespEnum.CONNECTION_ERROR);
    }

    private Result<?> formatResult(RespEnum respEnum) {
        // 错误码
        int errCode = respEnum.getCode();
        // 错误提示
        String errMsg = i18nUtil.getMessage(errCode);
        return Result.of(errCode, errMsg);
    }

    /**
     * 参数校验异常
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e) {
        return this.packValidExceptionResult(e.getBindingResult());
    }

    private Result<?> packValidExceptionResult(BindingResult bindingResult) {
        // 错误码
        int errCode = RespEnum.BAD_REQUEST.getCode();
        // 错误提示
        String errMsg = i18nUtil.getMessage(errCode);
        String errTip = errMsg;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                String msgKey = StringUtils.defaultString(fieldError.getDefaultMessage());
                // 获取实际校验参数值（去掉第一个参数对象）
                Object[] args = ArrayUtil.remove(fieldError.getArguments(), 0);
                if (ArrayUtil.get(args, 0) instanceof Boolean) {
                    args = ArrayUtil.remove(args, 0);
                }
                errTip = "[" + fieldError.getField() + "] " + i18nUtil.handleMessage(msgKey, args);
            }
        }
        logger.error("occurs error, message: {}", errTip);
        return Result.of(errTip, errCode, errMsg);
    }

    /**
     * 参数绑定异常
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result<?> handleBindException(BindException e) {
        return this.packValidExceptionResult(e.getBindingResult());
    }

    @ExceptionHandler(value = {MultipartException.class})
    @ResponseBody
    public Result<?> sizeLimitExceededError(MultipartException e) {
        this.printException(e);
        return formatResult(RespEnum.SIZE_LIMIT_ERROR);
    }

    private void printException(Exception e) {
        if (e != null) {
            logger.error("occurs error: {}, message: {}", e.getClass().getName(), e.getMessage());
            // 获取异常堆栈信息
            StackTraceElement[] stackTraceElements = e.getStackTrace();
            if (stackTraceElements == null) {
                return;
            }
            for (int i = 0; i < stackTraceElements.length; i ++) {
                if (i >= 5) {
                    // 如超过则只打印前5个
                    break;
                }
                System.err.println("\tat " + stackTraceElements[i]);
            }
        }
    }
}