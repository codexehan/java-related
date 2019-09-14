package codexe.han.leetcode.sortalgo;

import java.util.Arrays;

/**
 * 冒泡排序
 * 从后向前，比较大小，交换位置
 *
 * O(n^2)
 */
public class BubbleSort {
    public static void main(String[] args) {
        int[] arr = new int[]{3,9,4,6,2};
        bubbleSort(arr);
        System.out.println(Arrays.toString(arr));
    }
    public static void bubbleSort(int[] arr){
        for(int i=0;i<arr.length;i++){
            for(int j=arr.length-1;j>i;j--){
                if(arr[j]<arr[j-1]){
                    swap(arr,j,j-1);
                }
            }
        }
    }
    public static void swap(int[] arr, int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
