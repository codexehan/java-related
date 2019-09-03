package codexe.han.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SimpleBatchProducer {
    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("BatchProducerGroupName");
        producer.setNamesrvAddr("");//每隔30s同步一次

        try {
            producer.start();
            String topic = "BatchTest";
            List<Message> messages = new ArrayList<>();

            messages.add(new Message(topic,"Tag","OrderID001","Hello world1".getBytes()));
            messages.add(new Message(topic,"Tag","OrderID001","Hello world2".getBytes()));
            messages.add(new Message(topic,"Tag","OrderID001","Hello world3".getBytes()));

            System.out.println(producer.send(messages));

            producer.shutdown();
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 默认的负载均衡的发送算法是轮询策略，如果需要id相同的，发送到同一个丢列中，需要进行自定义分发
 */

class CustomBatchProducer{
    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("BatchProducerGroupName");
        producer.setNamesrvAddr("");

        try{
            producer.start();
            String topic = "BatchTest";

            String[] tags = new String[]{"TagA","TagB","TagC","TagD","TagE"};
            for(int i=0;i<100;i++){
                int orderId = i%10;
                Message msg = new Message("TopicTestjjj",tags[i%tags.length],"KEY"+i, ("Hellp RocketMQ "+i).getBytes(RemotingHelper.DEFAULT_CHARSET));

                SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Integer id = (Integer)arg;
                        int index = id%mqs.size();
                        return mqs.get(index);
                    }
                },orderId);

                System.out.printf("%s%n",sendResult);
            }
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        }
    }
}
