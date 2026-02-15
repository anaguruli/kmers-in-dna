import java.util.*;

public class KmerSortBased {

    static class KmerPos {
        String kmer;
        int pos;

        KmerPos(String kmer, int pos) {
            this.kmer = kmer;
            this.pos = pos;
        }
    }

    public static Map<String, List<Integer>> findRepeated(String dna, int k) {

        int n = dna.length();
        List<KmerPos> list = new ArrayList<>();

        // Generate all K-mers with positions
        for (int i = 0; i <= n - k; i++) {
            list.add(new KmerPos(dna.substring(i, i + k), i));
        }

        // Sort lexicographically by K-mer
        list.sort(Comparator.comparing(o -> o.kmer));

        Map<String, List<Integer>> result = new HashMap<>();

        int i = 0;
        while (i < list.size()) {
            String currentKmer = list.get(i).kmer;
            List<Integer> positions = new ArrayList<>();
            positions.add(list.get(i).pos);
            int j = i + 1;

            // Scan contiguous block of duplicates
            while (j < list.size() && list.get(j).kmer.equals(currentKmer)) {
                positions.add(list.get(j).pos);
                j++;
            }

            // Only keep K-mers that appear at least twice
            if (positions.size() > 1) {
                Collections.sort(positions); // ascending order
                result.put(currentKmer, positions);
            }

            i = j; // skip processed block
        }

        return result;
    }
}
