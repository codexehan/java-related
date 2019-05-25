package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class escape66 {
    public int[] plusOne(int[] digits){
        int count=1;
        LinkedList<Integer> res = new LinkedList<>();
        for(int i = digits.length-1; i>=0; i--){
            int sum = digits[i] + count;
            res.push(sum>=10?0:sum);
            count=sum>=10?1:0;
        }
        if(count!=0) res.push(count);
        return res.stream().mapToInt(i->i).toArray();
    }
}
