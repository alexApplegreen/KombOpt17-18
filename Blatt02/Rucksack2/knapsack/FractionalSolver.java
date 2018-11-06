package knapsack;

import java.util.*;

public class FractionalSolver implements SolverInterface {

    /**
     * inner class to store information about indices when calculating
     * the quotient from instance data
     */
    private class Tupel {

        private Double quotient;
        private int index;

        public Tupel(double quotient, int index) {
            this.quotient = new Double(quotient);
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public Double getQuotient() {
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
            Double v = new Double(instance.getValue(i));
            Double w = new Double(instance.getWeight(i));
            // catch division by 0 errors
            if (w == 0.0) {
                quotients.add(i, new Tupel(v, i));
            }
            else {
                // TODO integer division is performed
                quotients.add(i, new Tupel((v / w), i));
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

        Double scale = new Double(0.0);
        // iterate over elements and add from best to worst
        for (int i = 0; i < quotients.size(); i++) {
            scale = 1.0;
            int index = quotients.get(i).getIndex();
            // if next object fits whole, pack max of 1
            if (s.getWeight() < instance.getCapacity()) {
                s.set(index, scale);
                if (!s.isFeasible()) {
                    s.set(index, 0.0);
                }
            }
            else {
                // if next elememt does not fit, shrink it down and add it.
                Double w = new Double(s.getWeight());
                Double w_max = new Double(instance.getCapacity());
                Double wi = new Double(instance.getWeight(i));
                scale = (w - w_max) / wi;
                Logger.println("Scale: " + scale);
                assert scale > 0 : "Calculation Error";
                s.set(index, scale);
            }
        }
        return s;
    }
}