package parallelhyflex.memory.senders;

import mpi.MPI;
import parallelhyflex.communication.Communication;
import parallelhyflex.problemdependent.solution.Solution;

/**
 *
 * @author kommusoft
 */
public class DistributedPushSender<TSolution extends Solution<TSolution>> extends PushSenderBase<TSolution> {

    private int sendIndex = 0;

    @Override
    public void sendSolution(int index, TSolution solution) {
        Object[] data = generatePacket(index, solution);
        Communication.NbS(data, 0, 3, MPI.OBJECT, Communication.others()[this.sendIndex], 0);
        this.sendIndex = (this.sendIndex + 1) % (Communication.getCommunication().getSize() - 1);
    }
}