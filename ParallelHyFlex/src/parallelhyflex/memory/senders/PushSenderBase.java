package parallelhyflex.memory.senders;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import parallelhyflex.communication.Communication;
import parallelhyflex.communication.routing.SendTagged;
import parallelhyflex.problemdependent.solution.Solution;

/**
 *
 * @author kommusoft
 */
public abstract class PushSenderBase<TSolution extends Solution<TSolution>> implements PushSender<TSolution>, SendTagged {

    /**
     *
     */
    public static final int SendTag = 0x00;

    /**
     *
     * @param index
     * @param solution
     * @return
     */
    public Object[] generatePacket(int index, TSolution solution) {
        Object[] data = new Object[3];
        data[0] = Communication.getCommunication().getRank();
        data[1] = index;
        try {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); DataOutputStream dos = new DataOutputStream(baos)) {
                solution.write(dos);
                data[2] = baos.toByteArray();
            }
        } catch (Exception e) {
            Communication.log(e);
        }
        return new Object[]{data};
    }

    /**
     *
     * @return
     */
    @Override
    public int getSendTag() {
        return SendTag;
    }
}
