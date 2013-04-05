/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parallelhyflex.problems.threesat.constraint;

import parallelhyflex.problems.threesat.solution.ThreeSatSolutionGenerator;
import parallelhyflex.problems.threesat.solution.ThreeSatSolution;
import parallelhyflex.problems.threesat.problem.ThreeSatProblemGenerator;
import parallelhyflex.problems.threesat.problem.ThreeSatProblem;
import parallelhyflex.problems.threesat.constraints.ThreeSatWritableEnforceableConstraintGenerator;
import parallelhyflex.problems.threesat.constraints.ThreeSatWritableEnforceableConstraint2;
import parallelhyflex.problemdependent.constraints.EnforceableConstraint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import junit.framework.Assert;
import java.io.DataOutputStream;
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
public class ThreeSatWritableEnforceableConstraint2Test {

    public ThreeSatWritableEnforceableConstraint2Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of enforceTrue method, of class ThreeSatWritableEnforceableConstraint2.
     */
    @Test
    public void testEnforceTrue1() {
        for (int i = 0; i < TestParameters.LOOP_PARAMETER; i++) {
            ThreeSatProblemGenerator tspg = new ThreeSatProblemGenerator(10, 42);
            ThreeSatProblem tsp = tspg.generateProblem();
            ThreeSatWritableEnforceableConstraintGenerator tsweg = new ThreeSatWritableEnforceableConstraintGenerator(tsp);
            ThreeSatSolutionGenerator tsg = tsp.getSolutionGenerator();
            ThreeSatSolution tss = tsg.generateSolution();
            ThreeSatWritableEnforceableConstraint2 tswec = new ThreeSatWritableEnforceableConstraint2(tsp, tss.clone(), 5);
            tswec.enforceTrue(tss);
            Assert.assertTrue(tswec.isSatisfied(tss));
        }
    }

    /**
     * Test of enforceTrue method, of class ThreeSatWritableEnforceableConstraint2.
     */
    @Test
    public void testEnforceTrue2() {
        ThreeSatProblemGenerator tspg = new ThreeSatProblemGenerator(10, 42);
        ThreeSatProblem tsp = tspg.generateProblem();
        ThreeSatWritableEnforceableConstraintGenerator tsweg = new ThreeSatWritableEnforceableConstraintGenerator(tsp);
        ThreeSatSolutionGenerator tsg = tsp.getSolutionGenerator();
        ThreeSatSolution tss = tsg.generateSolution();
        ThreeSatWritableEnforceableConstraint2 tswec = new ThreeSatWritableEnforceableConstraint2(tsp, tss.clone(), 5);
        for (int i = 0; i < TestParameters.LOOP_PARAMETER; i++) {
            ThreeSatSolution tss2 = tsg.generateSolution();
            tswec.enforceTrue(tss2);
            Assert.assertTrue(tswec.isSatisfied(tss2));
        }
    }

    /**
     * Test of enforceFalse method, of class ThreeSatWritableEnforceableConstraint2.
     */
    @Test
    public void testEnforceFalse1() {
        for (int i = 0; i < TestParameters.LOOP_PARAMETER; i++) {
            ThreeSatProblemGenerator tspg = new ThreeSatProblemGenerator(10, 42);
            ThreeSatProblem tsp = tspg.generateProblem();
            ThreeSatWritableEnforceableConstraintGenerator tsweg = new ThreeSatWritableEnforceableConstraintGenerator(tsp);
            ThreeSatSolutionGenerator tsg = tsp.getSolutionGenerator();
            ThreeSatSolution tss = tsg.generateSolution();
            ThreeSatWritableEnforceableConstraint2 tswec = new ThreeSatWritableEnforceableConstraint2(tsp, tss.clone(), 5);
            tswec.enforceFalse(tss);
            Assert.assertFalse(tswec.isSatisfied(tss));
        }
    }

    /**
     * Test of enforceFalse method, of class ThreeSatWritableEnforceableConstraint2.
     */
    @Test
    public void testEnforceFalse2() {
        ThreeSatProblemGenerator tspg = new ThreeSatProblemGenerator(10, 42);
        ThreeSatProblem tsp = tspg.generateProblem();
        ThreeSatWritableEnforceableConstraintGenerator tsweg = new ThreeSatWritableEnforceableConstraintGenerator(tsp);
        ThreeSatSolutionGenerator tsg = tsp.getSolutionGenerator();
        ThreeSatSolution tss = tsg.generateSolution();
        ThreeSatWritableEnforceableConstraint2 tswec = new ThreeSatWritableEnforceableConstraint2(tsp, tss.clone(), 5);
        for (int i = 0; i < TestParameters.LOOP_PARAMETER; i++) {
            ThreeSatSolution tss2 = tsg.generateSolution();
            tswec.enforceFalse(tss2);
            Assert.assertFalse(tswec.isSatisfied(tss2));
        }
    }

    /**
     * Test of serialisation/deserialisation method, of class ThreeSatWritableEnforceableConstraint1.
     */
    @Test
    public void testSerializeDeserialize() throws Exception {
        for (int i = 0; i < TestParameters.LOOP_PARAMETER; i++) {
            ThreeSatProblemGenerator tspg = new ThreeSatProblemGenerator(10, 42);
            ThreeSatProblem tsp = tspg.generateProblem();
            ThreeSatWritableEnforceableConstraintGenerator tsweg = new ThreeSatWritableEnforceableConstraintGenerator(tsp);
            ThreeSatSolutionGenerator tsg = tsp.getSolutionGenerator();
            ThreeSatSolution tss = tsg.generateSolution();
            ThreeSatWritableEnforceableConstraint2 tswec = new ThreeSatWritableEnforceableConstraint2(tsp, tss, 50);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            tswec.write(dos);
            dos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            baos.close();
            DataInputStream dis = new DataInputStream(bais);
            EnforceableConstraint<ThreeSatSolution> tswec2 = tsweg.readAndGenerate(dis);
            Assert.assertEquals(tswec, tswec2);
            dis.close();
            bais.close();
        }
    }
}