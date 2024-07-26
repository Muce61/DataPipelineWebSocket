package com.mc.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author Anumbrella 拦截器
 */
@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse, @NonNull WebSocketHandler webSocketHandler, @NonNull Map<String, Object> attributes) {
        LOGGER.info("=============== 握手前 =============");
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse, @NonNull WebSocketHandler webSocketHandler, Exception e) {
        LOGGER.info("=============== 握手后 =============");
    }
}
