package com.mc.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.mc.config.Constants;
import com.mc.core.RedisCache;
import com.mc.dao.SubscriptionDao;
import com.mc.enums.Members;
import com.mc.service.SecurityService;
import io.lettuce.core.RedisCommandExecutionException;
import io.micrometer.common.util.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
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

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatWebSocketHandler.class);

    private static final Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<>());
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<WebSocketSession, Map<String, Future<?>>> sessionTopicFutures = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, Map<String, KafkaConsumer<String, String>>> sessionTopicConsumers = new ConcurrentHashMap<>();

    private RedisCache redisCache;

    private SecurityService securityService;

    @Autowired
    public void setMyRepository(RedisCache redisCache, SecurityService securityService) {
        this.redisCache = redisCache;
        this.securityService = securityService;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws IOException {
        sessions.add(session);
        session.sendMessage(new TextMessage("连接成功"));
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        session.sendMessage(new TextMessage(session.getId().split("-")[0] + ": " + message.getPayload()));

        String payload = message.getPayload();
        if ("ping".equals(payload)) {
            session.sendMessage(new TextMessage(Members.ADMIN.getMsg() + ": " + "pong"));
        }
        try {
            SubscriptionDao subscriptionDao = JSON.parseObject(payload, SubscriptionDao.class);
            if (StringUtils.isNotBlank(subscriptionDao.getApiSecret()) && StringUtils.isNotBlank(subscriptionDao.getTopics()) &&
                    StringUtils.isNotBlank(subscriptionDao.getType()) && "subscription".equals(subscriptionDao.getType())) {

                String[] topics = subscriptionDao.getTopics().split(",");

                boolean isPass = securityService.isPass(subscriptionDao.getApiSecret(), topics);
                if (isPass) {
                    Arrays.stream(topics).forEach(
                            topic -> startKafkaListener(session, topic, subscriptionDao.getApiSecret())
                    );
                } else {
                    session.sendMessage(new TextMessage(Members.ADMIN.getMsg() + ": " + "鉴权失败"));
                }


            } else if (StringUtils.isNotBlank(subscriptionDao.getApiSecret()) && StringUtils.isNotBlank(subscriptionDao.getTopics()) &&
                    StringUtils.isNotBlank(subscriptionDao.getType()) && "close".equals(subscriptionDao.getType())) {
                closeKafkaListener(session, subscriptionDao.getTopics(), subscriptionDao.getApiSecret());
            } else {
                session.sendMessage(new TextMessage(Members.ADMIN.getMsg() + ": " + "格式错误"));
            }
        } catch (JSONException ignored) {
            session.sendMessage(new TextMessage(Members.ADMIN.getMsg() + ": " + "格式错误"));
        }

    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        sessions.remove(session);
        closeAllKafkaListeners(session);
    }

    private void startKafkaListener(WebSocketSession session, String topic, String apiSecret) {
        // 使用 apiSecret 和 topic 作为 Redis 键
        String redisKey = apiSecret + Constants.UNDER_LINE + topic;
        String existingSessionId = redisCache.getCacheObject(redisKey);

        if (existingSessionId != null && !existingSessionId.equals(session.getId())) {
            // 找到旧地会话并关闭它
            WebSocketSession existingSession = null;
            for (WebSocketSession s : sessions) {
                if (s.getId().equals(existingSessionId)) {
                    existingSession = s;
                    break;
                }
            }

            if (existingSession != null) {
                closeAllKafkaListeners(existingSession);
                try {
                    existingSession.close();
                } catch (IOException e) {
                    LOGGER.error("关闭旧会话时出错", e);
                }
            }
        }

        // 将新的会话 ID 保存到 Redis
        redisCache.setCacheObject(redisKey, session.getId());

        Future<?> future = executorService.submit(() -> {
            KafkaConsumer<String, String> consumer = getStringStringKafkaConsumer(session);

            // 将 KafkaConsumer 保存到 sessionTopicConsumers 中
            sessionTopicConsumers.computeIfAbsent(session, k -> new ConcurrentHashMap<>()).put(topic, consumer);

            try (consumer) {
                consumer.subscribe(Collections.singletonList(topic));
                while (session.isOpen()) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        synchronized (session) { // 确保发送消息是同步的
                            session.sendMessage(new TextMessage("Kafka: " + record.value()));
                        }
                    }
                }
            } catch (WakeupException ignore) {

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // 将 Future 保存到 sessionTopicFutures 中
        sessionTopicFutures.computeIfAbsent(session, k -> new ConcurrentHashMap<>()).put(topic, future);
    }

    private void closeKafkaListener(WebSocketSession session, String topics, String apiSecret) {
        for (String topic : topics.split(",")) {
            Map<String, Future<?>> topicFutures = sessionTopicFutures.get(session);
            Map<String, KafkaConsumer<String, String>> topicConsumers = sessionTopicConsumers.get(session);

            if (topicFutures != null && topicFutures.containsKey(topic)) {
                Future<?> future = topicFutures.remove(topic);
                if (future != null) {
                    future.cancel(true);
                }
            }

            if (topicConsumers != null && topicConsumers.containsKey(topic)) {
                KafkaConsumer<String, String> consumer = topicConsumers.remove(topic);
                if (consumer != null) {
                    consumer.wakeup();
                }
            }

            // 从 Redis 中删除该 api-secret 和 topic 的键
            String redisKey = apiSecret + Constants.UNDER_LINE + topic;
            redisCache.deleteObject(redisKey);
        }
    }

    private void closeAllKafkaListeners(WebSocketSession session) {
        Map<String, Future<?>> topicFutures = sessionTopicFutures.remove(session);
        Map<String, KafkaConsumer<String, String>> topicConsumers = sessionTopicConsumers.remove(session);

        if (topicFutures != null) {
            for (Future<?> future : topicFutures.values()) {
                future.cancel(true);
            }
        }

        if (topicConsumers != null) {
            for (KafkaConsumer<String, String> consumer : topicConsumers.values()) {
                consumer.wakeup();
            }
        }

        // 从 Redis 中删除与该会话相关的所有 api-secret 和 topic 的键
        for (String redisKey : redisCache.getAllKeys()) {
            try {
                String sessionId = redisCache.getCacheObject(redisKey);
                if (sessionId != null && sessionId.equals(session.getId())) {
                    redisCache.deleteObject(redisKey);
                }
            } catch (RedisCommandExecutionException ignored) {
            }


        }
    }

    private static KafkaConsumer<String, String> getStringStringKafkaConsumer(WebSocketSession session) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.87.101:9092,192.168.87.102:9092,192.168.87.103:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id" + Constants.UNDER_LINE + session.getId() + Constants.UNDER_LINE + UUID.randomUUID());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new KafkaConsumer<>(props);
    }
}
