// Similiar to Counting Sort, ARU Counting Sort is for non-negative integers.
// 
// m is the ceiling of the square root of k; k is the max value being sorted
// Counts occurrences of each possible quotient and remainder value when divided by m (0 to m). 
// Sorts values by their remainder's when divided by m into auxiliary array B 
// Iterating through auxiliary array B, this algorithm then sorts values by their 
// quotient (when divided by m), placing values back into array A, thus accounting 
// for both the quotient and remainder when the original value is divided by m.  
// 
// Time Complexity: O(n+√k)
// Space Complexity: 2n + 2√k 
// Stable sort.
public class ARUCountingSort {
    public static void sort(int[] arr) {
        int n = arr.length;
        double k = max(arr);
        int m = (int)Math.ceil(Math.sqrt(k));

        // Initiate Q and R to have all 0’s, with a length of √k 
        // Added 1 to account for when k = m^2; it's quotient corresponds to index m in Q
        int mm = k == m*m ? m + 1 : m;
        int Q[] = new int[mm];
        int R[] = new int[mm];
        for (int i = 0; i < mm; i++) {
            Q[i] = 0;
            R[i] = 0;
        }

        // Update Q and R store the count for the quotient and remainder
        // of values when divided by m
        for (int i = 0; i < n; i++) {
            Q[arr[i]/m] = Q[arr[i]/m] + 1;
            R[arr[i]%m] = R[arr[i]%m] + 1;
        }

        // Update Q and R to store the cummulative count; Q[m] = n
        for (int i = 1; i < mm; i++) {
            Q[i] = Q[i] + Q[i-1] ;
            R[i] = R[i] + R[i-1] ;
        }

        // Array B stores the values sorted by the size of their remainder when divided by m
        // Account for values having the same remainder using array R
        // Ex) [4, 5, 4, 1, 9] becomes [9, 4, 4, 1, 5]
        int d;
        int B[] = new int[n];
        for (int i = n-1; i >=0; i--) {
            d = arr[i]%m; 
            R[d] = R[d] - 1; 
            B[R[d]] = arr[i];
        }

        // A stores the sorted array; Stable through walking backwards
        // Iterated through Array B and check how many values are before the given value by quotient
        for (int i = n-1; i >=0; i--) {
            d = B[i]/m; 
            Q[d] = Q[d] - 1; 
            arr[Q[d]] = B[i];
        }
    }

    static int max(int[] arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
}