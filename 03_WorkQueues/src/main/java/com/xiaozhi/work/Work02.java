package com.xiaozhi.work;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xiaozhi.utils.RabbitMQUtil;
import com.xiaozhi.utils.SleepUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaozhi
 *
 * 消息消费者
 */
public class Work02 {

    public static final String WORK_QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        System.out.println("c2应用等待消息...");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            SleepUtil.sleep(30);
            System.out.println("接收消息： " + msg);
            /*
                手动应答 -> Channel#basicAck
                    1.消息标记tag
                    2.批量应答 false：单个应答 ture：批量应答
             */
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息中断：" + consumerTag);
        };
        channel.basicConsume(WORK_QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
