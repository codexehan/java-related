package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.List;

public class escape60 {
    String res = "";
    public String getPermutation(int n, int k) {
        boolean[] visited = new boolean[n];
        backtracking(n,k,new StringBuilder(),new ArrayList<>(),visited);
        return res;
    }
    public void backtracking(int n, int k,StringBuilder tmp, List<String> resList, boolean[]visited){
        if(tmp.length()==n){
            resList.add(tmp.toString());
            if(resList.size()==k){
                res = tmp.toString();
            }
            return;
        }
        for(int i=1;i<=n&&resList.size()<k;i++){
            if(!visited[i-1]){
                visited[i-1] = true;
                tmp.append(i);
                backtracking(n,k,tmp,resList,visited);
                visited[i-1] = false;
                tmp.deleteCharAt(tmp.length()-1);
            }
        }
    }
}
/**
 The set [1,2,3,...,n] contains a total of n! unique permutations.

 By listing and labeling all of the permutations in order, we get the following sequence for n = 3:

 "123"
 "132"
 "213"
 "231"
 "312"
 "321"
 Given n and k, return the kth permutation sequence.

 Note:

 Given n will be between 1 and 9 inclusive.
 Given k will be between 1 and n! inclusive.
 Example 1:

 Input: n = 3, k = 3
 Output: "213"
 Example 2:

 Input: n = 4, k = 9
 Output: "2314"
 */

class OfficialAnswer{
    public String getPermutation(int n, int k) {
        if (n == 0)
            return "";
        int[] perm = new int[n];
        perm[0] = 1;
        for (int i=1; i<n; i++)
            perm[i] = i*perm[i-1];
        boolean[] usage = new boolean[n+1];
        return build(n, k-1, perm, usage);
    }

    public String build(int n, int k, int[] perm, boolean[] usage) {
        if (n == 0)
            return "";

        int order = k/perm[n-1];
        int i = 1;
        for (int cnt = 0; cnt<=order; i++) {
            if (!usage[i])
                ++cnt;
        }
        --i;
        usage[i] = true;
        return String.valueOf(i) + build(n-1, k%perm[n-1], perm, usage);
    }
}
