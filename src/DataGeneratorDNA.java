import java.util.Random;

public class DataGeneratorDNA {

    private static final char[] NUCLEOTIDES = {'A', 'C', 'G', 'T'};

    public static String uniformRandom(int n, long seed) {
        Random r = new Random(seed);
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++)
            sb.append(NUCLEOTIDES[r.nextInt(4)]);
        return sb.toString();
    }

    public static String repetitiveDNA(int n, String pattern) {
        StringBuilder sb = new StringBuilder(n);
        while (sb.length() < n) sb.append(pattern);
        return sb.substring(0, n);
    }

    public static String biasedDNA(int n, double gcFraction, long seed) {
        Random r = new Random(seed);
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            double x = r.nextDouble();
            if (x < gcFraction)
                sb.append(r.nextBoolean() ? 'G' : 'C');
            else
                sb.append(r.nextBoolean() ? 'A' : 'T');
        }
        return sb.toString();
    }
}
