package codexe.han.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HelloRabbitMq {
}

class Send{
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        String host = "172.28.2.22";
        ConnectionFactory factory = new ConnectionFactory();
        try(Connection connection = factory.newConnection()){
            Channel channel = connection.createChannel();
            /**
             durable
             含义：是否持久化，如果设置为true，服务器重启了队列仍然存在
             exclusive
             含义：是否为独享队列（排他性队列），只有自己可见的队列，即不允许其它用户访问
             autoDelete
             含义：当没有任何消费者使用时，自动删除该队列
             */
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            String message = "Hello World!";
            /**
             Default Exchange
             broker提前定义的空字符串
             每一个创建的queue会自动与其绑定，绑定方式是通过一个和queue名字一样的routing key

             Direct Exchange
             queue1会和routing key1 绑定，如果一个message的k1 = routing key1就会发送到对应的queue1

             Fanout Exchange

             */
            channel.basicPublish("",QUEUE_NAME, null, message.getBytes());
            System.out.println();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
