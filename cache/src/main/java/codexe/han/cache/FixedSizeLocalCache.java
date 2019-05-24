package codexe.han.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

class DataObject {
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
