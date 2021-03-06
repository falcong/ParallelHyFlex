package parallelhyflex.problems.frequencyassignment.problem;

import parallelhyflex.problemdependent.problem.ObjectiveFunctionBase;
import parallelhyflex.problems.frequencyassignment.solution.FrequencyAssignmentSolution;

/**
 *
 * @author kommusoft
 */
public class FrequencyAssignmentObjectiveFunction1 extends ObjectiveFunctionBase<FrequencyAssignmentSolution> {

    /**
     *
     */
    public static final double K = 10e9;

    /**
     *
     * @param solution
     * @return
     */
    @Override
    public double evaluateSolution(FrequencyAssignmentSolution solution) {
        return solution.getInterference() + K * solution.getnConflicts();
    }
}
