package parallelhyflex;

import java.lang.reflect.Array;
import java.util.Arrays;
import mpi.MPI;

/**
 *
 * @author kommusoft
 */
public class ProxyMemory<TSolution extends Solution<TSolution>> {
    
    private final MemorySlots<TSolution>[] solutionCache;
    private final MemorySlots localSlots;
    private final int[][] others;
    private int totalMemory = 0;

    public ProxyMemory(int initialMemory, MemoryExchangePolicy localPolicy) {
        MemoryExchangePolicy[] policies = MemoryExchangePolicy.values();
        int s = Communication.getCommunication().getSize();
        int r = Communication.getCommunication().getRank();
        int[][] out = new int[1][2];
        out[0][0] = initialMemory;
        out[0][1] = localPolicy.ordinal();
        this.others = new int[s][];
        this.localSlots = new MemorySlots<TSolution>(true,initialMemory,localPolicy);
        this.solutionCache = (MemorySlots<TSolution>[]) Array.newInstance(this.localSlots.getClass(),s);
        this.solutionCache[0x00] = this.localSlots;
        Communication.AG(out,0, 1, MPI.OBJECT, this.others, 0, 1, MPI.OBJECT);
        int sum = 0, ni;
        int i = 1;
        for(int j = r+1; j < s; i++, j++) {
            ni = this.others[j][0];
            sum += ni;
            solutionCache[i] = new MemorySlots<TSolution>(false,ni,policies[this.others[j][1]]);
        }
        sum += this.others[r][0];
        for(int j = 0; j < r; i++, j++) {
            ni = this.others[j][0];
            sum += ni;
            solutionCache[i] = new MemorySlots<TSolution>(false,ni,policies[this.others[j][1]]);
        }
        this.totalMemory = sum;
        System.out.println(""+r+" in says "+Arrays.deepToString(this.others)+" with "+Arrays.toString(solutionCache));
    }
    
    public int getMemorySize () {
        return this.totalMemory;
    }
    
    public int getLocalMemorySize () {
        return this.localSlots.getSize();
    }

    protected TSolution getSolution(int index) {
        //return (TSolution) this.innerMemory[index];
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected void setSolution(int index, TSolution value) {
        this.localSlots.setSolution(index,value);
    }

    //TODO: carefull with heuristics applied to global solution space
    protected void applyHeuristic(Heuristic<TSolution> heuristic, int from, int to) {
        if(from == to) {
            heuristic.applyHeuristicLocally(this.getSolution(from));
            pushSolution(to);
        }
        else {
            this.setSolution(to, heuristic.applyHeuristic(this.getSolution(from)));
        }
    }

    protected void applyHeuristic(Heuristic<TSolution> heuristic, int from1, int from2, int to) {
        if(from1 == to) {
            heuristic.applyHeuristicLocally(this.getSolution(from1),this.getSolution(from2));
            pushSolution(to);
        }
        else {
            this.setSolution(to, heuristic.applyHeuristic(this.getSolution(from1), this.getSolution(from2)));
        }
    }

    private void pushSolution(int to) {
        this.localSlots.pushSolution(to);
    }

    public Solution peekSolution(int index) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
