package parallelhyflex.problems.circlepositioning.solution;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import parallelhyflex.communication.serialisation.SerialisationUtils;
import parallelhyflex.problemdependent.solution.Solution;
import parallelhyflex.problems.circlepositioning.CirclePositioningUtils;
import parallelhyflex.problems.circlepositioning.problem.CirclePositioningProblem;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class CirclePositioningSolution implements Solution<CirclePositioningSolution> {

    private final double[] positions;
    private double overlapArea;
    private double outerArea;

    public CirclePositioningSolution(double[] positions, double overlapArea, double outerArea) {
        this.positions = positions;
        this.overlapArea = overlapArea;
        this.outerArea = outerArea;
    }

    public CirclePositioningSolution(double[] positions, double[] radia, double largeRadius) {
        this(positions, 0.0d, 0.0d);
        this.overlapArea = CirclePositioningUtils.calculateOverlap(radia, positions);
        this.outerArea = CirclePositioningUtils.calculateOuter(largeRadius, radia, positions);
    }

    public CirclePositioningSolution(double[] positions, CirclePositioningProblem problem) {
        this(positions, problem.getRadia(), problem.getLargeCircleRadius());
    }

    public double getXi(int index) {
        return this.positions[index << 0x01];
    }

    public double getYi(int index) {
        return this.positions[(index << 0x01) + 0x01];
    }

    public double getR2i(int index) {
        int i2 = index << 0x01;
        double x = this.positions[i2];
        double y = this.positions[i2 + 0x01];
        return x * x + y * y;
    }

    public double getRi(int index) {
        return Math.sqrt(this.getR2i(index));
    }

    public double getThetai(int index) {
        int i2 = index << 0x01;
        double x = this.positions[i2];
        double y = this.positions[i2 + 0x01];
        return Math.atan2(y, x);
    }

    public double calculateOverlap(CirclePositioningProblem problem) {
        return CirclePositioningUtils.calculateOverlap(problem.getRadia(), this.positions);
    }

    public double calculateOuter(CirclePositioningProblem problem) {
        return CirclePositioningUtils.calculateOuter(problem.getLargeCircleRadius(), problem.getRadia(), this.positions);
    }

    public void moveCircle(CirclePositioningProblem problem, int index, double dx, double dy) {
        int i = index << 0x01;
        double x = positions[i++] + dx;
        double y = positions[i] + dy;
        this.setCircle(problem, index, x, y);
    }

    public void setCircle(CirclePositioningProblem problem, int index, double x3, double y3) {
        double[] radia = problem.getRadia();
        int i = index << 0x01;
        double x1 = positions[i++];
        double y1 = positions[i];
        double r13 = radia[index];
        double R = problem.getLargeCircleRadius();
        this.outerArea += CirclePositioningUtils.calculateDifferenceOuter(R, x3, y3, r13, x1, y1);
        this.overlapArea += CirclePositioningUtils.calculateDifferenceOverlap(this.positions, index, radia, x3, y3, r13, x1, y1);
    }

    @Override
    public CirclePositioningSolution clone() {
        double[] values = new double[this.getPositions().length];
        System.arraycopy(this.getPositions(), 0, values, 0, this.getPositions().length);
        return new CirclePositioningSolution(values, this.getOverlapArea(), this.getOuterArea());
    }

    @Override
    public boolean equalSolution(CirclePositioningSolution other) {
        double[] va = this.getPositions();
        double[] vb = other.getPositions();
        int n = va.length;
        if (n != vb.length || Math.abs(this.getOverlapArea() - other.getOverlapArea()) > Utils.Tolerance) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (Math.abs(va[i] - vb[i]) > Utils.Tolerance) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasFastDifferenceWith(CirclePositioningSolution other) {
        return false;
    }

    @Override
    public void read(DataInputStream dis) throws IOException {
        SerialisationUtils.readDoubleArray(dis, positions);
        this.overlapArea = dis.readDouble();
        this.outerArea = dis.readDouble();
    }

    @Override
    public void write(DataOutputStream dos) throws IOException {
        SerialisationUtils.writeDoubleArray(dos, positions);
        dos.writeDouble(this.overlapArea);
        dos.writeDouble(this.getOuterArea());
    }

    /**
     * @return the positions
     */
    public double[] getPositions() {
        return positions;
    }

    /**
     * @return the overlapArea
     */
    public double getOverlapArea() {
        return overlapArea;
    }

    /**
     * @return the outerArea
     */
    public double getOuterArea() {
        return outerArea;
    }

    public double getDefaultEvaluation() {
        return this.getOuterArea() + this.getOverlapArea();
    }

    public void swapCircle(CirclePositioningProblem problem, int index0, int index1) {
        int i20 = index0 << 0x01;
        int i21 = index1 << 0x01;
        double[] pos = this.positions;
        double[] rad = problem.getRadia();
        double x0 = pos[i20];
        double y0 = pos[i20 + 0x01];
        double r0 = rad[index0];
        double x1 = pos[i21];
        double y1 = pos[i21 + 0x01];
        double r1 = rad[index1];
        double dr = r1 - r0;
        int ind0 = Math.min(index0, index1);
        int ind1 = Math.min(index0, index1);
        double R = problem.getLargeCircleRadius();
        this.overlapArea += CirclePositioningUtils.calculateDifferenceOverlapRadius(pos, ind0, ind1, rad, x0, y0, r0, dr) + CirclePositioningUtils.calculateDifferenceOverlapRadius(pos, ind0, ind1, rad, x1, y1, r1, -dr);
        this.outerArea += CirclePositioningUtils.calculateDifferenceOuterRadius(R, x0, y0, r0, r1) + CirclePositioningUtils.calculateDifferenceOuterRadius(R, x1, y1, r1, r0);
        pos[i20] = x1;
        pos[i20 + 0x01] = y1;
        pos[i21] = x0;
        pos[i21 + 0x01] = y0;
    }
}
