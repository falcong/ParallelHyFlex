package parallelhyflex.problems.threesat;

import java.util.logging.Logger;
import parallelhyflex.problems.threesat.solution.ThreeSatSolution;
import parallelhyflex.utils.CompactBitArray;
import parallelhyflex.utils.ProbabilityUtils;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class ClauseUtils {

    private static final Logger LOG = Logger.getLogger(ClauseUtils.class.getName());

    /**
     *
     * @param i
     * @param cba
     * @param constraints
     * @param tocheck
     * @return
     */
    public static int calculateLoss(Integer i, CompactBitArray cba, long[] constraints, int[] tocheck) {
        int delta, np, nn, j;
        delta = 0;
        np = tocheck[0];
        nn = tocheck.length;
        for (j = 1; j <= np; j++) {
            if (cba.willSwap(constraints[tocheck[j]], i)) {
                delta++;
            }
        }
        for (; j < nn; j++) {
            if (cba.willSwap(constraints[tocheck[j]], i)) {
                delta--;
            }
        }
        delta *= (cba.getBit(i) << 1) - 1;
        return delta;
    }

    /**
     *
     * @param index0
     * @param index1
     * @param index2
     * @return
     */
    public static long generate0Clause(long index0, long index1, long index2) {
        return (index0 << 40) | (index1 << 20) | (index2);
    }

    /**
     *
     * @param index0
     * @param index1
     * @param index2
     * @param value0
     * @param value1
     * @param value2
     * @return
     */
    public static long generateClause(long index0, long index1, long index2, boolean value0, boolean value1, boolean value2) {
        long val = generate0Clause(index0, index1, index2);
        if (value0) {
            val |= 1L << 62;
        }
        if (value1) {
            val |= 1L << 61;
        }
        if (value2) {
            val |= 1L << 60;
        }
        return val;
    }

    /**
     *
     * @param cba
     * @return
     */
    public static long generateTrueClause(CompactBitArray cba) {
        int n = cba.getLength();
        long i0 = Utils.StaticRandom.nextInt(n);
        long i1 = Utils.StaticRandom.nextInt(n);
        long i2 = Utils.StaticRandom.nextInt(n);
        long ia = Math.min(i0, Math.min(i1, i2));
        long ic = Math.max(i0, Math.max(i1, i2));
        long ib = i0 + i1 + i2 - ia - ic;
        long fill = (((long) Utils.StaticRandom.nextInt(8)) << 60) | generate0Clause(ia, ib, ic);
        int ci = Utils.StaticRandom.nextInt(3);
        long index = ClauseUtils.getIndexI(fill, ci);
        fill = ClauseUtils.setValue(fill, ci, cba.getBit(index));
        return fill;
    }

    /**
     *
     * @param cba
     * @param index0
     * @param index1
     * @param index2
     * @return
     */
    public static long generateCompletelyTrueClause(CompactBitArray cba, long index0, long index1, long index2) {
        long fill = generate0Clause(index0, index1, index2);
        for (int i = 0; i < 3; i++) {
            fill = ClauseUtils.setValue(fill, i, cba.getBit(ClauseUtils.getIndexI(fill, i)));
        }
        return fill;
    }

    /**
     *
     * @param cba
     * @return
     */
    public static long generateCompletelyTrueClause(CompactBitArray cba) {
        int n = cba.getLength();
        long i0 = Utils.StaticRandom.nextInt(n);
        long i1 = Utils.StaticRandom.nextInt(n);
        long i2 = Utils.StaticRandom.nextInt(n);
        long ia = Math.min(i0, Math.min(i1, i2));
        long ic = Math.max(i0, Math.max(i1, i2));
        long ib = i0 + i1 + i2 - ia - ic;
        return generateCompletelyTrueClause(cba, ia, ib, ic);
    }

    /**
     *
     * @param cba
     * @param cdf
     * @return
     */
    public static long generateCompletelyTrueClause(CompactBitArray cba, double[] cdf) {
        long i0 = ProbabilityUtils.randomIndexFromCDF(cdf);
        long i1 = ProbabilityUtils.randomIndexFromCDF(cdf);
        long i2 = ProbabilityUtils.randomIndexFromCDF(cdf);
        long ia = Math.min(i0, Math.min(i1, i2));
        long ic = Math.max(i0, Math.max(i1, i2));
        long ib = i0 + i1 + i2 - ia - ic;
        return generateCompletelyTrueClause(cba, ia, ib, ic);
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int degree(long clause) {
        long inda = clause & 0x0F_FFFF;
        long indb = (clause >> 20) & 0x0F_FFFF;
        long indc = (clause >> 40) & 0x0F_FFFF;
        int deg = 1;
        if (inda != indb) {
            deg++;
        }
        if (indb != indc) {
            deg++;
        }
        return deg;
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int[] getUniqueIndices(long clause) {
        int[] ind = new int[degree(clause)];
        long inda = clause & 0x0F_FFFF;
        long indb = (clause >> 20) & 0x0F_FFFF;
        long indc = (clause >> 40) & 0x0F_FFFF;
        ind[0] = (int) inda;
        int deg = 1;
        if (inda != indb) {
            ind[deg++] = (int) indb;
        }
        if (indb != indc) {
            ind[deg++] = (int) indc;
        }
        return ind;
    }

    /**
     *
     * @param constraints
     * @return
     */
    public static String clausesToString(long[] constraints) {
        StringBuilder sb = new StringBuilder();
        for (long clause : constraints) {
            sb.append(String.format("(%s)", clauseToString(clause)));
        }
        return sb.toString();
    }

    /**
     *
     * @param clause
     * @return
     */
    public static boolean isHornClause(long clause) {
        long vals = (clause >> 60) & 7;
        return Utils.countOnes(vals) < 2;
    }

    /**
     *
     * @param clause
     * @return
     */
    public static boolean isValidClause(long clause) {
        long inda = getIndex0(clause);
        long indb = getIndex1(clause);
        long indc = getIndex2(clause);
        if (inda > indb || indb > indc) {
            return false;
        }
        if (indb == indc && inda != indb) {
            return false;
        }
        long vala = getValue0(clause);
        long valb = getValue1(clause);
        long valc = getValue2(clause);
        return (inda != indb || vala == valb) && (indb != indc || valb == valc) && (inda != indc || vala == valc);//TODO: optional third part? (indices are sorted)
    }

    /**
     *
     * @param n
     * @param influences
     * @param cba
     * @param constraints
     * @param from
     */
    public static void swapRandomBit(int n, int[][] influences, CompactBitArray cba, long[] constraints, ThreeSatSolution from) {
        int i = Utils.StaticRandom.nextInt(n);
        swapBit(i, influences[i], cba, constraints, from);
    }

    /**
     *
     * @param i
     * @param tocheck
     * @param cba
     * @param constraints
     * @param from
     */
    public static void swapBit(int i, int[] tocheck, CompactBitArray cba, long[] constraints, ThreeSatSolution from) {
        int j, np = tocheck[0], nn = tocheck.length, delta = 0;
        for (j = 1; j <= np; j++) {
            if (cba.willSwap(constraints[tocheck[j]], i)) {
                delta++;
            }
        }
        for (; j < nn; j++) {
            if (cba.willSwap(constraints[tocheck[j]], i)) {
                delta--;
            }
        }
        delta *= 1 - (cba.swapGetBit(i) << 1);
        from.addConfictingClauses(delta);
    }

    /**
     *
     * @param clause
     * @param i
     * @return
     */
    public static int getIndexI(long clause, int i) {
        return (int) ((clause >> (40 - 20 * i)) & 0x0F_FFFF);
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getIndex0(long clause) {
        return (int) ((clause >> 40) & 0x0F_FFFF);
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getIndex1(long clause) {
        return (int) ((clause >> 20) & 0x0F_FFFF);
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getIndex2(long clause) {
        return (int) (clause & 0x0F_FFFF);
    }

    /**
     *
     * @param clause
     * @param i
     * @return
     */
    public static int getValueI(long clause, int i) {
        return (int) ((clause >> (62 - i)) & 0x01);
    }

    /**
     *
     * @param clause
     * @param i
     * @return
     */
    public static int getSigned1IndexI(long clause, int i) {
        return (getIndexI(clause, i) + 1) * getSignedValueI(clause, i);
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getSigned1Index0(long clause) {
        return (getIndex0(clause) + 1) * getSignedValue0(clause);
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getSigned1Index1(long clause) {
        return (getIndex1(clause) + 1) * getSignedValue1(clause);
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getSigned1Index2(long clause) {
        return (getIndex2(clause) + 1) * getSignedValue2(clause);
    }

    /**
     *
     * @param clause
     * @param i
     * @return
     */
    public static int getSignedValueI(long clause, int i) {
        return (getValueI(clause, i) << 0x01) - 0x01;
    }

    /**
     *
     * @param fill
     * @param i
     * @param bit
     * @return
     */
    public static long setValue(long fill, int i, long bit) {
        return (fill & (~(0x01L << (62 - i)))) | (bit << (62 - i));
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getValue0(long clause) {
        return (int) ((clause >> 62) & 0x01);
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getValue1(long clause) {
        return (int) ((clause >> 61) & 0x01);
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getValue2(long clause) {
        return (int) ((clause >> 60) & 0x01);
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getSignedValue0(long clause) {
        return 2 * getValue0(clause) - 1;
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getSignedValue1(long clause) {
        return 2 * getValue1(clause) - 1;
    }

    /**
     *
     * @param clause
     * @return
     */
    public static int getSignedValue2(long clause) {
        return 2 * getValue2(clause) - 1;
    }

    /**
     *
     * @param clause
     * @param indices
     */
    public static void setIndices(long clause, int[] indices) {
        indices[2] = (int) (clause & 0x0F_FFFF);
        indices[1] = (int) ((clause >> 20) & 0x0F_FFFF);
        indices[0] = (int) ((clause >> 40) & 0x0F_FFFF);
    }

    /**
     * Retrieves the influences of a certain clause towards certain variables
     *
     * @param clause the clause to analyze
     * @param positive A 4-array where the first element is the number of
     * variables and the next three elements the indices of the variables.
     * @param negative A 4-array where the first element is the number of
     * variables and the next three elements the indices of the variables.
     */
    public static void setInfluences(long clause, int[] positive, int[] negative) {
        int ip = 1;
        int in = 1;
        int ia = (int) (clause & 0x0F_FFFF);
        int ib = (int) ((clause >> 20) & 0x0F_FFFF);
        int ic = (int) ((clause >> 40) & 0x0F_FFFF);
        if ((clause & 0x1000_0000_0000_0000L) != 0x00) {
            positive[ip++] = ia;
        } else {
            negative[in++] = ia;
        }
        if (ia != ib) {
            if ((clause & 0x2000_0000_0000_0000L) != 0x00) {
                positive[ip++] = ib;
            } else {
                negative[in++] = ib;
            }
        }
        if (ib != ic) {
            if ((clause & 0x4000_0000_0000_0000L) != 0x00) {
                positive[ip++] = ic;
            } else {
                negative[in++] = ic;
            }
        }
        positive[0] = ip;
        negative[0] = in;
    }

    /**
     *
     * @param clauses
     * @return
     */
    public static int getLargestIndex(Iterable<Long> clauses) {
        int index = 0;
        for (long clause : clauses) {
            index = Math.max(index, (int) (clause & 0x0F_FFFF));
        }
        return index;
    }

    /**
     *
     * @param clauses
     * @return
     */
    public static int getLargestIndex(long[] clauses) {
        int index = 0;
        for (long clause : clauses) {
            index = Math.max(index, (int) (clause & 0x0F_FFFF));
        }
        return index;
    }

    /**
     *
     * @param cba
     * @param clauses
     * @return
     */
    public static int getNumberOfFailedClauses(CompactBitArray cba, long[] clauses) {
        int number = 0;
        for (long clause : clauses) {
            if (!cba.satisfiesClause(clause)) {
                number++;
            }
        }
        return number;
    }

    /**
     *
     * @param from
     * @param clauses
     * @return
     */
    public static int getFalseClauseIndex(ThreeSatSolution from, long[] clauses) {
        int c = clauses.length;
        int c0 = Utils.StaticRandom.nextInt(c);
        for (int i = c0; i < c; i++) {
            if (!from.satisfiesClause(clauses[i])) {
                return i;
            }
        }
        for (int i = 0; i < c0; i++) {
            if (!from.satisfiesClause(clauses[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param cba1
     * @param cba2
     * @return
     */
    public static int getNonEqualVariableIndex(CompactBitArray cba1, CompactBitArray cba2) {
        int v = cba1.getLength();
        int v0 = Utils.StaticRandom.nextInt(v);//TODO: speedup with block level
        for (int i = v0; i < v; i++) {
            if (cba1.getBit(i) != cba2.getBit(i)) {
                return i;
            }
        }
        for (int i = 0; i < v0; i++) {
            if (cba1.getBit(i) != cba2.getBit(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param cba1
     * @param cba2
     * @return
     */
    public static int getEqualVariableIndex(CompactBitArray cba1, CompactBitArray cba2) {
        int v = cba1.getLength();
        int v0 = Utils.StaticRandom.nextInt(v);//TODO: speedup with block level
        for (int i = v0; i < v; i++) {
            if (cba1.getBit(i) == cba2.getBit(i)) {
                return i;
            }
        }
        for (int i = 0; i < v0; i++) {
            if (cba1.getBit(i) == cba2.getBit(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     *
     * @param clause
     * @return
     */
    public static String clauseToString(long clause) {
        long inda = getIndex0(clause);
        long indb = getIndex1(clause);
        long indc = getIndex2(clause);
        long vala = getValue0(clause);
        long valb = getValue1(clause);
        long valc = getValue2(clause);
        return String.format("[%s]=%s or [%s]=%s or [%s]=%s", inda, vala, indb, valb, indc, valc);
    }

    private ClauseUtils() {
    }
}
