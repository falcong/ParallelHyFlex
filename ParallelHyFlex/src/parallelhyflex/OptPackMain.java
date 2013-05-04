package parallelhyflex;

import parallelhyflex.problems.fdcsp.problem.FiniteIntegerDomain;

/**
 *
 * @author kommusoft
 */
public class OptPackMain {
    
    public static void main (String[] args) {
        FiniteIntegerDomain interval = new FiniteIntegerDomain(1,2);
        System.out.println(interval);
        interval.add(5,5);
        System.out.println(interval);
        interval.add(7,9);
        System.out.println(interval);
        interval.add(4,4);
        System.out.println(interval);
        interval.add(3,6);
        System.out.println(interval);
        FiniteIntegerDomain interval2 = new FiniteIntegerDomain();
        interval.minusWith(interval2);
        System.out.println(interval);
        interval2.add(4,5);
        interval.minusWith(interval2);
        System.out.println(interval);
        interval2.add(3,5);
        interval.minusWith(interval2);
        System.out.println(String.format("minus %s",interval2));
        System.out.println(interval);
    }
    
}