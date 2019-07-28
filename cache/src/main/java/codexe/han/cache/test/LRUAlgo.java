package codexe.han.cache.test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUAlgo<K,V> extends LinkedHashMap<K,V> {
    private int CACHE_SIZE;
    public LRUAlgo(int cacheSize){
        super(cacheSize,0.75f,true);
        this.CACHE_SIZE = cacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest){
        return size()>CACHE_SIZE;
    }

    public static void main(String[] args) {
        LRUAlgo lruAlgo = new LRUAlgo(5);
        lruAlgo.put("1",1);
        lruAlgo.put("2",1);
        lruAlgo.put("3",1);
        lruAlgo.put("4",1);
        lruAlgo.put("5",1);
        lruAlgo.put("6 ",1);
        for(Iterator<Map.Entry<Integer,Integer>> it = lruAlgo.entrySet().iterator(); it.hasNext();){
            System.out.println(it.next().getKey());
        }
        System.out.println("----------");
        lruAlgo.get("4");
        for(Iterator<Map.Entry<Integer,Integer>> it = lruAlgo.entrySet().iterator(); it.hasNext();){
            System.out.println(it.next().getKey());
        }

    }
}
