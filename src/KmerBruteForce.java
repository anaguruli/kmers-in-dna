import java.util.*;

public class KmerBruteForce {

    public static Map<String, List<Integer>> findRepeated(String dna, int k) {

        int n = dna.length();
        Map<String, List<Integer>> result = new HashMap<>();

        for (int i = 0; i <= n - k; i++) {

            for (int j = i + 1; j <= n - k; j++) {

                boolean equal = true;

                for (int t = 0; t < k; t++) {
                    if (dna.charAt(i + t) != dna.charAt(j + t)) {
                        equal = false;
                        break;
                    }
                }

                if (equal) {
                    String kmer = dna.substring(i, i + k);
                    result.computeIfAbsent(kmer, x -> new ArrayList<>()).add(i);
                    result.get(kmer).add(j);
                }
            }
        }

        result.values().forEach(Collections::sort);
        return result;
    }
}
