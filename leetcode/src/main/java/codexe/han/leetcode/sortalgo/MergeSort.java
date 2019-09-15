package codexe.han.leetcode.sortalgo;

import java.util.Arrays;

/**
 * https://www.cnblogs.com/chengxiao/p/6194356.html
 * 归并排序
 *
 * O(nlogn)
 *
 * 稳定排序算法
 *
 * 将数组进行划分，然后merge排序
 */
public class MergeSort {
    public static void main(String[] args) {
        int[] arr = new int[]{3,9,4,6,2};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort(int[] arr){
        int[] tmp = new int[arr.length];
        mergeSort(arr,0,arr.length-1,tmp);
    }

    public static void mergeSort(int[] arr, int left, int right, int[] tmp){
        if(left<right){
            int mid = (left+right)/2;
            mergeSort(arr,left,mid,tmp);
            mergeSort(arr,mid+1,right,tmp);
            merge(arr,left,mid,right,tmp);
        }
    }

    public static void merge(int[] arr, int left, int mid, int right, int[] tmp){
        int i = left;
        int j = mid+1;//右序列指针
        int t = 0;//临时数组指针

        while(i<=mid && j<=right){
            if(arr[i]<=arr[j]){
                tmp[t++] = arr[i++];
            }
            else{
                tmp[t++] = arr[j++];
            }
        }

        while(i<=mid){//将左边剩余元素填充进tmp中
            tmp[t++] = arr[i++];
        }
        while(j<=right){
            tmp[t++] = arr[j++];
        }
        t = 0;
        //将tmp中的元素全部拷贝到原数组中
        t=0;
        while(left<=right){
            arr[left++] = tmp[t++];
        }
    }
}
