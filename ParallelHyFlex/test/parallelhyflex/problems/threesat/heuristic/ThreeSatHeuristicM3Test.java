package parallelhyflex.problems.threesat.heuristic;

import org.junit.Test;
import parallelhyflex.problemdependent.heuristic.MutationHeuristicBase;
import parallelhyflex.problems.TestRenewalStrategy;
import parallelhyflex.problems.heuristics.ProblemHeuristicMTestBase;
import parallelhyflex.problems.heuristics.TestHeuristicEvaluationStrategy;
import parallelhyflex.problems.threesat.ThreeSatRenewalStrategy;
import parallelhyflex.problems.threesat.problem.ThreeSatProblem;
import parallelhyflex.problems.threesat.problem.ThreeSatProblemGenerator;
import parallelhyflex.problems.threesat.solution.ThreeSatSolution;
import parallelhyflex.problems.threesat.solution.ThreeSatSolutionGenerator;

/**
 *
 * @author kommusoft
 */
public class ThreeSatHeuristicM3Test extends ProblemHeuristicMTestBase<ThreeSatSolutionGenerator,ThreeSatProblem,ThreeSatProblemGenerator,ThreeSatSolution> {
    
    /**
     * Test of applyHeuristicLocally method, of class ThreeSatHeuristicM1.
     */
    @Test
    @Override
    public void testApplyHeuristicLocallyEvaluationApprox() {
        super.testApplyHeuristicLocallyEvaluationApprox();
    }

    /**
     *
     * @return
     */
    @Override
    public MutationHeuristicBase<ThreeSatSolution, ThreeSatProblem> renewHeuristic() {
        return new ThreeSatHeuristicM3(getTsp());
    }

    /**
     *
     * @return
     */
    @Override
    public TestHeuristicEvaluationStrategy<ThreeSatSolutionGenerator, ThreeSatProblem, ThreeSatProblemGenerator, ThreeSatSolution> generateHeuristicEvaluationStrategy() {
        return new ThreeSatEvaluationStrategy();
    }

    /**
     *
     * @return
     */
    @Override
    public TestRenewalStrategy<ThreeSatSolutionGenerator, ThreeSatProblem, ThreeSatProblemGenerator, ThreeSatSolution> getRenewalStrategy() {
        return new ThreeSatRenewalStrategy();
    }
}