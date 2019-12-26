package codexe.han.kafkadatapipeline.流控功能consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

//根据partition进行划分
@Slf4j
public class RecordsPool {
    private List<TopicPartition> topicPartitionList;//每次插入之前，要用topicPartitionMap keyset进行一遍去重
    private Map<TopicPartition, PartitionManager> topicPartitionMap;
    private int currentSize;
    private int topicPartitionPointer;

    private int MAX_PENDING_CONSUMING_TASK=4000;
    private int MAX_OFFSET_GAP = 1000;

    public RecordsPool(int MAX_PENDING_CONSUMING_TASK, int MAX_OFFSET_GAP){
        this.MAX_PENDING_CONSUMING_TASK = MAX_PENDING_CONSUMING_TASK;
        this.MAX_OFFSET_GAP = MAX_OFFSET_GAP;
        this.topicPartitionList = new LinkedList<>();
        this.topicPartitionMap = new HashMap<>();
    }
    public RecordsPool(){
        this.topicPartitionList = new LinkedList<>();
        this.topicPartitionMap = new HashMap<>();
    }

    private synchronized boolean isFull(){
        return this.currentSize>=this.MAX_PENDING_CONSUMING_TASK;
    }

    private synchronized boolean isEmpty(){
        return this.currentSize == 0;
    }

    private synchronized boolean arriveLargestGap(){
        for(Map.Entry<TopicPartition, PartitionManager> entry : this.topicPartitionMap.entrySet()){
            if(entry.getValue().offsetDistance() >= this.MAX_OFFSET_GAP){
                log.info("topic {} partition {} offset distance larger than max offset gap {}, start flow control",entry.getKey().topic(),entry.getKey().partition(),this.MAX_OFFSET_GAP);
                return true;//开启流控
            }
        }
        return false;
    }

    public synchronized boolean needFlowControl(){
        return isFull()||arriveLargestGap();
    }

    public synchronized void put(ConsumerRecords<String,String> records){
        //正常来讲 调用put的时候，需要先判断是否是full，然后在进行下一步
        //但是因为我们再pull线程中，会先进行流控检测，所以在这里不需要判断了
        boolean wasEmpty = isEmpty();//原来是空的
        //根据partition进行分割
        for(TopicPartition tp : records.partitions()){
            List<ConsumerRecord<String,String>> tpRecords = records.records(tp);
            for(ConsumerRecord<String, String> record : tpRecords){
                RecordNode node = new RecordNode(tp,record);

                //初始化topic partition管理内容
                if(!this.topicPartitionMap.containsKey(tp)){
                    this.topicPartitionList.add(tp);
                    this.topicPartitionMap.put(tp, new PartitionManager());
                }

                //insert record node to partition manager
                this.topicPartitionMap.get(tp).insert(node);

                //当前size增大
                this.currentSize++;

            }
        }
        if(wasEmpty){
            notifyAll();
        }
    }

    public synchronized RecordNode get() throws Exception {
        /**
         * 这部分可以进一步优化
         * 优化的思路是 分段锁
         * 根据partition 分段加锁
         */
        while(isEmpty()){
            log.info("records pool is empty, thread {} is blocked", Thread.currentThread().getName());
            wait();
        }
        //round robin get info from partition
        int originPosition = this.topicPartitionPointer;
        //正常来讲，前面进行空判断了，在下面这一步是一定可以获取到node的
        for(;;) {
            TopicPartition topicPartition = this.topicPartitionList.get(this.topicPartitionPointer++);
            PartitionManager partitionManager = this.topicPartitionMap.get(topicPartition);
            RecordNode node = partitionManager.get();
            if (node != null) {
                return node;
            }
            if(this.topicPartitionPointer == this.topicPartitionList.size()){
                this.topicPartitionPointer = 0;
            }
            if(this.topicPartitionPointer == originPosition){
                throw new NullPointerException("Iterate all the partition but cannot get a node, please check your code");
            }
        }
    }

    public synchronized List<RecordNode> get(int size) throws Exception{
        /**
         * 这部分可以进一步优化
         * 优化的思路是 分段锁
         * 根据partition 分段加锁
         */
        if(size<=0) throw new IllegalArgumentException("size must be larger than 0");
        while(isEmpty()){
            log.info("records pool is empty, thread {} is blocked", Thread.currentThread().getName());
            wait();
        }
        //round robin get info from partition
        int originPosition = this.topicPartitionPointer;
        //正常来讲，前面进行空判断了，在下面这一步是一定可以获取到node的
        for(;;) {
            TopicPartition topicPartition = this.topicPartitionList.get(this.topicPartitionPointer++);
            PartitionManager partitionManager = this.topicPartitionMap.get(topicPartition);
            List<RecordNode> nodes = partitionManager.get(size);
            if (nodes != null) {
                return nodes;
            }
            if(this.topicPartitionPointer == this.topicPartitionList.size()){
                this.topicPartitionPointer = 0;
            }
            if(this.topicPartitionPointer == originPosition){
                throw new NullPointerException("Iterate all the partition but cannot get a node, please check your code");
            }
        }
    }

