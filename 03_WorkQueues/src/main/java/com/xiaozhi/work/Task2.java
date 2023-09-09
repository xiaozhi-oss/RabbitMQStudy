package com.xiaozhi.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.xiaozhi.utils.RabbitMQUtil;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author xiaozhi
 *
 * 生产者
 */
public class Task2 {

    public static final String WORK_QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 创建队列
        channel.queueDeclare(WORK_QUEUE_NAME, true, false, false, null);
        Scanner sc = new Scanner(System.in);
        while (true) {
            String message = sc.next();
            System.out.println("发送消息：" + message);
            channel.basicPublish("", WORK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
