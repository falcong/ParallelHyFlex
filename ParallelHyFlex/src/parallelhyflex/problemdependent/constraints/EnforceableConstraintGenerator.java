package parallelhyflex.problemdependent.constraints;

import parallelhyflex.communication.serialisation.ReadableGenerator;
import parallelhyflex.problemdependent.solution.Solution;

/**
 *
 * @author kommusoft
 */
public interface EnforceableConstraintGenerator<TSolution extends Solution<TSolution>, TEC extends EnforceableConstraint<TSolution>> extends ReadableGenerator<TEC> {
}
