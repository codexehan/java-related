package codexe.han.leetcode.escapeplan;

public class escape31 {

    public void nextPermutation(int[] A) {
        //从后往前找到第一个递增的位置，并置换
     /*   if(A == null || A.length <= 1) return;
        int i = A.length - 2;
        while(i >= 0 && A[i] >= A[i + 1]) i--; // Find 1st id i that breaks descending order
        if(i >= 0) {                           // If not entirely descending
            int j = A.length - 1;              // Start from the end
            while(A[j] <= A[i]) j--;           // Find rightmost first larger id j
            swap(A, i, j);                     // Switch i and j
        }
        reverse(A, i + 1, A.length - 1);       // Reverse the descending sequence*/
       //解决方案：自己从头写一遍1，2，3，4，5就知道该怎么做了

    }

    public void swap(int[] A, int i, int j) {
        int tmp = A[i];
        A[i] = A[j];
        A[j] = tmp;
    }

    public void reverse(int[] A, int i, int j) {
        while(i < j) swap(A, i++, j--);
    }
}
/**
 * Only open the files when you are prepared to start the problem.
 1 2 3
 1 3 2
 2 1 3
 2 3 1
 3 1 2
 3 2 1
 所以题目的意思是，从上面的某一行重排到期下一行，如果已经是最后一行了，则重排成第一行。

 但是也不能根据给出的数组中的数字列出所有排列，因为要求不能占用额外的空间。
 Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers.

 If such arrangement is not possible, it must rearrange it as the lowest possible order (ie, sorted in ascending order).

 The replacement must be in-place and use only constant extra memory.

 Here are some examples. Inputs are in the left-hand column and its corresponding outputs are in the right-hand column.

 1,2,3 → 1,3,2
 3,2,1 → 1,2,3
 1,1,5 → 1,5,1

 思路：
 首先肯定从后面开始看，1和5调换了没有用。

 7、5和1调换了也没有效果，因此而发现了8、7、5、1是递减的。

 如果想要找到下一个排列，找到递增的位置是关键。

 因为在这里才可以使其增长得更大。

 于是找到了4，显而易见4过了是5而不是8或者7更不是1。

 因此就需要找出比4大但在这些大数里面最小的值，并将其两者调换。

 那么整个排列就成了：6 5 5 8 7 4 1

 然而最后一步将后面的8 7 4 1做一个递增。
 */
