package parallelhyflex.problems.threesat.heuristics;

import java.util.HashSet;
import parallelhyflex.problemdependent.heuristics.RuinRecreateHeuristicBase;
import parallelhyflex.problems.threesat.problem.ThreeSatProblem;
import parallelhyflex.problems.threesat.solution.ThreeSatSolution;
import parallelhyflex.utils.CompactBitArray;
import parallelhyflex.utils.Utils;

/**
 * A ruin-recreate heuristic where an entire block of 64 consecutive bits are
 * cleared and set to a new value based on a greedy approach where the number of
 * satisfiable clauses (if positive or negative) is maximized.
 *
 * @author kommusoft
 */
public class ThreeSatHeuristicR2 extends RuinRecreateHeuristicBase<ThreeSatSolution, ThreeSatProblem> {

    public ThreeSatHeuristicR2(ThreeSatProblem problem) {
        super(problem);
    }

    @Override
    public void applyHeuristicLocally(ThreeSatSolution from) {
        
    }
}