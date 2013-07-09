package parallelhyflex.problemdependent.searchspace;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import parallelhyflex.algebra.collections.MultiThreadedList;
import parallelhyflex.problemdependent.constraints.EnforceableConstraint;
import parallelhyflex.problemdependent.solution.Solution;
import parallelhyflex.utils.ProbabilityUtils;

/**
 *
 * @author kommusoft
 */
public class TwoSetSearchSpace<TSolution extends Solution<TSolution>> extends SearchSpaceBase<TSolution> {

    private final List<EnforceableConstraint<TSolution>> positive = new MultiThreadedList<>();
    private final ReentrantReadWriteLock posLock = new ReentrantReadWriteLock();
    private final List<EnforceableConstraint<TSolution>> negative = new MultiThreadedList<>();
    private final ReentrantReadWriteLock negLock = new ReentrantReadWriteLock();

    /**
     *
     * @param solution
     */
    @Override
    public void correct(TSolution solution) {
        /*Lock lo = this.posLock.readLock();
        lo.lock();
        try {*/
            for (EnforceableConstraint<TSolution> constraint : this.getPositive()) {
                constraint.enforceTrue(solution);
            }
        /*} finally {
            lo.unlock();
        }*/
        /*lo = this.negLock.readLock();
        lo.lock();
        try {*/
            if (this.getNegative().size() > 0) {
                ProbabilityUtils.randomElement(this.getNegative()).enforceFalse(solution);
            }
        /*} finally {
            lo.unlock();
        }*/
    }

    /**
     *
     * @param solution
     * @return
     */
    @Override
    public boolean isInSearchSpace(TSolution solution) {
        //TODO: positive
        /*Lock lo = this.negLock.readLock();
        lo.lock();
        try {*/
            for (EnforceableConstraint<TSolution> constraint : this.getNegative()) {
                if (!constraint.isSatisfied(solution)) {
                    return true;
                }
            }
            return false;
        /*} finally {
            lo.unlock();
        }*/
    }

    /**
     * @return the positive
     */
    public List<EnforceableConstraint<TSolution>> getPositive() {
        return positive;
    }

    /**
     *
     * @param positive
     */
    public void replacePositive(Collection<? extends EnforceableConstraint<TSolution>> positive) {
        /*Lock lo = this.posLock.writeLock();
        lo.lock();
        try {*/
            this.positive.clear();
            this.positive.addAll(positive);
        /*} finally {
            lo.unlock();
        }*/
    }

    /**
     * @return the negative
     */
    public List<EnforceableConstraint<TSolution>> getNegative() {
        return negative;
    }

    /**
     *
     * @param positive
     */
    public void replaceNegative(Collection<? extends EnforceableConstraint<TSolution>> positive) {
        /*Lock lo = this.negLock.writeLock();
        lo.lock();
        try {*/
            this.negative.clear();
            this.negative.addAll(positive);
        /*} finally {
            lo.unlock();
        }*/
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        /*Lock lo = this.posLock.writeLock();
        lo.lock();
        try {*/
            sb.append('+');
            sb.append(this.positive.toString());
        /*} finally {
            lo.unlock();
        }
        lo = this.negLock.writeLock();
        lo.lock();
        try {*/
            sb.append('-');
            sb.append(this.negative.toString());
        /*} finally {
            lo.unlock();
        }*/
        return sb.toString();
    }
    
}
