package parallelhyflex.problems.threesat.constraint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import junit.framework.Assert;
import org.junit.Test;
import parallelhyflex.TestParameters;
import parallelhyflex.problemdependent.constraints.EnforceableConstraint;
import parallelhyflex.problemdependent.constraints.WritableEnforceableConstraintBase;
import parallelhyflex.problems.threesat.constraints.ThreeSatWritableEnforceableConstraint2;
import parallelhyflex.problems.threesat.constraints.ThreeSatWritableEnforceableConstraintGenerator;
import parallelhyflex.problems.threesat.problem.ThreeSatProblem;
import parallelhyflex.problems.threesat.problem.ThreeSatProblemGenerator;
import parallelhyflex.problems.threesat.solution.ThreeSatSolution;
import parallelhyflex.problems.threesat.solution.ThreeSatSolutionGenerator;

/**
 *
 * @author kommusoft
 */
public class ThreeSatWritableEnforceableConstraint2Test extends ThreeSatWritableEnforceableConstraintTestBase {

    public ThreeSatWritableEnforceableConstraint2Test() {
    }

    /**
     * Test of enforceTrue method, of class
     * ThreeSatWritableEnforceableConstraint2.
     */
    @Test
    @Override
    public void testEnforceTrue1() {
        super.testEnforceTrue1();
    }

    /**
     * Test of enforceTrue method, of class
     * ThreeSatWritableEnforceableConstraint2.
     */
    @Test
    @Override
    public void testEnforceTrue2() {
        super.testEnforceTrue2();
    }

    /**
     * Test of enforceFalse method, of class
     * ThreeSatWritableEnforceableConstraint2.
     */
    @Test
    @Override
    public void testEnforceFalse1() {
        super.testEnforceFalse1();
    }

    /**
     * Test of enforceFalse method, of class
     * ThreeSatWritableEnforceableConstraint2.
     */
    @Test
    @Override
    public void testEnforceFalse2() {
        super.testEnforceFalse2();
    }

    /**
     * Test of serialisation/deserialisation method, of class
     * ThreeSatWritableEnforceableConstraint1.
     */
    @Test
    public void testSerializeDeserialize() throws Exception {
        for (int i = 0; i < TestParameters.LOOP_PARAMETER; i++) {
            ThreeSatProblemGenerator tspg = new ThreeSatProblemGenerator(TestParameters.NUMBER_OF_VARIABLES, TestParameters.NUMBER_OF_CLAUSES);
            ThreeSatProblem tsp = tspg.generateProblem();
            ThreeSatWritableEnforceableConstraintGenerator tsweg = new ThreeSatWritableEnforceableConstraintGenerator(tsp);
            ThreeSatSolutionGenerator tsg = tsp.getSolutionGenerator();
            ThreeSatSolution tss = tsg.generateSolution();
            ThreeSatWritableEnforceableConstraint2 tswec = new ThreeSatWritableEnforceableConstraint2(tsp, tss, 50);
            ByteArrayInputStream bais;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                try (DataOutputStream dos = new DataOutputStream(baos)) {
                    tswec.write(dos);
                }
                bais = new ByteArrayInputStream(baos.toByteArray());
            }
            try (DataInputStream dis = new DataInputStream(bais)) {
                EnforceableConstraint<ThreeSatSolution> tswec2 = tsweg.readAndGenerate(dis);
                Assert.assertEquals(tswec, tswec2);
            }
            bais.close();
        }
    }

    @Override
    public WritableEnforceableConstraintBase<ThreeSatSolution, ThreeSatProblem> renewWritableEnforceableConstraint() {
        return new ThreeSatWritableEnforceableConstraint2(getTsp(), getTss().clone(), 5);
    }
}
