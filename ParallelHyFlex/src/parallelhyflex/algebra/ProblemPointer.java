package parallelhyflex.algebra;

import parallelhyflex.problemdependent.problem.Problem;
import parallelhyflex.problemdependent.solution.Solution;

/**
 *
 * @param <TSolution> 
 * @param <TProblem> 
 * @author kommusoft
 */
public interface ProblemPointer<TSolution extends Solution<TSolution>, TProblem extends Problem<TSolution>> {

    public TProblem getProblem();
}
