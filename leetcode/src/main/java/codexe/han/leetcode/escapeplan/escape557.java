package codexe.han.leetcode.escapeplan;

//找到每个单词，对每个单词调用reverse(收尾两指针)
public class escape557 {
    public String reverseWords(String s) {
        int len = s.length();
        char[] a = s.toCharArray();
        int i = 0, j = 0;
        for(; i < len; i++){
            if(a[i] == ' '){
                reverse(a, j, i-1);
                j = i+1;
            }
        }
        reverse(a, j, i-1);
        return new String(a);
    }
    public void reverse(char[] a, int start, int end){
        while(start < end){
            char temp = a[start];
            a[start] = a[end];
            a[end] = temp;
            start++;
            end--;
        }
    }
}
