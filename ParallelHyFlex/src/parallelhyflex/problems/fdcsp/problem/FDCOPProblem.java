package parallelhyflex.problems.fdcsp.problem;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import parallelhyflex.algebra.PinnedFlightWeight;
import parallelhyflex.algebra.collections.ArgumentIterable;
import parallelhyflex.algebra.collections.ArrayIterator;
import parallelhyflex.problemdependent.problem.ObjectiveFunction;
import parallelhyflex.problemdependent.problem.ProblemBase;
import parallelhyflex.problems.fdcsp.problem.expression.Expression;
import parallelhyflex.problems.fdcsp.problem.solution.FDCOPSolution;
import parallelhyflex.problems.fdcsp.problem.solution.FDCOPSolutionGenerator;
import parallelhyflex.utils.GenericUniqueRandomGenerator;
import parallelhyflex.utils.Utils;

/**
 *
 * @author kommusoft
 */
public class FDCOPProblem extends ProblemBase<FDCOPSolution, FDCOPSolutionGenerator> implements Iterable<Variable>, ArgumentIterable<Variable, FDCOPConstraint> {
    private static final PinnedFlightWeight<Integer, FDCOPObjectiveFunctionI> objectivesFw = new PinnedFlightWeight<>(FDCOPObjectiveFunctionIGenerator.getInstance());

    private final Variable[] variables;
    private final MutableFiniteIntegerDomain[] variableDomains;
    private final int[] domainSizes;
    private final Expression[] minimalisations;
    private final GenericUniqueRandomGenerator<Integer> variableSelector;

    /**
     *
     * @param variables
     * @param minimalisations
     */
    public FDCOPProblem(Variable[] variables, Expression[] minimalisations) {
        this.setSolutionGenerator(new FDCOPSolutionGenerator(this));
        //System.out.println(String.format("FDCOPP %s %s", Arrays.toString(variables), Arrays.toString(minimalisations)));
        variableSelector = new GenericUniqueRandomGenerator(Utils.sequence(0, variables.length));
        this.variables = variables;
        this.variableDomains = new MutableFiniteIntegerDomain[this.variables.length];
        this.minimalisations = minimalisations;
        reduceDomains();
        this.domainSizes = new int[variables.length];
        int index = 0;
        for (Variable var : variables) {
            this.variableDomains[index] = var.getDomain();
            this.domainSizes[index] = var.getDomain().size();
            var.setIndex(index++);
            /*System.out.print(String.format("%s in %s subject to", var, var.getDomain()));
            for(FDCOPConstraint c : var) {
                System.out.print(String.format("\t%s",c));
            }
            System.out.println();*/
        }
    }

    private HashSet<FDCOPConstraint> getConstraints() {
        HashSet<FDCOPConstraint> constraints = new HashSet<>();
        for (Variable v : variables) {
            for (FDCOPConstraint c : v) {
                constraints.add(c);
            }
        }
        return constraints;
    }

    /**
     *
     * @param index
     * @return
     */
    @Override
    public ObjectiveFunction<FDCOPSolution> getObjectiveFunction(int index) {
        return objectivesFw.generate(index);
    }

    /**
     *
     * @return
     */
    @Override
    public int getNumberOfObjectiveFunctions() {
        return this.minimalisations.length;
    }

    /**
     *
     * @param variableIndex
     * @return
     */
    public int getDomainSize(int variableIndex) {
        return this.domainSizes[variableIndex];
    }

    /**
     *
     * @param var
     * @return
     */
    public int getDomainSize(Variable var) {
        return this.domainSizes[var.getIndex()];
    }

    /**
     *
     * @param variableIndex
     * @return
     */
    public Variable getVariable(int variableIndex) {
        return this.variables[variableIndex];
    }

    /**
     *
     * @param variableIndex
     * @return
     */
    public MutableFiniteIntegerDomain getDomain(int variableIndex) {
        return this.variables[variableIndex].getDomain();
    }

    /**
     *
     * @return
     */
    public int getNumberOfVariables() {
        return this.variables.length;
    }

    /**
     *
     * @return
     */
    public int getNumberOfMinimalisations() {
        return this.minimalisations.length;
    }
    
    

    private void reduceDomains() {
        HashSet<FDCOPConstraint> toReduce = this.getConstraints();
        HashSet<FDCOPConstraint> toAdd = new HashSet<>();
        do {
            toAdd.clear();
            for (Iterator<FDCOPConstraint> it = toReduce.iterator(); it.hasNext();) {
                FDCOPConstraint c = it.next();
                it.remove();
                if (c.relaxDomains()) {
                    for (Variable v : c) {
                        for (FDCOPConstraint ca : v) {
                            if (!toReduce.contains(ca)) {
                                toAdd.add(ca);
                            }
                        }
                    }
                }
            }
            toReduce.addAll(toAdd);
        } while (!toAdd.isEmpty());
    }

    /**
     *
     * @param dos
     * @throws IOException
     */
    @Override
    public void write(DataOutputStream dos) throws IOException {
        //TODO
    }

    /**
     *
     * @return
     */
    @Override
    public Iterator<Variable> iterator() {
        return new ArrayIterator<>(this.variables);
    }

    /**
     *
     * @return
     */
    public Iterator<Variable> variableIterator() {
        return this.iterator();
    }

    /**
     *
     * @return
     */
    public Iterator<Expression> minimalisationIterator() {
        return new ArrayIterator<>(this.minimalisations);
    }

    /**
     *
     * @param argument
     * @return
     */
    @Override
    public Iterator<FDCOPConstraint> iterator(Variable argument) {
        return argument.iterator();
    }
}