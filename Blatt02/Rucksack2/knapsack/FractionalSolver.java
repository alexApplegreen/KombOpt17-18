package knapsack;

import java.util.*;

public class FractionalSolver implements SolverInterface {

    private class Tupel {

        private double quotient;
        private int index;

        public Tupel(double quotient, int index) {
            this.quotient = quotient;
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public double getQuotient() {
            return quotient;
        }
    }

    private Instance instance;
    private FractionalSolution s;
    private List<Tupel> quotients;

    public FractionalSolver(Instance instance) {
        this.instance = instance;
        this.quotients = new ArrayList<Tupel>();
        this.s = new FractionalSolution(instance);
    }

    public FractionalSolution solve(Instance instance) {
        // TODO implement this
        // write quotients of value and weight to ArrayList
        for (int i = 0; i < instance.getSize(); i++) {
            // catch division by 0 errors
            if (instance.getWeight(i) == 0) {
                quotients.add(i, new Tupel(instance.getValue(i), i));
            }
            else {
                quotients.add(i, new Tupel((instance.getValue(i) / instance.getWeight(i)), i));
            }
        }

        Collections.sort(quotients, new Comparator() {
            public int compare(Object a, Object b) {
                Tupel x = (Tupel)a;
                Tupel y = (Tupel)b;
                if (x.getQuotient() > y.getQuotient()) {
                    return -1;
                }
                else if (x.getQuotient() == y.getQuotient()) {
                    return 0;
                }
                else {
                    return 1;
                }
            }
        });

        for (int i = 0; i < quotients.size(); i++) {
            double scale = 1.0;
            int index = quotients.get(i).getIndex();
            // TODO this is doint weird shit
            if (instance.getWeight(index) < instance.getCapacity()) {
                s.set(index, scale);
                if (!s.isFeasible()) {
                    s.set(index, 0.0);
                }
            }
            else {
                // if nect elememt does not fot, shrink it down and add it.
                i++;
            }
        }
        return s;
    }
}