import java.util.*;

public class KmerHashMap {

    public static Map<String, List<Integer>> findRepeated(String dna, int k) {

        int n = dna.length();
        Map<String, List<Integer>> map = new HashMap<>();

        for (int i = 0; i <= n - k; i++) {
            String kmer = dna.substring(i, i + k);
            map.computeIfAbsent(kmer, x -> new ArrayList<>()).add(i);
        }

        Map<String, List<Integer>> result = new HashMap<>();

        for (var entry : map.entrySet()) {
            if (entry.getValue().size() > 1) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }
}
