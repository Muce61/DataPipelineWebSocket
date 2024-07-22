package com.mc.config;

import com.mc.handler.ChatWebSocketHandler;
import com.mc.intecptor.AuthHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final AuthHandshakeInterceptor authHandshakeInterceptor;
    private final ChatWebSocketHandler chatWebSocketHandler;

    @Autowired
    public WebSocketConfig(AuthHandshakeInterceptor authHandshakeInterceptor, ChatWebSocketHandler chatWebSocketHandler) {
        this.authHandshakeInterceptor = authHandshakeInterceptor;
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/chat")
                .addInterceptors(authHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
