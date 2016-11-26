package tao.study.algorithm.sort;

import java.util.Arrays;

/**
 * Created by qinluo.ct on 15/6/30.
 */
public class MergeSort {


    public static Object[] sort(Object[] src){

        Object[] dest = Arrays.copyOfRange(src,0,src.length);
        mergeSort(src,dest,0,src.length);
        return dest;
    }

    private static void mergeSort(Object[] src,
                                  Object[] dest,
                                  int low,
                                  int high) {
        int length = high - low;

        // Insertion sort on smallest arrays
        if (length < 3) {
            print("冒泡:"+low+"->"+high);
            for (int i=low; i<high; i++) {
                print("冒泡:i=" + i);
                for (int j = i; j > low &&
                        ((Comparable) dest[j - 1]).compareTo(dest[j]) > 0; j--)
                    swap(dest, j, j - 1);
            }
            return;
        }

        // Recursively sort halves of dest into src
        int destLow  = low;
        int destHigh = high;
        int mid = (low + high) >>> 1;
        mergeSort(dest, src, low, mid);
        mergeSort(dest, src, mid, high);

//        // If list is already sorted, just copy from src to dest.  This is an
//        // optimization that results in faster sorts for nearly ordered lists.
//        if (((Comparable)src[mid-1]).compareTo(src[mid]) <= 0) {
//            System.arraycopy(src, low, dest, destLow, length);
//            return;
//        }

        // Merge sorted halves (now in src) into dest
        for(int i = destLow, p = low, q = mid; i < destHigh; i++) {
            if (q >= high || p < mid && ((Comparable)src[p]).compareTo(src[q])<=0) {
                dest[i] = src[p++];
                print("merge:" + i);
            }
            else{
                dest[i] = src[q++];
                print("merge:"+i);
            }
        }
        printArray(src);
        printArray(dest);
    }

    /**
     * Swaps x[a] with x[b].
     */
    private static void swap(Object[] x, int a, int b) {
        Object t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    private static void print(String msg){
        System.out.println("[MergeSort]"+msg);
    }

    private static void printArray(Object[] src){
        String msg="";
        for (int i=0;i<src.length;i++){
            msg+=src[i]+",";
        }

        print(msg);
    }
}
