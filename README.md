# RabbitMQStudy

## 介绍各个模块

### HelloWord
RabbitMQ入门

### WordQueues
介绍了 RabbitMQ几种常用的工作队列

1. 轮询分发消息
2. 消息应答机制
3. 不公平分发消息
4. RabbitMQ持久化
5. 发布确认策略

### ExchangeDemo
介绍 RabbitMQ 的几种交换机
1. Direct exchange
2. Fanout exchange
3. Topic Exchange
4. Headers exchange


### DeadQueue
介绍 RabbitMQ 的死信队列 
成为死信队列的几种情况
- 消息TTL过期
- 队列达到最大长度
- 消息被拒绝

### DelayQueue
介绍延迟队列，两种实现方式：
- 死信队列
- 插件实现

### ConfirmPublish
发布确认高级
- 回退消息
- 备份交换机


### OtherContent
- 幂等性
- 优先队列
- 惰性队列
