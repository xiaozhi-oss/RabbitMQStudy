package com.xaiozhi.temporary;

import com.rabbitmq.client.Channel;
import com.xaiozhi.util.RabbitMQUtil;

/**
 * @author xiaozhi
 *
 * 创建临时队列
 */
public class CreateTemporaryQueue {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        // 创建临时队列
        String queueName = channel.queueDeclare().getQueue();
    }
}
