package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class escape90 {
    public static void main(String[] args) {
        int[] nums = new int[]{1,2,2};
        subsetsWithDup(nums);
    }
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> finalRes = new ArrayList<>();
        finalRes.add(new ArrayList<>());
        backtracking(finalRes,new ArrayList<>(),0,nums);
        return finalRes;
    }

    /*public static void backtracking(int[] nums, int start, List<List<Integer>> res){
        if(start<nums.length) {
            if (!(start > 0 && nums[start] == nums[start - 1])||start==0) {
                List<List<Integer>> tmp = new ArrayList<>();
                for (List<Integer> resEntry : res) {
                    List<Integer> resEntryTmp = new ArrayList<>(resEntry);
                    resEntryTmp.add(nums[start]);
                    tmp.add(resEntryTmp);
                }
                res.addAll(tmp);
            }
            backtracking(nums, start + 1, res);
        }
    }*/

    public static void backtracking(List<List<Integer>> finalRes,List<Integer> res, int start, int[] nums){
        for(int i=start;i<nums.length;i++){
            //每次校验相同必须从start后面的开始校验，因为start对应的数字，是必须要加入到结果集中的！！！！！！！
            if(i>start&&nums[i]==nums[i-1]) continue;
            res.add(nums[i]);
            finalRes.add(new ArrayList<>(res));
            backtracking(finalRes,res,i+1,nums);
            res.remove(res.size()-1);
        }
    }
}
