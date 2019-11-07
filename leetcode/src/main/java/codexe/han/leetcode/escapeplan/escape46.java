package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * [1,2,3]
 * 方法一：
 * 回溯法
 * 剪枝条件 统一路径上，已经出现过的数字不能够再次出现，所以会有一个boolean数组，记录路径上出想过的index
 * 深度优先遍历，部分数据结构可以重复利用
 *
 * 方法二：
 * 按照位置插入
 * 1
 * 2，1  1，2
 */
public class escape46 {
    public static void main(String[] args) {
        permute(new int[]{1,2,3});
    }
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        res.add(new LinkedList<>());
        for(int i =0; i<nums.length; i++){
            List<List<Integer>> resTmp = new ArrayList<>();
            for(List<Integer> tmp : res){
                for(int j=0; j<tmp.size()+1; j++){
                    tmp.add(j, nums[i]);
                    resTmp.add(new LinkedList<>(tmp));
                    tmp.remove(j);
                }
            }
            res = resTmp;

        }
        return res;
    }

    public void backtracking(int[] nums, List<List<Integer>> res, List<Integer>tmp, boolean[] visited){
        if(tmp.size() == nums.length){
            res.add(new ArrayList<>(tmp));
            return;
        }
        for(int i=0;i<nums.length;i++){//深度优先遍历，部分数据机构可以重复利用
            if(!visited[i]){
                visited[i]=true;
                //List<Integer> newTmp = new ArrayList<>(tmp);
                //newTmp.add(nums[i]);
                //backtracking(nums, res, new ArrayList<>(tmp), new boolean[]{visited});//这样做会浪费空间！！！
                tmp.add(nums[i]);
                visited[i]=true;
                backtracking(nums,res,tmp, visited);//节省空间
                tmp.remove(tmp.size()-1);
                visited[i]=false;
            }
        }
    }


}
/**
 Given a collection of distinct integers, return all possible permutations.

 Example:

 Input: [1,2,3]
 Output:
 [
 [1,2,3],
 [1,3,2],
 [2,1,3],
 [2,3,1],
 [3,1,2],
 [3,2,1]
 ]
 */

class OfficalAnswer {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if(nums == null || nums.length == 0) {
            return res;
        }
        helper(nums, new boolean[nums.length], new ArrayList<>(), res);
        return res;
    }
    public void helper(int[] nums, boolean[] visited, List<Integer> subList, List<List<Integer>> res) {

        if(subList.size() == nums.length) {
            res.add(new ArrayList<>(subList));
            return;
        } else {
            for(int i = 0; i < nums.length; i++) {
                if(visited[i]) {
                    continue;
                }
                visited[i] = true;
                subList.add(nums[i]);
                helper(nums, visited, subList, res);
                subList.remove(subList.size() - 1);
                visited[i] = false;
            }
        }

    }
}
