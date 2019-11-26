package codexe.han.kafkadatapipeline.流控功能consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class RecordsPool {

    private LinkedList<ConsumerRecord<String, String>> pool;

    private int maxPendingConsumingTask;
    private int maxOffsetGap;
    private AtomicInteger maxOffsetPending;
    private AtomicInteger minOffsetPending;

    public RecordsPool(int size, int maxOffsetGap){
        this.maxPendingConsumingTask = size;

        this.maxOffsetGap = maxOffsetGap;
    }

    public boolean isFull(){
        return this.pool.size()>=this.maxPendingConsumingTask;
    }

    public boolean isEmpty(){
        return this.pool.size() == 0;
    }

    //poll线程才会调用put 所以是单线程的put，这样可以保证各个partition 中的 offset是递增有序的
    public synchronized void put(ConsumerRecords<String, String> records){
        //正常来讲 调用put的时候，需要先判断是否是full，然后在进行下一步
        //但是因为我们再pull线程中，会先进行流控检测，所以在这里不需要判断了
        boolean wasEmpty = isEmpty();//原来是空的
        for(ConsumerRecord<String, String> record : records){
            this.pool.add(record);
        }
        if(wasEmpty){
            notifyAll();
        }
    }

    public synchronized ConsumerRecord
}
