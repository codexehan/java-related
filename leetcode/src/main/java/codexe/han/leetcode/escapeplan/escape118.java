package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class escape118 {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> res = new ArrayList<>();
        if(numRows == 0) return res;
        res.add(Arrays.asList(1));
        if(numRows == 1) return res;
        Arrays.asList(1);
        res.add(Arrays.asList(1,1));
        int n = 3;
        while(n++ <= numRows){
            /**
             * 不需要每次都创建新的tmp数组，可以直接使用一个 每次都从头覆盖，这样的话，因为新的数值肯定比上一次要多，不用担心重复
             */
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
    public List<List<Integer>> generateBest(int numRows) {
        List<List<Integer>> allrows = new ArrayList<List<Integer>>();
        ArrayList<Integer> row = new ArrayList<Integer>();
        for(int i=0;i<numRows;i++)
        {
            row.add(0, 1);
            for(int j=1;j<row.size()-1;j++)
                row.set(j, row.get(j)+row.get(j+1));
            allrows.add(new ArrayList<Integer>(row));
        }
        return allrows;
    }
}
