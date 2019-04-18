package codexe.han.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkQueues {
    /**
     * 多个worker 消费同一个queue
     */

}

class SendWorkQueues{
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        String host = "172.28.2.22";
        /**
         default
         username:guest
         password:guest
         */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

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
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);//Temporary Queue
            String message = "Hello World!";
            /**
             Default Exchange
             broker提前定义的空字符串(没有名字的direct exchange)
             每一个创建的queue会自动与其绑定，绑定方式是通过一个和queue名字一样的routing key

             Direct Exchange
             适用于一对一传输
             queue1会和routing key1 绑定，如果一个message的k1 = routing key1就会发送到对应的queue1

             Fanout Exchange(扇形)
             适用于一对多传输，广播
             忽略routing key，传输到与该exchange绑定的所有queue上

             Topic Exchange
             适用于订阅/发布场景

             Header Exchange
             忽略routing key，根据header attribute进行路由
             */
            /**
             channel为了进行多路复用
             connections are multiplexed with channels that can be thought of as "lightweight connections that share a single TCP connection
             */
            channel.basicPublish("",QUEUE_NAME, null, message.getBytes());
            System.out.println("Sent "+message);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
class RecvWorkQueues{
    private volatile boolean exit = false;
    private final static String QUEUE_NAME = "hello";

    public void consume() throws IOException, TimeoutException {
        //不使用try catch 是因为不想自动关闭channel 和 connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.28.2.22");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        /**
         * 消费模块不需要像kafka一样，进行while循环
         */

        /**
         * delivery back进行消费
         */
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

        /**
         * 定义consumer 进行异步消费
         */
        //define the consumer to process messages from queue asynchronously
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws  IOException{
                String message = new String(body, "UTF-8");
                //process the message
                System.out.println("Consumed by consumer, message is "+ message);

            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);

    }
}
