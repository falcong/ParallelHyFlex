package parallelhyflex;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import mpi.MPI;
import parallelhyflex.utils.CompactBitArray;

/**
 *
 * @author kommusoft
 */
public abstract class MemorySlots<TSolution extends Solution<TSolution>> {
    
    private final MemoryExchangePolicy policy;
    
    public MemorySlots (MemoryExchangePolicy policy) {
        this.policy = policy;
    }

    /**
     * @return the isLocal
     */
    public abstract boolean isLocal();

    /**
     * @return the policy
     */
    public MemoryExchangePolicy getPolicy() {
        return policy;
    }

    public abstract void pushSolution(int index);
    public abstract void setSolution (int index, TSolution sol);
    public abstract TSolution getSolution (int index);
    abstract void receiveSolution (int index, TSolution sol);
    
    @Override
    public String toString () {
        return String.format("MemorySlots[%s,%s,%s]",this.isLocal(),this.getPolicy(),this.getSize());
    }

    public abstract int getSize();

    /**
     * @return the notExchangeMask
     */
    public abstract CompactBitArray getNotExchangeMask();
    
    public abstract boolean willExchange (int index);
    
}
