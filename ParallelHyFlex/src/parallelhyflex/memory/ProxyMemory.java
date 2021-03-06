package parallelhyflex.memory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.logging.Logger;
import mpi.MPI;
import parallelhyflex.algebra.Generator;
import parallelhyflex.communication.Communication;
import parallelhyflex.communication.abstraction.CommMode;
import parallelhyflex.communication.routing.PacketReceiver;
import parallelhyflex.logging.LoggingParameters;
import parallelhyflex.memory.senders.PushSenderBase;
import parallelhyflex.problemdependent.experience.WriteableExperience;
import parallelhyflex.problemdependent.heuristic.Heuristic;
import parallelhyflex.problemdependent.searchspace.DummySearchSpace;
import parallelhyflex.problemdependent.searchspace.SearchSpace;
import parallelhyflex.problemdependent.solution.Solution;
import parallelhyflex.problemdependent.solution.SolutionGenerator;
import parallelhyflex.problemdependent.solution.SolutionReader;
import parallelhyflex.utils.CompactBitArray;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class ProxyMemory<TSolution extends Solution<TSolution>> implements PacketReceiver {

    private final MemorySlots<TSolution>[] solutionCache;
    private final MemorySlots localSlots;
    private final SolutionReader<TSolution> solutionReader;
    private WriteableExperience<TSolution, ?> WriteableExperience;
    private SearchSpace<TSolution> searchSpace = new DummySearchSpace<>();
    private final int[][] others;
    private final int[] cdfI;
    private Generator<TSolution, Double> objectiveGenerator;
    private int totalMemory = 0;
    private double smallEval = Double.POSITIVE_INFINITY;

    /**
     *
     * @param initialMemory
     * @param localPolicy
     * @param solutionReader
     */
    public ProxyMemory(int initialMemory, MemoryExchangePolicy localPolicy, SolutionReader<TSolution> solutionReader) {
        this.solutionReader = solutionReader;
        MemoryExchangePolicy[] policies = MemoryExchangePolicy.values();
        int s = Communication.getCommunication().getSize();
        int r = Communication.getCommunication().getRank();
        int[][] out = new int[1][2];
        out[0][0] = initialMemory;
        out[0][1] = localPolicy.ordinal();
        this.others = new int[s][];
        this.localSlots = localPolicy.generateSender(initialMemory);
        this.solutionCache = (MemorySlots<TSolution>[]) Array.newInstance(this.localSlots.getClass().getSuperclass(), s);
        this.solutionCache[0x00] = this.localSlots;
        Communication.Allgather(CommMode.MpiBlocking, out, 0, 1, MPI.OBJECT, this.others, 0, 1, MPI.OBJECT);
        int sum = this.others[r][0], ni;
        cdfI = new int[s];
        cdfI[0] = sum;
        int i = 1;
        for (int j = r + 1; j < s; i++, j++) {
            ni = this.others[j][0];
            sum += ni;
            solutionCache[i] = policies[this.others[j][1]].generateReceiver(ni);
            cdfI[i] = sum;
        }
        for (int j = 0; j < r; i++, j++) {
            ni = this.others[j][0];
            sum += ni;
            solutionCache[i] = policies[this.others[j][1]].generateReceiver(ni);
            cdfI[i] = sum;
        }
        this.totalMemory = sum;
        //Communication.Log(Arrays.deepToString(this.others)+" with "+Arrays.toString(solutionCache));
    }

    /**
     *
     * @return
     */
    public int getMemorySize() {
        return this.totalMemory;
    }

    /**
     *
     * @return
     */
    public int getLocalMemorySize() {
        return this.localSlots.getSize();
    }

    /**
     *
     * @param index
     * @return
     */
    public TSolution getSolution(int index) {
        int ii = Utils.getLengthIndex(this.cdfI, index + 1);
        int ij = index;
        if (ii > 0) {
            ij -= this.cdfI[ii - 1];
        }
        return this.solutionCache[ii].getSolution(ij);
    }

    /**
     *
     * @param index
     * @param value
     */
    public void setSolution(int index, TSolution value) {
        this.getWriteableExperience().join(value);
        double eval = this.getObjectiveGenerator().generate(value);
        Communication.logFileTime(LoggingParameters.LOG_MEMORY_SET, LoggingParameters.LOG_MEMORY_SET_TEXT, index, eval);
        if (eval < smallEval) {
            Communication.logFileTime(LoggingParameters.LOG_MEMORY_BETTER, LoggingParameters.LOG_MEMORY_BETTER_TEXT, index, eval);
            this.smallEval = eval;
        }
        this.localSlots.setSolution(index, value);
    }

    //TODO: carefull with heuristics applied to global solution space
    /**
     *
     * @param heuristic
     * @param from
     * @param to
     */
    public void applyHeuristic(Heuristic<TSolution> heuristic, int from, int to) {
        if (from == to) {
            heuristic.applyHeuristicLocally(this.getSolution(from));
            pushSolution(to);
        } else {
            this.setSolution(to, heuristic.applyHeuristic(this.getSolution(from)));
        }
    }

    /**
     *
     * @param heuristic
     * @param from1
     * @param from2
     * @param to
     */
    public void applyHeuristic(Heuristic<TSolution> heuristic, int from1, int from2, int to) {
        if (from1 == to) {
            heuristic.applyHeuristicLocally(this.getSolution(from1), this.getSolution(from2));
            pushSolution(to);
        } else {
            this.setSolution(to, heuristic.applyHeuristic(this.getSolution(from1), this.getSolution(from2)));
        }
    }

    private void pushSolution(int to) {
        this.getWriteableExperience().join(this.getSolution(to));
        this.localSlots.pushSolution(to);
    }

    /**
     *
     * @param index
     * @return
     */
    public Solution peekSolution(int index) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private int rankToIndex(int rank) {
        int myr = Communication.getCommunication().getRank();
        if (rank > myr) {
            return rank - myr;
        } else {
            int siz = Communication.getCommunication().getSize();
            return siz - myr + rank;
        }
    }

    /**
     * @return the WriteableExperience
     */
    public WriteableExperience<TSolution, ?> getWriteableExperience() {
        return WriteableExperience;
    }

    /**
     * @param WriteableExperience the WriteableExperience to set
     */
    public void setWriteableExperience(WriteableExperience<TSolution, ?> WriteableExperience) {
        this.WriteableExperience = WriteableExperience;
    }

    /**
     * @param searchSpace the searchSpace to set
     */
    public void setSearchSpace(SearchSpace<TSolution> searchSpace) {
        if (searchSpace != null) {
            this.searchSpace = searchSpace;
        } else {
            this.searchSpace = new DummySearchSpace<>();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public int[] getPacketTags() {
        return new int[]{PushSenderBase.SendTag};
    }

    /**
     *
     * @param from
     * @param tag
     * @param data
     * @throws IOException
     */
    @Override
    public void receivePacket(int from, int tag, Object data) throws IOException {
        Object[] buffer = (Object[]) data;
        //Communication.Log("received " + Arrays.toString(buffer));
        try (ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) buffer[2]); DataInputStream dis = new DataInputStream(bais)) {
            TSolution sol = solutionReader.readAndGenerate(dis);
            searchSpace.correct(sol);
            solutionCache[rankToIndex((int) buffer[0])].receiveSolution((int) buffer[1], sol);
        } catch (Exception e) {
            Communication.log(e);
        }
    }

    /**
     *
     * @return
     */
    public CompactBitArray getExchangeBlockingMask() {
        return this.localSlots.getBlockingMask();
    }

    /**
     *
     * @param from
     * @param to
     */
    public void copySolution(int from, int to) {
        this.setSolution(to, this.getSolution(from).clone());
    }

    /**
     *
     * @param generator
     */
    public void initializeProxyMemory(SolutionGenerator<TSolution> generator) {
        MemorySlots ms;
        for (int i = 1; i < solutionCache.length; i++) {
            ms = this.solutionCache[i];
            int J = ms.getSize();
            for (int j = 0; j < J; j++) {
                ms.receiveSolution(j, generator.generateSolution());
            }
        }
    }

    /**
     * @return the objectiveGenerator
     */
    public Generator<TSolution, Double> getObjectiveGenerator() {
        return objectiveGenerator;
    }

    /**
     * @param objectiveGenerator the objectiveGenerator to set
     */
    public void setObjectiveGenerator(Generator<TSolution, Double> objectiveGenerator) {
        this.objectiveGenerator = objectiveGenerator;
    }
    private static final Logger LOG = Logger.getLogger(ProxyMemory.class.getName());
}
