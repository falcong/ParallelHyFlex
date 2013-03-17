package parallelhyflex.pushdeciders;

import parallelhyflex.problemdependent.Solution;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class ProbablePushDecider<TSolution extends Solution<TSolution>> implements PushDecider<TSolution> {
    
    private double probability;
    
    public ProbablePushDecider () {
        this(0.5);
    }
    public ProbablePushDecider (double probability) {
        this.probability = probability;
    }

    @Override
    public boolean decidePush(int index, TSolution sol) {
        return Utils.StaticRandom.nextDouble() < probability;
    }
    
}