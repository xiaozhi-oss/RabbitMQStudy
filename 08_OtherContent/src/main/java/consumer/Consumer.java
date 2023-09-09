package consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import utils.RabbitMQUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaozhi
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        Map<String, Object> params = new HashMap<>();
        params.put("x-queue-mod", "lazy");
        channel.queueDeclare("hello", false, false, false, params);
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("收到消息：" + msg);
        };
        channel.basicConsume("hello", true,  deliverCallback, consumerTag -> { System.out.println("消息未能处理"); });
    }
}
