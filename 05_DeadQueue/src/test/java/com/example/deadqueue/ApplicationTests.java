package com.example.deadqueue;

import com.example.deadqueue.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApplicationTests {

	@Resource
	private CachingConnectionFactory rabbitConnectionFactory;

	@SneakyThrows
	@Test
	public void sendMsg() {
		for (int i = 0; i < 6; i++) {
			Connection connection = rabbitConnectionFactory.createConnection();
			// 发送6条消息
			Channel channel = connection.createChannel(false);
			channel.basicPublish("", RabbitMQConfig.QUEUE_A, null, String.valueOf(i).getBytes());
		}
	}

}

