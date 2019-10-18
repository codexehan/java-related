package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class escape350 {
    public int[] intersect(int[] nums1, int[] nums2) {
        List<Integer> resList = new ArrayList<>();
        Map<Integer, Integer> countMap = new HashMap<>();
        for(int n:nums1){
            countMap.put(n,countMap.getOrDefault(n,0)+1);
        }
        for(int n:nums2){
            int exist = countMap.getOrDefault(n,0);
            if(exist>0){
                resList.add(n);
                countMap.put(n,exist-1);
            }
        }
        return resList.stream().mapToInt(i->i).toArray();
    }
}
