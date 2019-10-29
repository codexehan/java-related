package codexe.han.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.transaction.TransactionStatus;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

public class TransactionalMessage {

    public static void main(String[] args) throws InterruptedException, MQClientException {
        TransactionListener transactionListener = new TransactionListenerImpl();

        TransactionMQProducer producer = new TransactionMQProducer("transaction_producer");
        producer.setNamesrvAddr("172.28.10.43:9876");

        //自定义线程池 用来处理check request
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);

        producer.start();


        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 1; i++) {
            try {
                Message msg =
                        new Message("TopicTest1234", tags[i % tags.length], "KEY" + i,
                                ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.sendMessageInTransaction(msg, null);

                ((TransactionSendResult) sendResult).getLocalTransactionState();//获取transaction result

                System.out.printf("%s%n", sendResult);

                Thread.sleep(1);
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 100000; i++) {
            Thread.sleep(1000);
        }
        producer.shutdown();
    }
}





class TransactionListenerImpl implements TransactionListener{

    /**
     * 主要是设置本地事务状态，与业务代码在一个事务中，只要本地事务提交成功，该方法也会提交成功
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String bizUniNO = msg.getUserProperty("bizUniNo");//从消息中获取业务唯一ID

        //调用本地事务 进行数据库操作，操作成功 return LocalTransactionState.COMMIT
        //操作失败 return LocalTransactionState.ROLLBACK
        //无法得知 return LocalTransactionState.UNKNOWN

        return LocalTransactionState.UNKNOW;//这边返回unknown就会永远执行事务消息回查

    }

    /**
     * 主要是告诉rocketMQ提交还是回滚，这边可以自己定义回查次数，根据回查次数返回不同的状态。
     * 当然rocketmq默认的回查次数是5，可配置
     * @param msg
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        Integer status = 0;
        String bizUniNo = msg.getUserProperty("bizUniNo");//从消息中获取业务唯一ID
        System.out.println(msg.toString());
        System.out.println(new String(msg.getBody()));
        System.out.println("recheck transaction result...");
        /**
         TRANSACTION_CHECK_TIMES 可以统计回查次数，回查一直失败的话，建议人工处理
         事务执行失败的话，会发送rollback
         回查的可能是，事务执行失败或者执行成功，但是mq没有收到commit 或者 rollback的指示
         所以如果回查一直失败的话，需要人工干预，这也是官方推荐的做法
         */
        return LocalTransactionState.UNKNOW;
    }
}
