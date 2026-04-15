import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Benchmark {

    // Paper's experimental setup: fix n, vary k (max element value)
    static final int[] N_VALUES = {1_000, 10_000, 100_000, 1_000_000};
    static final int[] K_VALUES = {999, 9_999, 99_999, 999_999, 9_999_999, 99_999_999, 999_999_999};

    static final int RUNS = 5;
    static final long SEED = 42;

    public static void main(String[] args) throws IOException {
        PrintWriter csv = new PrintWriter(new FileWriter("results.csv"));
        csv.println("algorithm,n,k,time_ms");

        for (int n : N_VALUES) {
            for (int k : K_VALUES) {
                // Skip k values smaller than n (paper starts k at roughly n)
                if (k < n - 1) continue;

                System.out.printf("%nn = %d, k = %d%n", n, k);
                int[] base = randomArray(n, k);

                run("MergeSort",    n, k, base, MergeSort::sort,    csv);
                run("QuickSort",    n, k, base, QuickSort::sort,    csv);
                run("CountingSort", n, k, base, CountingSort::sort, csv);
                run("RadixSort",    n, k, base, RadixSort::sort,    csv);
                run("ARUCountingSort", n, k, base, ARUCountingSort::sort, csv);
            }
        }

        csv.close();
        System.out.println("\nResults written to results.csv");
    }

    static void run(String name, int n, int k, int[] base, Consumer<int[]> algo, PrintWriter csv) {
        long ns = time(base, algo);
        double ms = ns / 1e6;
        System.out.printf("  %-20s %.4f ms%n", name, ms);
        csv.printf("%s,%d,%d,%.4f%n", name, n, k, ms);
    }

    static long time(int[] base, Consumer<int[]> algo) {
        // warmup
        algo.accept(Arrays.copyOf(base, base.length));

        long total = 0;
        for (int i = 0; i < RUNS; i++) {
            int[] copy = Arrays.copyOf(base, base.length);
            long start = System.nanoTime();
            algo.accept(copy);
            total += System.nanoTime() - start;
        }
        return total / RUNS;
    }

    static int[] randomArray(int n, int k) {
        Random rng = new Random(SEED);
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = rng.nextInt(k + 1);
        return arr;
    }
}
