package codexe.han.leetcode.sortalgo;

import java.util.Arrays;

/**
 * 插入排序
 * 1.从第一个元素开始，该元素可以认为已经被排序
 * 2.取出下一个元素，在已经排序的元素序列中从后向前扫描
 * 3.如果该元素(已排序)大于新元素，将该元素移到下一位置
 * 4.重复步骤3，直到找到已排序的元素小于或者等于新元素的位置
 * 5.将新元素插入到该位置后
 * 6.重复步骤2-5
 *
 * 稳定的
 *
 * 平均时间复杂度 O(n^2)
 */
public class InsertionSort {
    public static void main(String[] args) {
        int[] arr = new int[]{3,9,4,6,2};
        insertionSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void insertionSort(int[] arr){
        for(int i=1;i<arr.length;i++){
            int tmp = arr[i];
            /*for(int j=i-1;j>=-1;j--){
                if(j>=0&&arr[j]>tmp){//j>=0是添加到位置0的判断
                    arr[j+1] = arr[j];
                }
                else{
                    arr[j+1] = tmp;
                    break;
                }
            }*/
            //更加优雅的写法
            for(int j=i;j>=0;j--){
                if(j>0&&arr[j-1]>tmp){
                    arr[j] = arr[j-1];
                }
                else{
                    arr[j] = tmp;
                    break;
                }
            }
        }
    }
}
