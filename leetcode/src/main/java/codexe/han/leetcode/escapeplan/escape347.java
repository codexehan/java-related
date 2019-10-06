package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * hashmap 统计频率
 * bucket创建，统计前k个
 */
public class escape347 {
    public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for(int n:nums){
            frequencyMap.put(n,frequencyMap.getOrDefault(n,0)+1);
        }
        List<Integer>[] frequencyBucket = new ArrayList[nums.length+1];//最大频率是nums.length
        for(Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()){
            if(frequencyBucket[entry.getValue()]==null){
                frequencyBucket[entry.getValue()] = new ArrayList();
            }
            frequencyBucket[entry.getValue()].add(entry.getKey());
        }
        List<Integer> res = new ArrayList<>();
        for(int i=frequencyBucket.length-1;i>=0&&k>0;i--){
            if(frequencyBucket[i]!=null) {
                for(Integer j : frequencyBucket[i]) {
                    res.add(j);
                    k--;
                }
            }
        }
        return res;
    }
}
