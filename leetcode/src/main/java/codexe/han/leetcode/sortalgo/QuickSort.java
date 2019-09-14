package codexe.han.leetcode.sortalgo;

import java.util.Arrays;

/**
 * 选一个base,比base小的移动到base左边，比base大的移动到右边
 *
 * 然后左右指针相遇的地方，和base进行替换
 * 从而将数组分成两部分，再对各个部分进行快速排序
 *
 * 平均O(Nlog(N))
 * 最坏O(n^2)
 * 不稳定的
 *
 * l 和 r指针问题，一个是一定小于等于base  一个是一定大于等于base
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] arr = new int[]{3,9,6,4,6,2};
        quickSort(arr,0,arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    public static void quickSort(int[] arr, int left, int right){
        if(left<right){
            int base = arr[left];
            //l和r指向的一定是小于等于base的 r指向的一定是大于等于base的
            int l=left;//从基准位置开始
            int r=right;
            while(l<r){
                //因为l 和 r相遇以后，要和基数进行交换，所以，如果基数选择了第一个，就需要进行替换，因为要替换到基数左边，
                // 所以必须先动右边，以保证交换数字小于基数
                while(l<r&&arr[r]>=base){
                    r--;
                }
                while(l<r&&arr[l]<=base){
                    l++;
                }
                if(l<r){
                    int tmp = arr[r];
                    arr[r] = arr[l];
                    arr[l] = tmp;
                    //一定不要在这个位置移动指针，这样会出现bug！！！！！
                   /* l++;
                    r--;*/
                }
            }
            arr[left]=arr[l];
            arr[l]=base;
   //         System.out.print(Arrays.toString(arr));
            quickSort(arr, left, l-1 );
            quickSort(arr, l+1, right);
        }
    }
}
