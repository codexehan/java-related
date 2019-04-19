package codexe.han.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkQueues {

    /**
     * Fair dispatch and durable
     */
    /**
     * 多个worker 消费同一个queue
     * 异步处理time-consuming工作
     */

    /**
     * 消息分发模式
     * 默认的消息分发模式是Round-robin 无差别的，next message 对应 next worker、这样会导致有的worker一直很忙
     * channel.basicQos(1);设置公平的消息分发模式，处理完一个在处理下一个
     */

    /**
     * 消息确认机制
     * autoAck=true, 不手动发送ack,一旦消息发送，rabbitmq就会立即删除该消息
     * atutoAck=false 手动发送ack，worker没有返回确认的话，rabbitmq会requeue这条信息，如果有其他人的consumer在线，会立即转发给其他人
     * try{
     *     doWork(message);
     * } finally{
     *     channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);//手动发送消息确认
     * }
     */

    /**
     * 持久化消息
     * 如果一个queue已经创建，不可以通过参数配置再去修改持久化消息
     * 持久化消息也不可能保证消息百分百不丢失，以为写磁盘的时候，还是有可能会有一个窗口时间，丢失消息，这种情况下，就要是在publish的时候也添加ack，确认写入磁盘了在发送
     * 消息未被消费，会被持久化到磁盘，如果消息已经被消费，rabbitmq会将其删除
     */

}

class SendWorkQueues{
    private final static String QUEUE_NAME = "hello_durable";

    public static void main(String[] args) {
        String host = "172.28.2.22";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);


        try(Connection connection = factory.newConnection()){
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME,true,false,false,null);//durable Queue

            String message = "Hello World....";
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("Sent " + message);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
class RecvWorkQueues{
    private volatile boolean exit = false;
    private final static String QUEUE_NAME = "hello_durable";

    public static void main(String[] args) throws IOException, TimeoutException {

        //不使用try catch 是因为不想自动关闭channel 和 connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.28.2.22");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            try {
                for (char ch : message.toCharArray()) {
                    if (ch == '.') {
                        Thread.sleep(1000);
                    }
                }
            }catch(Exception e){
                System.out.println("consumer error");
                e.printStackTrace();
            } finally {
                System.out.println("envelope delivery tag "+delivery.getEnvelope().getDeliveryTag());
                try {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
        /**
         * 定义consumer 进行异步消费
         */
        //define the consumer to process messages from queue asynchronously
       /* Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws  IOException{
                String message = new String(body, "UTF-8");
                //process the message
                System.out.println("Consumed by consumer, message is "+ message);
                try {
                    for (char ch : message.toCharArray()) {
                        if (ch == '.') {
                            Thread.sleep(1000);
                        }
                    }
                }catch(Exception e){
                    System.out.println("consumer error");
                    e.printStackTrace();
                } finally {
                    System.out.println("envelope delivery tag "+envelope.getDeliveryTag());
                    try {
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

            }
        };
        channel.basicConsume(QUEUE_NAME, false, consumer);*/
    }
}
