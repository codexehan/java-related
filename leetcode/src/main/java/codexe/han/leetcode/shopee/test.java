package codexe.han.leetcode.shopee;

import java.util.*;

public class test {
    public static void main(String[] args) {
       // System.out.println(codingTestShopee("abbabab","ab"));
        new test().codingTestShopee("","");
    }
    public List<Integer> codingTestShopee(String s, String p) {
        if(s==null||s.length()==0||p==null||p.length()==0) return new ArrayList<>();
        List<Integer> res = new ArrayList<>();
        Map<Character, Integer> map  = new HashMap<>();
        for(char ch : p.toCharArray()){
            map.put(ch, map.getOrDefault(ch,0)+1);
        }
        int counter = map.size();//unique characters in p

        int begin = 0, end = 0;
        while(end<s.length()){
            char chEnd = s.charAt(end);
            if(map.containsKey(chEnd)){
                if(map.get(chEnd)-1==0) counter--;//amount of chEnd become 0 means we already found encough chEnd -> counter need minus 1
                map.put(chEnd,map.get(chEnd)-1);
            }
            end++;

            //we find substring in (begin, end) where p's characters are all contained in it, but could contain other characters compared with p
            //because check begin will only occured when we found all characters in p, it will save much time
            //this step will make us only need to check any character in the string at most twice
            //time complexity is O(2n)->O(n) suppose s.length() is n
            //space complexity is O(k) suppose p.length is k
            while(counter==0){
                char chBegin = s.charAt(begin);
                if(map.containsKey(chBegin)){//if the chBegin is the char in p
                    map.put(chBegin,map.get(chBegin)+1);
                    if(map.get(chBegin)>0)counter++;//
                }
                if((end-begin)==p.length()){
                    res.add(begin);
                }
                //if counter is still 0, means chBegin is not character in p
                begin++;
            }
        }
        return res;
    }

    public List<Integer> codingTest(String s, String p){
        List<Integer> res = new ArrayList<>();
        int[] map = new int[26];
        for(int i = 0; i < p.length(); i++) map[p.charAt(i) - 'a']++;

        int j = 0;
        for(int i = 0; i < s.length(); i++) {
            map[s.charAt(i) - 'a']--;
            //find next window
            while(map[s.charAt(i) - 'a'] < 0) map[s.charAt(j++) - 'a']++;
            if(i - j + 1 == p.length()) res.add(j);
        }

        return res;
    }
}
