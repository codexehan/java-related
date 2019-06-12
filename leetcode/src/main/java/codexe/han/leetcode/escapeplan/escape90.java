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
        List<List<Integer>> res = new ArrayList<>();
        res.add(new ArrayList<>());
        backtracking(nums,0,res);
        return res;
    }

    public static void backtracking(int[] nums, int start, List<List<Integer>> res){
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
    }
}
