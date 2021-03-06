package parallelhyflex.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author kommusoft
 */
public final class ProbabilityUtils {

    /**
     *
     * @param <T>
     * @param list
     * @return
     */
    public static <T> T randomElement(List<T> list) {
        return list.get(Utils.nextInt(list.size()));
    }

    /**
     *
     * @param D
     * @return an Integer in the interval [0,D[
     */
    public static int integerFromBenfordDistribution(int D) {
        return (int) Math.floor(Math.pow(D + 1, Utils.nextDouble())) - 1;
    }

    /**
     *
     * @param D
     * @return an Integer in the Interval [0,D[
     */
    public static int integerFromUniformDistribution(int D) {
        return (int) Math.floor(D * Utils.nextDouble());
    }

    /**
     *
     * @param weights
     */
    public static void unnormalizedWeightsToCDF(double[] weights) {
        double suminv = 0.0d;
        double min = 0.0d;
        for (double w : weights) {
            min = Math.min(w, min);
            suminv += w;
        }
        suminv -= min * weights.length;
        suminv = 1 / suminv;
        double c = 0.0d;
        for (int i = 0; i < weights.length; i++) {
            c += (weights[i] - min) * suminv;
            weights[i] = c;
        }
    }

    /**
     *
     * @param cdf
     * @param without
     * @return
     */
    public static int randomIndexFromCDF(double[] cdf, Collection<Integer> without) {
        int index;
        do {
            index = randomIndexFromCDF(cdf);
        } while (without.contains(index));
        return index;
    }

    /**
     *
     * @param cdf
     * @return
     */
    public static int randomIndexFromCDF(double[] cdf) {
        double rand = Utils.nextDouble();
        int index = Arrays.binarySearch(cdf, rand);
        if (index < 0) {
            index = ~index;
        }
        return index;
    }

    /**
     *
     * @param <T>
     * @param set
     * @return
     */
    public static <T> T randomElement(Collection<T> set) {
        int I = Utils.nextInt(set.size());
        Iterator<T> iterator = set.iterator();
        for (int i = 0; i < I; i++) {
            iterator.next();
        }
        return iterator.next();
    }

    /**
     *
     * @param values
     * @return
     */
    public static int randomElement(int[] values) {
        return values[Utils.nextInt(values.length)];
    }

    public static int randomIndexFromPDF(List<Double> pdf) {
        int index = -0x01;
        double val = Utils.nextDouble();
        for (Iterator<Double> pi = pdf.iterator(); val >= 0.0d && pi.hasNext(); index++) {
            val -= pi.next();
        }
        return index;
    }

    /**
     *
     * @param pdf
     * @param maxn
     * @return
     */
    public static int randomBoundedIndexFromPDF(List<Double> pdf, int maxn) {
        int index = -0x01;
        double val = Utils.nextDouble();
        for (Iterator<Double> pi = pdf.iterator(); val >= 0.0d && pi.hasNext() && index < maxn; index++) {
            val -= pi.next();
        }
        return Math.max(index, 0x00);
    }

    public static <T> void shuffle(List<T> list, int sublength) {
        T temp;
        sublength = Math.min(sublength, list.size());
        for (int i = 0; i < sublength; i++) {
            int j = Utils.StaticRandom.nextInt(sublength);
            temp = list.get(j);
            list.set(j, list.get(i));
            list.set(i, temp);
        }
    }

    public static <T> void shuffle(List<T> list) {
        shuffle(list, list.size());
    }

    private ProbabilityUtils() {
    }
}
