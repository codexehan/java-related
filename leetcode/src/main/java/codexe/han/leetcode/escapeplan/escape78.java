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

    }

    public void backtracking(List<List<Integer>> res, List<Integer> tmp, int[] nums, int start){
        res.addAll(Arrays.asList(tmp));
        for(;start<nums.length-1; start++){
            tmp.
        }
    }
}
