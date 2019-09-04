package codexe.han.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class TagConsumer {
    public static void main(String[] args) {
        try {
            //订单系统消费组
            DefaultMQPushConsumer orderConsumer = new DefaultMQPushConsumer("Order_Data_Syn");
            orderConsumer.subscribe("TagTest","TOPIC_TAG_ALL|TOPIC_TAG_ORD");

            //库存系统消费组
            DefaultMQPushConsumer kuCunConsumer = new DefaultMQPushConsumer("Order_Data_Syn");
            kuCunConsumer.subscribe("TagProducer","TOPIC_TAG_ALL|TOPIC_TAG_CAPACITY");

            //并发消费
            kuCunConsumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    return null;
                }
            });
        } catch (MQClientException e) {
            e.printStackTrace();
        }

    }
}

class TagProducer{
    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("TagProducer");
        producer.setNamesrvAddr("");

        try{
            producer.start();

            String topic = "TagTest";
            for(int i=0;i<10;i++){
                if(i%2==0) {
                    Message msg = new Message(topic, "TOPIC_TAG_ALL", "OrderID001", "Helloworld".getBytes(RemotingHelper.DEFAULT_CHARSET));
                    producer.send(msg);
                }
                else{
                    Message msg = new Message(topic, "TOPIC_TAG_ORD", "OrderID001", "Helloworld".getBytes(RemotingHelper.DEFAULT_CHARSET));
                    producer.send(msg);
                }
            }

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
