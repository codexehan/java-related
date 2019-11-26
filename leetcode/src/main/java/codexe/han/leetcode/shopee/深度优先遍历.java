package codexe.han.leetcode.shopee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class 深度优先遍历 {
    public static void main(String[] args) {
        List<List<String>> inputList = new ArrayList<>();
        inputList.add(Arrays.asList("ptt","abc"));
        inputList.add(Arrays.asList("I"));
        inputList.add(Arrays.asList("no"));
        System.out.println(permutation(inputList,2));
    }

    public static List<List<String>> permutation(List<List<String>> inputList, int k){
        List<List<String>> res = new ArrayList<>();
        dfs(res,inputList,k,new ArrayList<>(),0);
        return res;
    }
    public static void dfs(List<List<String>> res, List<List<String>>inputList, int k, List<String> tmpRes, int start) {
        if (tmpRes.size() == k) {
            res.add(new ArrayList<>(tmpRes));
            return;
        }
        for (int i = start; i < inputList.size(); i++) {
            for (String ele : inputList.get(i)) {
                tmpRes.add(ele);
                dfs(res, inputList, k, tmpRes, i + 1);
                tmpRes.remove(ele);
            }
        }
    }
}
