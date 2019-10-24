package codexe.han.leetcode.escapeplan;

import java.util.*;

/**
 * 滑动窗口
 *
 */
public class escape438 {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        int sLen = s.length(),  pLen = p.length(), j = 0;
        if(sLen == 0 || pLen == 0 || sLen < pLen)
        {
            return result;
        }

        int [] counts = new int[256];

        for(char c : p.toCharArray())
        {
            counts[c]++;
        }

        int counter = pLen;
        int begin = 0;
        int end = 0;
        char [] sc = s.toCharArray();

        while(end < sLen)
        {
            if(counts[sc[end++]]-- > 0)
            {
                counter--;
            }

            while(counter == 0)
            {
                if(counts[sc[begin]]++ == 0)
                {
                    counter++;
                }
                if((end - begin) == pLen)
                {
                    result.add(begin);
                }
                begin++;
            }
        }

        return result;
    }

}
