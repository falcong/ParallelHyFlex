/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelhyflex.problems.threesat;

import parallelhyflex.problems.threesat.solution.ThreeSatSolutionGenerator;
import parallelhyflex.problems.threesat.solution.ThreeSatSolution;
import parallelhyflex.problems.threesat.problem.ThreeSatProblemGenerator;
import parallelhyflex.problems.threesat.problem.ThreeSatProblem;
import parallelhyflex.problems.threesat.heuristics.ThreeSatHeuristicR1;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import parallelhyflex.TestParameters;

/**
 *
 * @author kommusoft
 */
public class ThreeSatHeuristicR1Test {
    
    public ThreeSatHeuristicR1Test() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of applyHeuristicLocally method, of class ThreeSatHeuristicR1.
     */
    @Test
    public void testApplyHeuristicLocallyConflictingClauses() {
        for (int i = 0; i < TestParameters.LOOP_PARAMETER; i++) {
            ThreeSatProblemGenerator tspg = new ThreeSatProblemGenerator(10, 42);
            ThreeSatProblem tsp = tspg.generateProblem();
            ThreeSatHeuristicR1 tshm1 = new ThreeSatHeuristicR1(tsp);
            ThreeSatSolutionGenerator tsg = tsp.getSolutionGenerator();
            ThreeSatSolution tss = tsg.generateSolution();
            Assert.assertEquals(ClauseUtils.getNumberOfFailedClauses(tss.getCompactBitArray(),tsp.getConstraints()),tss.getConflictingClauses());
            tshm1.applyHeuristicLocally(tss);
            Assert.assertEquals(ClauseUtils.getNumberOfFailedClauses(tss.getCompactBitArray(),tsp.getConstraints()),tss.getConflictingClauses());
        }
    }
}