package com.xaiozhi.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.xaiozhi.util.RabbitMQUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaozhi
 *
 * 发送日志
 */
public class EmitLogDirect {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtil.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 创建多个 bindingKey
        Map<String, String> bindingKeyMap = new HashMap<String, String>();
        bindingKeyMap.put("info", "普通 info 消息");
        bindingKeyMap.put("warning","警告 warning 信息");
        bindingKeyMap.put("debug", "调试 bug 消息");
        bindingKeyMap.put("error", "错误 error 消息");
        bindingKeyMap.forEach((k, v) -> {
            try {
                channel.basicPublish(EXCHANGE_NAME, k, null, v.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("生产者发出消息：" + v);
        });
    }
}
