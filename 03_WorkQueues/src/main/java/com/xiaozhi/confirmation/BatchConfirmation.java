package com.xiaozhi.confirmation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import com.xiaozhi.utils.RabbitMQUtil;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author xiaozhi
 *
 * 批量确认发布
 */
public class BatchConfirmation {

    public static final Integer COUNT = 1000;

    public static void main(String[] args) throws Exception {
        individualConfirmation();
        batchConfirmation();
        asyncConfirmation();
    }

    public static void individualConfirmation() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = String.valueOf(UUID.randomUUID());
        // 创建队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 开启确定发布
        channel.confirmSelect();
        // 统计时间
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            channel.basicPublish("", queueName,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, String.valueOf(i).getBytes());
            // 确认发布
            boolean flag = channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("单个确认耗时：：" + (end - start) + "ms");
    }

    public static void batchConfirmation() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = String.valueOf(UUID.randomUUID());
        // 创建队列
        channel.queueDeclare(queueName, true, false, false, null);
        // 开启确定发布
        channel.confirmSelect();
        // 批量确认个数
        int batchSize = 100;
        // 未确认个数
        int outstandingMessageCount = 0;
        // 统计时间
        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            channel.basicPublish("", queueName,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, String.valueOf(i).getBytes());
            outstandingMessageCount++;
            if (outstandingMessageCount % batchSize == 0) {
                channel.waitForConfirms();
                outstandingMessageCount = 0;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("批量确认耗时：" + (end - start) + "ms");
        // 生产消息的总时长为：934ms
    }

    public static void asyncConfirmation() throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.confirmSelect();
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        ConfirmCallback confirmCallback = (deliveryTag, multiple) -> {
            // 2.将确认收到的消息移除 Map
            if (multiple) {
                // 批处理
                // ConcurrentSkipListMap#headMap(toKey)：返回此映射的部分视图，其键值严格小于 toKey
                ConcurrentNavigableMap<Long, String> map = outstandingConfirms.headMap(deliveryTag);
                // 清空
                map.clear();
            } else {
                // 删除对应 deliveryTag 的消息
                outstandingConfirms.remove(deliveryTag);
            }
        };
        ConfirmCallback nackCallback = (sequenceNumber, multiple) -> {
            // 3.将发送失败的消息重新发送
            String message = outstandingConfirms.get(sequenceNumber);
            // 重新发送
            try {
                channel.waitForConfirms();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        /*
            添加一个异步确认的监听器
                1.确认收到消息的回调
                2.未收到消息的回调
         */
        channel.addConfirmListener(confirmCallback, nackCallback);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            // 1.将发送的消息保存到 Map 中
            String message = String.valueOf(i);
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", queueName,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("异步确认耗时：" + (end - begin) + "ms");
    }

}
