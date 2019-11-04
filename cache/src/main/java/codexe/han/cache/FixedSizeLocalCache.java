package codexe.han.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

class DataObject {
    /**
     窗口缓存占用总大小的1%左右，主缓存占用99%。Caffeine可以根据工作负载特性动态调整窗口和主空间的大小，如果新增数据频率比较高，大窗口更受欢迎;如果新增数据频率偏小，
     小窗口更受欢迎。主缓存内部包含两个部分，一部分为Protected，用于存比较热的数据，它占用主缓存80%空间；另一部分是Probation，用于存相对比较冷的数据，占用主缓存20%空间，数据可以在这两部分空间里面互相转移。

     缓存淘汰的过程：新添加的数据首先放入窗口缓存中，如果窗口缓存满了，则把窗口缓存淘汰的数据转移到主缓存Probation区域中。如果这时主缓存也满了，
     则从主缓存的Probation区域淘汰数据，把这条数据称为受害者，从窗口缓存淘汰的数据称为候选人。接下来候选人和受害者进行一次pk，来决定去留。pk的方式是通过TinyLFU记录的访问频率来进行判断，具体过程如下：

     首先获取候选人和受害者的频率
     如果候选人大于受害者，则淘汰受害者
     如果候选人频率小于等于5，则淘汰候选人
     其余情况随机处理。

     */
    private final String data;

    public DataObject(String data){
        this.data = data;
    }

    private static int objectCounter = 0;
    // standard constructors/getters

    public static DataObject get(String data) {
        objectCounter++;
        return new DataObject(data);
    }
}
public class FixedSizeLocalCache {
    public static void main(String[] args) {
        Cache<String, Object> cache = Caffeine.newBuilder()
                .maximumSize(10)
                .build();
        for(int i = 0;i<100; i++) {
            cache.put(String.valueOf(i), new DataObject(String.valueOf(i)));
            if(i>10 ){
                cache.cleanUp();
                System.out.println("-------------------------------");
                System.out.println(cache.asMap().keySet());
            }
        }
    }
}
