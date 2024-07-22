package com.mc.intecptor;

import com.alibaba.fastjson.JSON;
import com.mc.common.HandshakeException;
import com.mc.common.Result;
import com.mc.core.RedisCache;
import com.mc.enums.RespEnum;
import com.mc.service.SecurityService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Anumbrella 拦截器
 */
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthHandshakeInterceptor.class);

    private RedisCache redisCache;
    @Resource
    private SecurityService securityService;

    @Autowired
    public void setMyRepository(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse, @NonNull WebSocketHandler webSocketHandler, @NonNull Map<String, Object> attributes) throws Exception {
        LOGGER.info("=============== 握手前 =============");

        if (serverHttpRequest instanceof ServletServerHttpRequest servletRequest) {
            String topics = servletRequest.getServletRequest().getParameter("topics");
            String apiSecret = servletRequest.getServletRequest().getParameter("api-secret");

            if (topics != null && !topics.isEmpty() && apiSecret != null && !apiSecret.isEmpty()) {
                attributes.put("topics", topics);
                attributes.put("api-secret", apiSecret);

//                Integer pass = securityService.isPass(apiSecret);
//                if (pass == 0) {
//                    LOGGER.info("无订阅权限");
//                    String resultJson = JSON.toJSONString(Result.of(RespEnum.API_SECRET_NOT_EXISTS));
//                    throw new Exception();
//                }

                if (redisCache.hasKey(apiSecret)) {
                    LOGGER.info("apiSecret 已存在");
                    String resultJson = JSON.toJSONString(Result.of(RespEnum.API_CONSUMER_EXISTS));
                    throw new HandshakeException(resultJson);
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(apiSecret, topics);
                    redisCache.setCacheMap(apiSecret ,map);
                    redisCache.expire(apiSecret, 60 * 60 * 12);
                    LOGGER.warn("参数缓存成功");
                    return true;
                }
            } else {
                LOGGER.warn("Missing required handshake parameters: topics or api-secret.");
                String resultJson = JSON.toJSONString(Result.of(RespEnum.BAD_REQUEST));
                throw new HandshakeException(resultJson);
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse, @NonNull WebSocketHandler webSocketHandler, Exception e) {
        LOGGER.info("=============== 握手后 =============");
    }
}
