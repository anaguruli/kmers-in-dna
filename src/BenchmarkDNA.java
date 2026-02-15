import java.util.*;

public class BenchmarkDNA {

    private static final int WARMUP = 3;
    private static final int RUNS = 5;

    public static void main(String[] args) {

        // DNA lengths
        int[] Ns = {10_000, 50_000, 100_000};

        // K values
        int[] kValues = {10, 50, 200};

        // Dataset generators
        List<String> datasetNames = List.of(
                "Uniform Random",
                "Highly Repetitive",
                "GC-Biased (70%)"
        );

        List<String> datasets = new ArrayList<>();
        for (int N : Ns) {
            datasets.add(DataGeneratorDNA.uniformRandom(N, 42));
            datasets.add(DataGeneratorDNA.repetitiveDNA(N, "ACGTACGT"));
            datasets.add(DataGeneratorDNA.biasedDNA(N, 0.7, 42));
        }

        for (int i = 0; i < datasets.size(); i++) {
            System.out.println("\n===========================================");
            System.out.println("Dataset: " + datasetNames.get(i % 3) + ", N=" + Ns[i / 3]);
            System.out.println("===========================================");

            for (int k : kValues) {
                System.out.println("\n--- K = " + k + " ---");
                benchmarkAll(datasets.get(i), k, Ns[i / 3]);
            }
        }
    }

    private static void benchmarkAll(String dna, int k, int N) {

        KmerFinder brute = KmerBruteForce::findRepeated;
        KmerFinder hash = KmerHashMap::findRepeated;
        KmerFinder sort = KmerSortBased::findRepeated;

        // Run brute force only for small sequences (N <= 20K)
        boolean runBrute = N <= 20_000;

        Map<String, List<Integer>> resultBrute = null;
        if (runBrute) {
            resultBrute = benchmark("BruteForce", dna, k, brute);
        } else {
            System.out.println("BruteForce skipped (N > 20K to avoid OOM/long runtime).");
        }

        Map<String, List<Integer>> resultHash = benchmark("HashMap", dna, k, hash);
        Map<String, List<Integer>> resultSort = benchmark("SortBased", dna, k, sort);

        // Correctness verification
        if (runBrute) verifyEqual(resultBrute, resultHash);
        verifyEqual(resultHash, resultSort);
    }

    private static Map<String, List<Integer>> benchmark(
            String name,
            String dna,
            int k,
            KmerFinder finder) {

        // Warm-up
        for (int i = 0; i < WARMUP; i++)
            finder.findRepeated(dna, k);

        long total = 0;
        Map<String, List<Integer>> result = null;

        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            result = finder.findRepeated(dna, k);
            long end = System.nanoTime();
            total += (end - start);
        }

        double avgMs = (total / RUNS) / 1_000_000.0;

        // Memory measurement
        Runtime rt = Runtime.getRuntime();
        rt.gc();
        long memory = rt.totalMemory() - rt.freeMemory();

        System.out.printf("%-12s avg(ms): %-10.3f repeated: %-8d memory(MB): %.2f%n",
                name,
                avgMs,
                result.size(),
                memory / (1024.0 * 1024.0));

        return result;
    }

    /**
     * Verifies two K-mer maps are equal:
     * - Same keys
     * - Same positions for each K-mer (ascending)
     * Handles duplicates and ordering differences.
     */
    private static void verifyEqual(
            Map<String, List<Integer>> a,
            Map<String, List<Integer>> b) {

        if (!a.keySet().equals(b.keySet())) {
            throw new RuntimeException("Mismatch in K-mer keys!");
        }

        for (String kmer : a.keySet()) {
            List<Integer> listA = new ArrayList<>(new HashSet<>(a.get(kmer)));
            List<Integer> listB = new ArrayList<>(new HashSet<>(b.get(kmer)));

            Collections.sort(listA);
            Collections.sort(listB);

            if (!listA.equals(listB)) {
                throw new RuntimeException(
                        "Mismatch in positions for K-mer: " + kmer);
            }
        }
    }

    interface KmerFinder {
        Map<String, List<Integer>> findRepeated(String dna, int k);
    }
}
