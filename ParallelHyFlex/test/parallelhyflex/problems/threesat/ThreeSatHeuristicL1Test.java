/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelhyflex.problems.threesat;

import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import parallelhyflex.TestParameters;

/**
 *
 * @author kommusoft
 */
public class ThreeSatHeuristicL1Test {
    
    public ThreeSatHeuristicL1Test() {
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
     * Test of applyHeuristicLocally method, of class ThreeSatHeuristicL1.
     */
    @Test
    public void testApplyHeuristicLocallyConflictingClauses() {
        for (int i = 0; i < TestParameters.LOOP_PARAMETER; i++) {
            ThreeSatProblemGenerator tspg = new ThreeSatProblemGenerator(10, 42);
            ThreeSatProblem tsp = tspg.generateProblem();
            ThreeSatHeuristicL1 tshm1 = new ThreeSatHeuristicL1(tsp);
            ThreeSatSolutionGenerator tsg = tsp.getGenerator();
            ThreeSatSolution tss = tsg.generateSolution();
            Assert.assertEquals(ClauseUtils.getNumberOfFailedClauses(tss.getCompactBitArray(),tsp.getConstraints()),tss.getConflictingClauses());
            tshm1.applyHeuristicLocally(tss);
            Assert.assertEquals(ClauseUtils.getNumberOfFailedClauses(tss.getCompactBitArray(),tsp.getConstraints()),tss.getConflictingClauses());
        }
    }
    
    /**
     * Test of applyHeuristicLocally method, of class ThreeSatHeuristicL1.
     */
    @Test
    public void testApplyHeuristicLocallyImprovementConflictingClauses() {
        for (int i = 0; i < TestParameters.LOOP_PARAMETER; i++) {
            ThreeSatProblemGenerator tspg = new ThreeSatProblemGenerator(10, 42);
            ThreeSatProblem tsp = tspg.generateProblem();
            ThreeSatHeuristicL1 tshm1 = new ThreeSatHeuristicL1(tsp);
            ThreeSatSolutionGenerator tsg = tsp.getGenerator();
            ThreeSatSolution tss = tsg.generateSolution();
            int old = tss.getConflictingClauses();
            tshm1.applyHeuristicLocally(tss);
            Assert.assertTrue(old >= tss.getConflictingClauses());
        }
    }
}