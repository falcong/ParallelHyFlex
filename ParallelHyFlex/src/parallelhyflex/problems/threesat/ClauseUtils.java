package parallelhyflex.problems.threesat;

import parallelhyflex.utils.CompactBitArray;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class ClauseUtils {

    private ClauseUtils() {
    }

    public static int degree(long clause) {
        long inda = clause & 0x0FFFFF;
        long indb = (clause >> 20) & 0x0FFFFF;
        long indc = (clause >> 40) & 0x0FFFFF;
        int deg = 1;
        if(inda != indb) {
            deg++;
        }
        if(indb != indc) {
            deg++;
        }
        return deg;
    }
    
    public static boolean isHornClause (long clause) {
        long vals = (clause>>60)&7;
        return Utils.countOnes(vals)<2;
    }

    public static boolean isValidClause(long clause) {
        long inda = clause & 0x0FFFFF;
        long indb = (clause >> 20) & 0x0FFFFF;
        long indc = (clause >> 40) & 0x0FFFFF;
        if (inda > indb || indc > indb) {
            return false;
        }
        long vala = (clause >> 60) & 1;
        long valb = (clause >> 61) & 1;
        long valc = (clause >> 62) & 1;
        return (inda != indb || vala == valb) && (indb != indc || valb == valc) && (inda != indc || vala == valc);//TODO: optional third part? (indices are sorted)
    }

    public static void swapRandomBit(int n, int[][] influences, CompactBitArray cba, long[] constraints, ThreeSatSolution from) {
        int i = Utils.StaticRandom.nextInt(n);
        swapBit(i, influences[i], cba, constraints, from);
    }

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
        delta *= (cba.swapGetBit(i) << 1) - 1;
        from.addConfictingClauses(delta);
    }

    public static int getIndexI(long clause, int i) {
        return (int) ((clause >> (40 - 20 * i)) & 0x0FFFFF);
    }

    public static int getIndex0(long clause) {
        return (int) ((clause >> 40) & 0x0FFFFF);
    }

    public static int getIndex1(long clause) {
        return (int) ((clause >> 20) & 0x0FFFFF);
    }

    public static int getIndex2(long clause) {
        return (int) (clause & 0x0FFFFF);
    }

    public static int getValueI(long clause, int i) {
        return (int) ((clause >> (62 - i)) & 0x01);
    }

    public static int getValue0(long clause) {
        return (int) ((clause >> 62) & 0x01);
    }

    public static int getValue1(long clause) {
        return (int) ((clause >> 61) & 0x01);
    }

    public static int getValue2(long clause) {
        return (int) ((clause >> 60) & 0x01);
    }

    public static void setIndices(long clause, int[] indices) {
        indices[2] = (int) (clause & 0x0FFFFF);
        indices[1] = (int) ((clause >> 20) & 0x0FFFFF);
        indices[0] = (int) ((clause >> 40) & 0x0FFFFF);
    }

    /**
     * Retrieves the influences of a certain clause towards certain variables
     * @param clause the clause to analyse
     * @param positive A 4-array where the first element is the number of variables and the next three elements the indices of the variables.
     * @param negative A 4-array where the first element is the number of variables and the next three elements the indices of the variables.
     */
    public static void setInfluences(long clause, int[] positive, int[] negative) {
        int ip = 1;
        int in = 1;
        int ia = (int) (clause & 0x0FFFFF);
        int ib = (int) ((clause >> 20) & 0x0FFFFF);
        int ic = (int) ((clause >> 40) & 0x0FFFFF);
        if ((clause & 0x1000000000000000L) != 0x00) {
            positive[ip++] = ia;
        } else {
            negative[in++] = ia;
        }
        if (ia != ib) {
            if ((clause & 0x2000000000000000L) != 0x00) {
                positive[ip++] = ib;
            } else {
                negative[in++] = ib;
            }
        }
        if (ib != ic) {
            if ((clause & 0x4000000000000000L) != 0x00) {
                positive[ip++] = ic;
            } else {
                negative[in++] = ic;
            }
        }
        positive[0] = ip;
        negative[0] = in;
    }

    public static int getLargestIndex(Iterable<Long> clauses) {
        int index = 0;
        for (long clause : clauses) {
            index = Math.max(index, (int) (clause & 0x0FFFFF));
        }
        return index;
    }

    public static int getLargestIndex(long[] clauses) {
        int index = 0;
        for (long clause : clauses) {
            index = Math.max(index, (int) (clause & 0x0FFFFF));
        }
        return index;
    }

    public static String clauseToString(long clause) {
        long inda = clause & 0x0FFFFF;
        long indb = (clause >> 20) & 0x0FFFFF;
        long indc = (clause >> 40) & 0x0FFFFF;
        long vala = (clause >> 60) & 1;
        long valb = (clause >> 61) & 1;
        long valc = (clause >> 62) & 1;
        return String.format("[%s]=%s or [%s]=%s or [%s]=%s", inda, vala, indb, valb, indc, valc);
    }
}