    /**
     * Remove the record finally and update the size
     * @param recordNode
     */
    public synchronized void ack(RecordNode recordNode){
        TopicPartition topicPartition = recordNode.topicPartition;
        PartitionManager partitionManager = this.topicPartitionMap.get(topicPartition);
        partitionManager.remove(recordNode);
        this.currentSize--;
    }

    public synchronized void ack(List<RecordNode> recordNodes){
        //其实在批量get中，取得是同一个topic partition下的record
        for(RecordNode recordNode : recordNodes){
            TopicPartition topicPartition = recordNode.topicPartition;
            PartitionManager partitionManager = this.topicPartitionMap.get(topicPartition);
            partitionManager.remove(recordNode);
            this.currentSize--;
        }
    }

    public synchronized void loadOffset(Map<TopicPartition, OffsetAndMetadata> offsets){
        offsets.clear();
        for(TopicPartition partition : this.topicPartitionMap.keySet()){
            long minOffset = this.topicPartitionMap.get(partition).getMinOffset();
            offsets.put(partition, new OffsetAndMetadata(minOffset));
        }
    }

    //remove partition
    public synchronized void rebalancePartition(){
        //1.modify the topicPartitionList
        //2.modify the topicPartitionMap
    }
}

class RecordNode{
    TopicPartition topicPartition;
    ConsumerRecord consumerRecord;
    RecordNode next;
    RecordNode prev;
    public RecordNode(TopicPartition topicPartition, ConsumerRecord consumerRecord, RecordNode next, RecordNode prev){
        this.topicPartition = topicPartition;
        this.consumerRecord = consumerRecord;
        this.next = next;
        this.prev = prev;
    }
    public RecordNode(TopicPartition topicPartition, ConsumerRecord consumerRecord){
        this.topicPartition = topicPartition;
        this.consumerRecord = consumerRecord;
        this.next = null;
        this.prev = null;
    }
}

@Slf4j
class PartitionManager {

    RecordNode head;
    RecordNode tail;
    RecordNode pointer;//pointer永远指向下一个get要获取的位置
    long maxOffset;//用来记录最大offset的历史记录 默认值是-1

    public PartitionManager(){
        this.head = new RecordNode(null,null);
        this.tail = new RecordNode(null,null);
        this.head.next = this.tail;
        this.tail.prev = this.head;
        this.pointer = this.head;
        this.maxOffset = -1L;
    }

    public synchronized int offsetDistance(){
        if(this.head.next!=this.tail){
            long minOffset = this.head.next.consumerRecord.offset();
            long maxOffset = this.tail.prev.consumerRecord.offset();
            log.info("topic {} partition {} min pending offset is {}, max pending offset is {}", minOffset,maxOffset);
            return (int) (maxOffset - minOffset);
        }
        else{
            return 0;
        }
    }

    public synchronized void insert(RecordNode node){

        node.prev = this.tail.prev;
        node.next = this.tail;

        this.tail.prev.next = node;
        this.tail.prev = node;

        //update offset 因为是末尾插入，所以新插入的值一定是最大的offset
        this.maxOffset = node.consumerRecord.offset();
    }

    public synchronized void remove(RecordNode node){
        RecordNode prevNode = node.prev;
        RecordNode nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
    }

    //get by pointer and won't remove the node
    public synchronized RecordNode get(){
        if(this.pointer==this.head){//第一次获取
            this.pointer = this.head.next;
        }
        if(this.pointer == this.tail){//已经没有内容了
            this.pointer = this.head;//reset the position of pointer
            return null;
        }
        RecordNode node = this.pointer;
        this.pointer = this.pointer.next;
        return node;
    }

    public synchronized List<RecordNode> get(int size){
        if(size<=0) throw new IllegalArgumentException("size must be larger than 0");
        if(this.pointer == this.head){
            this.pointer = this.head.next;
        }
        if(this.pointer == this.tail){
            this.pointer = this.head;
            return null;
        }
        List<RecordNode> recordNodes = new ArrayList<>();
        while(this.pointer != this.tail&&size-->0){
            recordNodes.add(this.pointer);
            this.pointer = this.pointer.next;
        }
        return recordNodes;
    }

    public synchronized long getMinOffset(){
        //两种情况
        //1.list里面有node 那么返回该node的offset
        //2.list里面没有node 那么返回该maxOffset+1 maxOffset默认值是-1
        if(this.head.next!=this.tail){//case 1
            return this.head.next.consumerRecord.offset();
        }
        else{
            return this.maxOffset+1;
        }
    }

}
