package parallelhyflex.problems.threesat.constraints;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import parallelhyflex.problems.threesat.ClauseUtils;
import parallelhyflex.problems.threesat.problem.ThreeSatProblem;
import parallelhyflex.problems.threesat.solution.ThreeSatSolution;
import parallelhyflex.utils.CompactBitArray;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class ThreeSatWriteableEnforceableConstraint1 extends ThreeSatWriteableEnforceableConstraint {
    /**
     *
     */
    public static final long MASK_BIT = 0x8000_0000_0000_0000L;
    /**
     *
     */
    public static final long MASK = 0x7FFF_FFFF_FFFF_FFFFL;

    private long constraint;

    /**
     *
     * @param problem
     * @param constraint
     */
    public ThreeSatWriteableEnforceableConstraint1(ThreeSatProblem problem, long constraint) {
        super(problem);
        this.constraint = constraint;
    }

    /**
     *
     * @param solution
     */
    @Override
    public void enforceTrue(ThreeSatSolution solution) {
        CompactBitArray cba = solution.getCompactBitArray();
        if (!cba.satisfiesClause(constraint)) {
            int ii = Utils.StaticRandom.nextInt(3);
            solution.swapBit(ClauseUtils.getIndexI(this.getConstraint(), ii), this.getProblem());
        }
    }

    /**
     *
     * @param solution
     */
    @Override
    public void enforceFalse(ThreeSatSolution solution) {
        CompactBitArray cba = solution.getCompactBitArray();
        ThreeSatProblem tsp = this.getProblem();
        if (cba.satisfiesClause(getConstraint())) {
            int index = ClauseUtils.getIndex0(this.constraint);
            if (cba.getBit(index) == ClauseUtils.getValue0(this.constraint)) {
                solution.swapBit(index, tsp);
            }
            index = ClauseUtils.getIndex1(this.constraint);
            if (cba.getBit(index) == ClauseUtils.getValue1(this.constraint)) {
                solution.swapBit(index, tsp);
            }
            index = ClauseUtils.getIndex2(this.constraint);
            if (cba.getBit(index) == ClauseUtils.getValue2(this.constraint)) {
                solution.swapBit(index, tsp);
            }
        }
    }

    /**
     *
     * @param solution
     * @return
     */
    @Override
    public boolean isSatisfied(ThreeSatSolution solution) {
        return solution.satisfiesClause(getConstraint());
    }

    /**
     *
     * @param dos
     * @throws IOException
     */
    @Override
    public void write(DataOutputStream dos) throws IOException {
        dos.writeLong(MASK_BIT | this.getConstraint());
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ThreeSatWriteableEnforceableConstraint1) {
            return this.getConstraint() == ((ThreeSatWriteableEnforceableConstraint1) obj).getConstraint();
        } else {
            return false;
        }
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.getConstraint() ^ (this.getConstraint() >>> 32));
        return hash;
    }

    /**
     * @return the data
     */
    public long getConstraint() {
        return constraint;
    }

    /**
     * @param data the data to set
     */
    public void setData(long data) {
        this.constraint = data;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString () {
        return String.format("(%s)",ClauseUtils.clauseToString(this.getConstraint()));
    }
    private static final Logger LOG = Logger.getLogger(ThreeSatWriteableEnforceableConstraint1.class.getName());
    
}
