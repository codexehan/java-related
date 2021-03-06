package codexe.han.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class HelloRocketMq {
    public static void main(String[] args) {

    }

}
class SendMsgSync{
    public static void main(String[] args) {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer("hello_rocket_mq");
        // Specify name server addresses.
        producer.setNamesrvAddr("172.28.10.43:9876");
        MessageQueueSelector messageQueueSelector  = new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                System.out.println(mqs);
                return mqs.get(0);
            }
        };
        //Launch the instance.
        try {
            producer.start();
            for (int i = 0; i < 10; i++) {
                //Create a message instance, specifying topic, tag and message body.
                Message msg = new Message("TopicTest3" /* Topic */,
                        "TagA" /* Tag */,
                        ("Hello RocketMQ " +
                                i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );
                //Call send message to deliver message to one of brokers.
                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n", sendResult);
            }
            //Shut down once the producer instance is not longer in use.
            producer.shutdown();
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        }
    }
}

class SendMsgAsync{
    public static void main(String[] args) throws RemotingException, InterruptedException {
//Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("hello_rocket_mq");
        // Specify name server addresses.
        producer.setNamesrvAddr("172.28.10.43:9876");
        try {
            //Launch the instance.
            producer.start();
            producer.setRetryTimesWhenSendAsyncFailed(0);
            MessageQueueSelector messageQueueSelector  = new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    System.out.println(mqs);
                    return mqs.get(0);
                }
            };
            for (int i = 0; i < 100; i++) {
                final int index = i;
                //Create a message instance, specifying topic, tag and message body.
                Message msg = new Message("TopicTest",
                        "TagA",
                        "OrderID188",
                        "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));

                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.printf("%-10d OK %s %n", index,
                                sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        System.out.printf("%-10d Exception %s %n", index, e);
                        e.printStackTrace();
                    }
                });
            }
            //Shut down once the producer instance is not longer in use.
            producer.shutdown();
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

class Consumer{
    public static void main(String[] args) throws MQClientException {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("hello_rocket_mq");

        consumer.setConsumerGroup("consumer_client");
        // Specify name server addresses.
        consumer.setNamesrvAddr("172.28.10.43:9876");

        // Subscribe one more more topics to consume.
        consumer.subscribe("TopicTest3", "*");//subExpression 消息过滤表达式，TAG或SQL92表达式 还可以使用类的模式进行过滤
        // Register callback to execute on arrival of messages fetched from brokers.
       //并发消费会自动维护一个线程池
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                try {
                    System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }catch(Exception e){
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });

        //Launch the consumer instance.
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}