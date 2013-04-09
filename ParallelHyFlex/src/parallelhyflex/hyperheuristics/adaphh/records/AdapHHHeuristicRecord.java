package parallelhyflex.hyperheuristics.adaphh.records;

import parallelhyflex.algebra.Tabuable;
import parallelhyflex.hyperheuristics.records.EvaluatedHeuristicRecordBase;

/**
 *
 * @author kommusoft
 */
public class AdapHHHeuristicRecord extends EvaluatedHeuristicRecordBase implements Tabuable {

    private int cpbest = 0;
    private double fimp = 0.0d, fwrs = 0.0d;
    private double fpimp = 0.0d, fpwrs = 0.0d;
    private double tspent = 0.0d, tpspent = 0.0d;
    private final int tabuDurationOffset, tabuDurationLimit;
    private int tabuDuration;

    /**
     * 
     * @param heuristicIndex The index of the heuristic
     */
    public AdapHHHeuristicRecord(int heuristicIndex) {
        this(heuristicIndex, 5);
    }

    /**
     *
     * @param heuristicIndex The index of the heuristic
     * @param tabuDurationOffset According to the paper of Mustafa Misir, the tabu-duration must be set to sqrt(2*n) with n the number of heuristics.
     */
    public AdapHHHeuristicRecord(int heuristicIndex, int tabuDurationOffset) {
        this(heuristicIndex, tabuDurationOffset, 2*tabuDurationOffset);
    }
    
    /**
     *
     * @param heuristicIndex The index of the heuristic
     * @param tabuDurationOffset According to the paper of Mustafa Misir, the tabu-duration must be set to sqrt(2*n) with n the number of heuristics.
     */
    public AdapHHHeuristicRecord(int heuristicIndex, int tabuDurationOffset, int tabuDurationLimit) {
        super(heuristicIndex);
        this.tabuDurationOffset = tabuDurationOffset;
        this.tabuDurationLimit = tabuDurationLimit;
        this.tabuDuration = tabuDurationOffset;
    }

    public void newPhase() {
        this.setCpbest(0);
        this.setFpimp(0.0d);
        this.setFpwrs(0.0d);
        this.setTpspent(0.0d);
    }

    public void addTime(double dt) {
        this.setTspent(this.getTspent() + dt);
        this.setTpspent(this.getTpspent() + dt);
    }

    public void newBest() {
        this.setCpbest(this.getCpbest() + 1);
    }

    public void addImprovement(double df) {
        this.setFimp(this.getFimp() + df);
        this.setFpimp(this.getFpimp() + df);
    }

    public void addWorsening(double df) {
        this.setFwrs(this.getFwrs() + df);
        this.setFpwrs(this.getFpwrs() + df);
    }

    /**
     * @return the cpbest
     */
    public int getCpbest() {
        return cpbest;
    }

    /**
     * @param cpbest the cpbest to set
     */
    public void setCpbest(int cpbest) {
        this.cpbest = cpbest;
    }

    /**
     * @return the fimp
     */
    public double getFimp() {
        return fimp;
    }

    /**
     * @param fimp the fimp to set
     */
    public void setFimp(double fimp) {
        this.fimp = fimp;
    }

    /**
     * @return the fwrs
     */
    public double getFwrs() {
        return fwrs;
    }

    /**
     * @param fwrs the fwrs to set
     */
    public void setFwrs(double fwrs) {
        this.fwrs = fwrs;
    }

    /**
     * @return the fpimp
     */
    public double getFpimp() {
        return fpimp;
    }

    /**
     * @param fpimp the fpimp to set
     */
    public void setFpimp(double fpimp) {
        this.fpimp = fpimp;
    }

    /**
     * @return the fpwrs
     */
    public double getFpwrs() {
        return fpwrs;
    }

    /**
     * @param fpwrs the fpwrs to set
     */
    public void setFpwrs(double fpwrs) {
        this.fpwrs = fpwrs;
    }

    /**
     * @return the tspent
     */
    public double getTspent() {
        return tspent;
    }

    /**
     * @param tspent the tspent to set
     */
    public void setTspent(double tspent) {
        this.tspent = tspent;
    }

    /**
     * @return the tpspent
     */
    public double getTpspent() {
        return tpspent;
    }

    /**
     * @param tpspent the tpspent to set
     */
    public void setTpspent(double tpspent) {
        this.tpspent = tpspent;
    }

    public void incrementTabuDuration() {
        this.tabuDuration = Math.min(this.getTabuDuration()+1,this.tabuDurationLimit);
    }
    
    public void resetTabuDuration () {
        this.tabuDuration = this.tabuDurationOffset;
    }

    /**
     * @return the tabuDuration
     */
    public int getTabuDuration() {
        return tabuDuration;
    }
    
}
