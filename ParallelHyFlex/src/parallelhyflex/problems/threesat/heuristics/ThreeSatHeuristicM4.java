package parallelhyflex.problems.threesat.heuristics;

import parallelhyflex.problemdependent.heuristics.MutationHeuristicBase;
import parallelhyflex.problems.threesat.ClauseUtils;
import parallelhyflex.problems.threesat.problem.ThreeSatProblem;
import parallelhyflex.problems.threesat.solution.ThreeSatSolution;
import parallelhyflex.utils.CompactBitArray;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class ThreeSatHeuristicM4 extends MutationHeuristicBase<ThreeSatSolution,ThreeSatProblem> {
    
    public ThreeSatHeuristicM4 (ThreeSatProblem problem) {
        super(problem);
    }

    @Override
    public void applyHeuristicLocally(ThreeSatSolution from) {
        ThreeSatProblem problem = this.getProblem();
        int c = problem.getC();
        long[] clauses = problem.getConstraints();
        do {
            enforceClause(c, from, clauses, problem);
        } while (Utils.StaticRandom.nextDouble() < this.getIntensityOfMutation());
    }
    
    private void enforceClause(int c, ThreeSatSolution from, long[] clauses, ThreeSatProblem problem) {
        CompactBitArray cba = from.getCompactBitArray();
        int[][] influences = problem.getInfluences();
        int delta;
        int c0 = Utils.StaticRandom.nextInt(c);
        for (Integer i : Utils.getLimitedModuloEnumerable(c0, c)) {
            if (!from.satisfiesClause(clauses[i])) {
                int mindelta = Integer.MAX_VALUE;
                int minindex = -1;
                for(int k = 0; k < 3; k++) {
                    int index = ClauseUtils.getIndexI(clauses[i], k);
                    delta = ClauseUtils.calculateLoss(index, cba, clauses, influences[index]);
                    if(delta < mindelta) {
                        mindelta = delta;
                        minindex = index;
                    }
                }
                if(minindex != -1) {
                    from.swap(minindex);
                    from.addConfictingClauses(mindelta);
                }
                return;
            }
        }
    }
    
}