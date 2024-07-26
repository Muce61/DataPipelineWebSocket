# DataPipelineWebSocket 项目

## 项目简介

DataPipelineWebSocket 为基于 Spring Boot 和 WebSocket 的数据管道的项目。通过 WebSocket 提供实时数据传输功能，并集成了Redis 用于缓存和管理数据与 Mysql数据库管理查询验证相关权限数据。



启动请配置中指定环境变量（Environment variables）：

--spring.profiles.active=dev;--spring.config.additional-location=etc/conf.app.d/,etc/conf.infra.d/,etc/conf.security.d/

# 参数文档

通过websocket发送消息订阅



消息示例：

```json
{
    "api-secret": "55",
    "topics": "Flight-Planned-test-Z,Flight-Planned-test-A",
    "type": "subscription"
}
```



| 参数描述   |                                                              |
| ---------- | :----------------------------------------------------------- |
| topics     | 订阅的主题，多个主题由 , 分隔                                |
| type       | 操作类型，subscription为订阅， close为退订                   |
| api-secret | 此为各公司对应的 唯一订阅ID ，应在新增公司时由后端生成32位uuid存入公司表 |
