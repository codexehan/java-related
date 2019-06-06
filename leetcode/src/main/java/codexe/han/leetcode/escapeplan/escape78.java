package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 回溯
 * 剪枝条件，只匹配当前index 后面的index，向后匹配
 *
 */
public class escape78 {
    public List<List<Integer>> subsets(int[] nums){
        List<List<Integer>> res = new ArrayList<>();
        res.add(new ArrayList<>());
        backtracking(res,new ArrayList<>(),nums,0);
        return res;
    }

    public void backtracking(List<List<Integer>> res, List<Integer> tmp, int[] nums, int start){
        for(;start<nums.length; start++){
            tmp.add(nums[start]);
            res.add(new ArrayList<>(tmp));
            backtracking(res,tmp,nums,start+1);
            tmp.remove(tmp.size()-1);
        }
    }
}
