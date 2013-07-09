package parallelhyflex.problems.threesat.constraint;

import parallelhyflex.problemdependent.constraints.WritableEnforceableConstraintBase;
import parallelhyflex.problems.threesat.problem.ThreeSatProblem;
import parallelhyflex.problems.threesat.solution.ThreeSatSolution;

/**
 *
 * @author kommusoft
 */
public abstract class ThreeSatWritableEnforceableConstraint extends WritableEnforceableConstraintBase<ThreeSatSolution, ThreeSatProblem> {
    
    /**
     *
     * @param problem
     */
    protected ThreeSatWritableEnforceableConstraint(ThreeSatProblem problem) {
        super(problem);
    }
    
}
