package parallelhyflex.problems.threesat.solution;

import parallelhyflex.problems.threesat.problem.ThreeSatProblem;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import parallelhyflex.problemdependent.solution.Solution;
import parallelhyflex.problems.threesat.ClauseUtils;
import parallelhyflex.utils.CompactBitArray;

/**
 *
 * @author kommusoft
 */
public class ThreeSatSolution implements Solution<ThreeSatSolution> {

    private final CompactBitArray cba;
    private int conflictingClauses;

    ThreeSatSolution(int conflictingClauses, CompactBitArray cba) {
        this.conflictingClauses = conflictingClauses;
        this.cba = cba;
    }

    public boolean get(int index) {
        return this.get(index);
    }

    public long getBit(int index) {
        return this.getCompactBitArray().getBit(index);
    }

    public long getBit(long index) {
        return this.getCompactBitArray().getBit(index);
    }

    public boolean satisfiesClause(long constraint) {
        return this.getCompactBitArray().satisfiesClause(constraint);
    }

    public void swap(int index) {
        this.getCompactBitArray().swap(index);
    }

    public void swapRange(int fromIndex, int toIndex) {
        this.getCompactBitArray().swapRange(fromIndex, toIndex);
    }

    public void setRange(int fromIndex, int toIndex) {
        this.getCompactBitArray().setRange(fromIndex, toIndex);
    }

    public void resetRange(int fromIndex, int toIndex) {
        this.getCompactBitArray().resetRange(fromIndex, toIndex);
    }

    public void set(int index, boolean value) {
        this.getCompactBitArray().set(index, value);
    }

    public int getConflictingClauses() {
        return this.conflictingClauses;
    }

    void setConflictingClauses(int conflictingClauses) {
        this.conflictingClauses = conflictingClauses;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ThreeSatSolution) {
            return this.equalSolution((ThreeSatSolution) obj);
        }
        return false;
    }

    public void recalculateConflictingClauses(long[] clauses) {
        int nfail = 0;
        for (long clause : clauses) {
            if (!this.satisfiesClause(clause)) {
                nfail++;
            }
        }
        this.conflictingClauses = nfail;
    }

    public void clearTail() {
        this.getCompactBitArray().clearTail();
    }

    @Override
    public int hashCode() {
        return this.getCompactBitArray().hashCode();
    }

    @Override
    public String toString() {
        return this.getCompactBitArray().toString();
    }

    @Override
    public boolean hasFastDifferenceWith(ThreeSatSolution other) {
        return false;
    }

    @Override
    public ThreeSatSolution clone() {
        return new ThreeSatSolution(this.getConflictingClauses(), getCompactBitArray().clone());
    }

    @Override
    public boolean equalSolution(ThreeSatSolution other) {
        return (this.conflictingClauses == other.conflictingClauses && this.getCompactBitArray().equals(other.getCompactBitArray()));
    }

    @Override
    public void write(DataOutputStream os) throws IOException {
        os.writeInt(this.getConflictingClauses());
        this.getCompactBitArray().writeSolution(os);
    }

    @Override
    public void read(DataInputStream is) throws IOException {
        this.getCompactBitArray().readSolution(is);
    }

    public int getLength() {
        return this.getCompactBitArray().getLength();
    }

    /**
     * @return the cba
     */
    public CompactBitArray getCompactBitArray() {
        return cba;
    }

    public void addConfictingClauses(int delta) {
        this.conflictingClauses += delta;
    }

    public void swapBit(int index, ThreeSatProblem problem) {
        ClauseUtils.swapBit(index, problem.getInfluences()[index], cba, problem.getClauses(), this);
    }
}
