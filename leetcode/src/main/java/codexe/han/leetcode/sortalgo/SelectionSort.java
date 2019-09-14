package codexe.han.leetcode.sortalgo;

import java.util.Arrays;

/**
 * 选择排序
 * 每次从未排序序列中找到最小的元素，存放到未排序序列的起始位置
 * 稳定的
 * O(n^2)
 */
public class SelectionSort {
    public static void main(String[] args) {
        int[] arr = new int[]{3,9,4,6,2};
        selectSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void selectSort(int[] arr){
        for(int i=0;i<arr.length-1;i++){
            int min = arr[i];
            int minPos = i;
            for(int j=i+1;j<arr.length;j++){
                if(arr[j]<min){
                    min = arr[j];
                    minPos = j;
                }
            }
            //swap
            swap(arr,i,minPos);
        }

    }
    public static void swap(int[] arr, int i, int j){
        if(i!=j){
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }
}
