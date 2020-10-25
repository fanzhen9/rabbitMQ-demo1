package org.gupaedu.example.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 */
public class FoxConsumer1 {

    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE_FANOUT";

    private final static String SIMPLE_QUEUE = "SIMPLE_QUEUE_FANOUT2";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.FANOUT,false);
            channel.queueDeclare(SIMPLE_QUEUE,false,false,false,null);
            System.out.println("等待消费中》》》》》");
            channel.queueBind(SIMPLE_QUEUE,EXCHANGE_NAME,"gp.best2");
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msg = new String(body,"UTF-8");
                    System.out.println("receiveMessage:"+msg);
                    System.out.println("consumerTag:"+consumerTag);
                    System.out.println("deliveryTag:"+envelope.getDeliveryTag());
                }
            };
            channel.basicConsume(SIMPLE_QUEUE,true,consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }
}
