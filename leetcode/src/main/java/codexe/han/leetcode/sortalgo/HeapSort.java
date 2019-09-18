package codexe.han.leetcode.sortalgo;

import java.util.Arrays;

/**
 * https://www.cnblogs.com/chengxiao/p/6129630.html
 * 堆排序
 * 升序用大顶堆，降序用小顶堆
 * 最好 最坏 平均 复杂度都是 O(N logN)
 * 不稳定的排序算法
 *
 * 大顶堆：每个节点的值都大于或等于其左右孩子节点的值
 * 大顶堆数组：arr[i]>=arr[2i+1] && arr[i]>=arr[2i+2]   2i+1 2i+2就是节点的左右孩子节点
 * 小顶堆：每个节点的值都小于或等于其左右孩子节点的值
 * 小顶堆数组：arr[i]<=arr[2i+1] && arr[i]<=arr[2i+2]
 *
 * 建堆算法复杂度 O(n)
 * 建堆算法是从最后一个非叶子结点开始下溯(即 Max-Heapify操作)
 * 也可以把建堆过程想成先对左子树建堆(T(n/2))，再对右子树建堆(T(n/2))
 * 最后对根下溯(O(lg n))，所以递推式是T(n) = 2*T(n/2) + O(lg n)它的解是 T(n) = O(n)
 *
 */
public class HeapSort {
    public static void main(String[] args) {
        int[] arr = new int[]{3,9,4,6,2};
        headSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void headSort(int[] arr){
        //构建大顶堆
        for(int i=arr.length/2-1;i>=0;i--){//arr.length/2-1是最后一个元素的父节点
            //从第一个非叶子节点开始，从下至上，从右至左调整结构
            adjustHeap(arr,i,arr.length);
        }

        //输出排序 堆顶和最后一个元素交换
        for(int j=arr.length-1;j>0;j--){
            swap(arr,j,0);
            adjustHeap(arr,0,j);
        }
    }

    public static void adjustHeap(int[] arr, int i, int length){
        int tmp = arr[i];
        for(int k=2*i+1;k<length;k=2*k+1){//从i节点的左子节点开始，也就是2i+1处开始
            //找到左右子节点当中的最大值 k指向该节点
            if(k+1<length && arr[k]<arr[k+1]){//如果左子节点小于右子节点，k指向右子节点
                k++;
            }
            //比较确定是否需要交换
            if(arr[k]>tmp){//如果子节点大于父节点，将子节点赋值给父节点（不用进行交换）
                arr[i] = arr[k];
                i=k;//交换以后继续向下交换
            }
            else{
                break;
            }
        }
        arr[i] = tmp;
    }

    public static void swap(int[] arr, int a, int b){
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }
}
