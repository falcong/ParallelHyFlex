package parallelhyflex.hyperheuristics.records;


/**
 * 
 * @author kommusoft
 */
public abstract class HeuristicRecordEvaluatorBase<THeuristicRecord extends HeuristicRecord> implements HeuristicRecordEvaluator<THeuristicRecord> {

    /**
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(THeuristicRecord o1, THeuristicRecord o2) {
        return ((Double) this.generate(o1)).compareTo(this.generate(o2));
    }
    
}
