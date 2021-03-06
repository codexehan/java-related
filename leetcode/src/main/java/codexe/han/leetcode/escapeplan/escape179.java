package codexe.han.leetcode.escapeplan;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 两个数字相连
 *
 * 比较大小的话，就是比较 s1+s2 和 s2+s1 的大小
 */
public class escape179 {
    public String largestNumber(int[] nums) {
        if(nums==null || nums.length == 0){
            return "";
        }

        int len = nums.length;
        String[] str = new String[len];
        int i = 0;
        for(int n : nums){
            str[i] = String.valueOf(n);
            i++;
        }

        Arrays.sort(str, new Comparator<String>(){
            public int compare(String s1, String s2){
                return (s2+s1).compareTo(s1+s2);
            }
        });

        if(str[0].equals("0")){
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        for(String s : str){
            sb.append(s);
        }

        return sb.toString();
    }
}

/**
 Given a list of non negative integers, arrange them such that they form the largest number.

 Example 1:

 Input: [10,2]
 Output: "210"
 Example 2:

 Input: [3,30,34,5,9]
 Output: "9534330"
 Note: The result may be very large, so you need to return a string instead of an integer.
 */
