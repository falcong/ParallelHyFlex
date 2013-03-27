package parallelhyflex.memory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import parallelhyflex.communication.Communication;
import parallelhyflex.problemdependent.solution.Solution;
import parallelhyflex.problemdependent.heuristics.Heuristic;
import java.lang.reflect.Array;
import mpi.MPI;
import parallelhyflex.problemdependent.solution.SolutionReader;
import parallelhyflex.problemdependent.experience.WritableExperience;
import parallelhyflex.problemdependent.searchspace.DummySearchSpace;
import parallelhyflex.problemdependent.searchspace.SearchSpace;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class ProxyMemory<TSolution extends Solution<TSolution>> {

    private final MemorySlots<TSolution>[] solutionCache;
    private final MemorySlots localSlots;
    private final SolutionReader<TSolution> solutionReader;
    private WritableExperience<TSolution, ?> writableExperience;
    private SearchSpace<TSolution> searchSpace = new DummySearchSpace<>();
    private final int[][] others;
    private final int[] cdfI;
    private int totalMemory = 0;

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
        Communication.AG(out, 0, 1, MPI.OBJECT, this.others, 0, 1, MPI.OBJECT);
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
        new FetchThread().start();
    }

    public int getMemorySize() {
        return this.totalMemory;
    }

    public int getLocalMemorySize() {
        return this.localSlots.getSize();
    }

    public TSolution getSolution(int index) {
        int ii = Utils.getLengthIndex(this.cdfI, index);
        int ij = index;
        if (ii > 0) {
            ij -= this.cdfI[ii - 1];
        }
        return this.solutionCache[ii].getSolution(ij);
    }

    public void setSolution(int index, TSolution value) {
        //Communication.Log("" + (this.getWritableExperience() == null));
        this.getWritableExperience().join(value);
        this.localSlots.setSolution(index, value);
    }

    //TODO: carefull with heuristics applied to global solution space
    public void applyHeuristic(Heuristic<TSolution> heuristic, int from, int to) {
        if (from == to) {
            heuristic.applyHeuristicLocally(this.getSolution(from));
            pushSolution(to);
        } else {
            this.setSolution(to, heuristic.applyHeuristic(this.getSolution(from)));
        }
    }

    public void applyHeuristic(Heuristic<TSolution> heuristic, int from1, int from2, int to) {
        if (from1 == to) {
            heuristic.applyHeuristicLocally(this.getSolution(from1), this.getSolution(from2));
            pushSolution(to);
        } else {
            this.setSolution(to, heuristic.applyHeuristic(this.getSolution(from1), this.getSolution(from2)));
        }
    }

    private void pushSolution(int to) {
        this.getWritableExperience().join(this.getSolution(to));
        this.localSlots.pushSolution(to);
    }

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
     * @return the writableExperience
     */
    public WritableExperience<TSolution, ?> getWritableExperience() {
        return writableExperience;
    }

    /**
     * @param writableExperience the writableExperience to set
     */
    public void setWritableExperience(WritableExperience<TSolution, ?> writableExperience) {
        this.writableExperience = writableExperience;
    }

    /**
     * @param searchSpace the searchSpace to set
     */
    public void setSearchSpace(SearchSpace<TSolution> searchSpace) {
        if(searchSpace != null) {
            this.searchSpace = searchSpace;
        }
        else {
            this.searchSpace = new DummySearchSpace<>();
        }
    }

    private class FetchThread extends Thread {

        public FetchThread() {
            this.setDaemon(true);
        }

        @Override
        public void run() {
            Object[] buffer = new Object[3];
            TSolution sol;
            ByteArrayInputStream bais;
            DataInputStream dis;
            while (true) {
                try {
                    Communication.RV(buffer, 0, 3, MPI.OBJECT, MPI.ANY_SOURCE, 0);
                    //Communication.Log("received " + Arrays.toString(buffer));
                    bais = new ByteArrayInputStream((byte[]) buffer[2]);
                    dis = new DataInputStream(bais);
                    sol = solutionReader.readAndGenerate(dis);
                    searchSpace.correct(sol);
                    solutionCache[rankToIndex((int) buffer[0])].receiveSolution((int) buffer[1], sol);
                    dis.close();
                    bais.close();
                } catch (Exception e) {
                    Communication.Log(e);
                }
            }
        }
    }
}