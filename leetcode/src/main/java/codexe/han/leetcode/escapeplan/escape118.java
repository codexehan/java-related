package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class escape118 {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> res = new ArrayList<>();
        res.add(Arrays.asList(1));
        if(numRows == 1) return res;
        Arrays.asList(1);
        res.add(Arrays.asList(1,1));
        int n = 3;
        while(n++ < numRows){
            List<Integer> tmp = new ArrayList<>();
            tmp.add(1);
            List<Integer> prev = res.get(res.size()-1);
            for(int i =1; i < prev.size(); i++){
               tmp.add(prev.get(i-1)+prev.get(i));
            }
            tmp.add(1);

            res.add(tmp);
        }
        return res;
    }
}
