package parallelhyflex.problems.frequencyassignment.problem;

import java.io.DataInputStream;
import java.io.IOException;
import parallelhyflex.algebra.DoubleUpperMatrix;
import parallelhyflex.communication.serialisation.SerialisationUtils;
import parallelhyflex.problemdependent.problem.ProblemReader;
import parallelhyflex.problems.frequencyassignment.solution.FrequencyAssignmentSolution;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class FrequencyAssignmentProblemGenerator implements ProblemReader<FrequencyAssignmentSolution, FrequencyAssignmentProblem> {
    
    @Override
    public FrequencyAssignmentProblem readAndGenerate(DataInputStream dis) throws IOException {
        int nTransceivers = dis.readInt();
        int nSectors = dis.readInt();
        int[][] frequencies = SerialisationUtils.readIntArray2d(dis);
        DoubleUpperMatrix means = new DoubleUpperMatrix(0x00);
        means.read(dis);
        DoubleUpperMatrix stdevs = new DoubleUpperMatrix(0x00);
        stdevs.read(dis);
        int[] placement = SerialisationUtils.readIntArray(dis);
        return new FrequencyAssignmentProblem(nTransceivers, nSectors, frequencies, means, stdevs, placement);
    }
    
    public FrequencyAssignmentProblem generateProblem() {
        int n = Utils.nextInt(8000);
        int m = Utils.nextInt((int) Math.ceil(Math.sqrt(n)));
        int l = Utils.nextInt((int) Math.ceil(0.5d*Math.sqrt(n)));
        int[][] freqs = new int[n][];
        int[] placement = new int[n];
        for(int i = 0x00; i < n; i++) {
            int k = Utils.nextInt((int) Math.ceil(2.0d*Math.sqrt(m)));
            int[] vals = new int[k];
            for(int j = 0x00; j < k; j++) {
                vals[j] = Utils.nextInt(l);
            }
            freqs[i] = vals;
            placement[i] = Utils.nextInt(m);
        }
        DoubleUpperMatrix means = DoubleUpperMatrix.generateRandomGaussian(m, 0.0d, 1.0d);
        DoubleUpperMatrix sigmas = DoubleUpperMatrix.generateRandomAbsoluteGaussian(m, 1.0d, 1.0d);
        return new FrequencyAssignmentProblem(n,m,freqs,means,sigmas,placement);
    }
}