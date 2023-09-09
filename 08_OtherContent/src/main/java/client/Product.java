package client;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import utils.RabbitMQUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author xiaozhi
 */
public class Product {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties().builder().priority(5).build();
        for (int i = 0; i < 100000; i++) {
            String msg = "info" + i;
            if (i == 5) {
                channel.basicPublish("", "hello", null, msg.getBytes(StandardCharsets.UTF_8));
            } else {
                channel.basicPublish("", "hello",properties, msg.getBytes(StandardCharsets.UTF_8));
            }
            System.out.println("发送消息：" + msg);
        }
    }
}
