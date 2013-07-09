package parallelhyflex.communication;

import java.util.ArrayList;
import java.util.Iterator;
import mpi.MPI;
import parallelhyflex.algebra.collections.ArrayIterator;
import parallelhyflex.algebra.collections.CastingIterator;
import parallelhyflex.communication.abstraction.CommMode;
import parallelhyflex.communication.routing.PacketReceiver;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class AsynchronousGatherAll<T> implements PacketReceiver, Iterable<T> {

    private final int[] packetTags;
    private int currentDimension = 0;
    private final Object[] cache;
    private int receiveCache;

    /**
     *
     * @param packetTag
     */
    public AsynchronousGatherAll(int packetTag) {
        packetTags = new int[]{packetTag};
        this.cache = new Object[Communication.getCommunication().getSize()];
        this.reset();
    }

    /**
     *
     */
    public void reset() {
        this.receiveCache = Communication.getCommunication().getNeighbor(currentDimension) << 1;
        this.currentDimension = 0;
    }

    /**
     *
     * @param rank
     * @return
     */
    public boolean available(int rank) {
        int diff = Utils.base2Pow(Communication.getCommunication().getRank() ^ rank);
        return (this.receiveCache & diff) != 0x00;
    }

    /**
     *
     * @param value
     */
    public void send(T value) {
        this.receiveCache = 1;
        this.cache[Communication.getCommunication().getRank()] = value;
        this.checkSend();
    }

    /**
     *
     * @param rank
     * @return
     */
    public T getData(int rank) {
        return (T) cache[rank];
    }

    /**
     *
     * @param type
     * @return
     */
    public T[] toArray(T[] type) {
        return toArrayList().toArray(type);
    }

    /**
     *
     * @return
     */
    public boolean isReady() {
        return this.currentDimension >= Communication.getCommunication().getDimensions() && (this.receiveCache & (1 << Communication.getCommunication().getDimensions())) != 0x00;
    }

    /**
     *
     * @return
     */
    @Override
    public int[] getPacketTags() {
        return this.packetTags;
    }

    /**
     *
     * @param from
     * @param tag
     * @param data
     * @throws Exception
     */
    @Override
    public void receivePacket(int from, int tag, Object data) throws Exception {
        Object[] unpack = (Object[]) data;
        int diff = Communication.getCommunication().getRank() ^ from;
        int s = Communication.getCommunication().getSize();
        this.receiveCache |= diff << 1;
        int base = from & (~(diff - 1));
        int red = Math.min(diff, s - base);
        System.arraycopy(unpack, 0, cache, base, red);
        this.checkSend();
    }

    private void checkSend() {
        int d = Communication.getCommunication().getDimensions();
        int r = Communication.getCommunication().getRank();
        int s = Communication.getCommunication().getSize();
        int cd = this.currentDimension;
        int l = 1 << cd;
        while (cd < d && (this.receiveCache & l) != 0x00) {
            int other = r ^ l;
            if (other < s) {
                int base = r & (~(l - 1));
                int red = Math.min(l, s - base);
                Object[][] packet = new Object[1][red];
                System.arraycopy(cache, base, packet[0], 0, red);
                Communication.Send(CommMode.MpiNonBlocking, packet, 0, 1, MPI.OBJECT, other, this.packetTags[0x00]);
            }
            cd++;
            l <<= 1;
        }
        this.currentDimension = cd;
    }

    /**
     *
     * @return
     */
    public ArrayList<T> toArrayList() {
        ArrayList<T> al = new ArrayList<>(cache.length);
        for (Object o : cache) {
            al.add((T) o);
        }
        return al;
    }

    /**
     *
     * @return
     */
    @Override
    public Iterator<T> iterator() {
        return new CastingIterator<>(new ArrayIterator<>(this.cache));
    }
}
