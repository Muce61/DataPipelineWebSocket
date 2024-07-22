package com.mc.handler;

import com.mc.core.RedisCache;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    private String apiSecret;
    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private RedisCache redisCache;

    @Autowired
    public void setMyRepository(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessions.add(session);
        Map<String, String> params = getQueryMap(Objects.requireNonNull(session.getUri()).getQuery());
        apiSecret = params.get("api-secret");
        String topics = params.get("topics");

        if (apiSecret != null && topics != null) {
            startKafkaListener(session, topics);
        } else {
            try {
                session.close(CloseStatus.BAD_DATA);
            } catch (Exception e) {
                LOGGER.error("关闭连接失败");
            }
        }
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage(session.getId().split("-")[0] + ": " + message.getPayload()));
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        sessions.remove(session);
        redisCache.deleteObject(apiSecret);
    }

    private void startKafkaListener(WebSocketSession session, String topics) {
        executorService.submit(() -> {
            KafkaConsumer<String, String> consumer = getStringStringKafkaConsumer(session);

            try (consumer) {
                consumer.subscribe(Collections.singletonList(topics));
                while (session.isOpen()) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        session.sendMessage(new TextMessage("Kafka: " + record.value()));
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Kafka消费异常", e);
            }
        });
    }

    private static KafkaConsumer<String, String> getStringStringKafkaConsumer(WebSocketSession session) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.87.101:9092,192.168.87.102:9092,192.168.87.103:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id_" + session.getId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new KafkaConsumer<>(props);
    }

    private Map<String, String> getQueryMap(String query) {
        Map<String, String> map = new ConcurrentHashMap<>();
        if (query == null) {
            return map;
        }
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1) {
                map.put(pair[0], pair[1]);
            } else {
                map.put(pair[0], "");
            }
        }
        return map;
    }
}
