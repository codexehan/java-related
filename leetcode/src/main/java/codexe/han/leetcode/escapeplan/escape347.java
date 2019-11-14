package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * hashmap 统计频率
 * bucket创建，统计前k个
 * bucket的最大长度是nums.length，这种情况是所有的数字都相等的时候，才会出现
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

/**
 Given a non-empty array of integers, return the k most frequent elements.

 Example 1:

 Input: nums = [1,1,1,2,2,3], k = 2
 Output: [1,2]
 Example 2:

 Input: nums = [1], k = 1
 Output: [1]
 Note:

 You may assume k is always valid, 1 ≤ k ≤ number of unique elements.
 Your algorithm's time complexity must be better than O(n log n), where n is the array's size.
 */
