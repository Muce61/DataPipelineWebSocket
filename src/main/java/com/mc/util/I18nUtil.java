package com.mc.util;



import com.mc.enums.RespEnum;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

@Component
public final class I18nUtil {

    private static final Logger logger = LoggerFactory.getLogger(I18nUtil.class);

    public final static String FORMAT_PREFIX = "Format";

    @Resource
    private MessageSource messageSource;

    /**
     * 根据 [响应枚举] 获取对应的语言信息
     *
     * @param respEnum 响应枚举
     * @return String
     */
    public String getMessage(RespEnum respEnum) {
        if (Objects.isNull(respEnum)) {
            respEnum = RespEnum.SYSTEM_ERROR;
        }
        return getMessage(respEnum.getCode());
    }

    /**
     * 根据 [错误码] 获取对应的语言信息
     *
     * @param errCode 错误码
     * @return String
     */
    public String getMessage(int errCode) {
        return getMessage(String.valueOf(errCode), null);
    }

    /**
     * 根据 [信息key] 获取对应的语言信息
     *
     * @param msgKey 信息key: 国际化配置（xx.properties）中的key
     * @return String
     */
    public String getMessage(String msgKey) {
        return getMessage(msgKey, null);
    }

    /**
     * 根据 [信息key] 获取对应格式化后的语言信息
     *
     * @param msgKey 信息key: 国际化配置（xx.properties）中的key
     * @return String
     */
    public String formatMessage(String msgKey, Object[] objects) {
        return getMessage(msgKey, objects);
    }

    /**
     * 获取语言信息
     *
     * @param msgKey  信息key: 国际化配置（xx.properties）中的key
     * @param objects 格式化入参
     * @return String
     */
    public String getMessage(String msgKey, Object[] objects) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(msgKey, objects, locale);
        } catch (Exception e) {
            logger.error("===> [国际化] 发生异常, msgKey: {}, error: {}", msgKey, e.getMessage());
            return msgKey;
        }
    }

    /**
     * 根据 [信息key] 进行判断区分是否进行格式化<br/>
     * 注：如果是[Format]开头，则进行格式化参数处理
     *
     * @param msgKey  信息key: 国际化配置（xx.properties）中的key
     * @param objects 格式化入参
     * @return String
     */
    public String handleMessage(String msgKey, Object[] objects) {
        if (StringUtils.defaultString(msgKey).startsWith(FORMAT_PREFIX)) {
            return formatMessage(msgKey, objects);
        }
        return getMessage(msgKey);
    }

}
