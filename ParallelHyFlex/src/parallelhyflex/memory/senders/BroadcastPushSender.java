package parallelhyflex.memory.senders;

import java.util.logging.Logger;
import mpi.MPI;
import parallelhyflex.communication.Communication;
import parallelhyflex.communication.abstraction.CommMode;
import parallelhyflex.problemdependent.solution.Solution;

/**
 *
 * @author kommusoft
 */
public class BroadcastPushSender<TSolution extends Solution<TSolution>> extends PushSenderBase<TSolution> {

    /**
     *
     * @param index
     * @param solution
     */
    @Override
    public void sendSolution(int index, TSolution solution) {
        Communication.BcastRoot(CommMode.MpiNonBlocking,this.generatePacket(index, solution), 0, 1, MPI.OBJECT, PushSenderBase.SendTag);
    }
    private static final Logger LOG = Logger.getLogger(BroadcastPushSender.class.getName());

}
