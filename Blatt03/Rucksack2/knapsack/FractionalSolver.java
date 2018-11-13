package knapsack;

import java.util.*;

/**
 * Solution of a fractional knapsack problem
 *
 * @author Alexander Tepe, Marcel Joschko
 */

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

    /**
     * Solve given instance
     * @param instance
     * @return Solution instance
     */
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
                quotients.add(i, new Tupel((v / w), i));
            }
        }

        // sort quotients arraylist descending
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

        // take the first objects where its still feasible
        // and also add the fractional part of the next element which is not feasible
        Double scale = new Double(1.0);
        int index;
        for (int i = 0; i < quotients.size(); i++) {
            scale = 1.0;
            index = quotients.get(i).getIndex();
            s.set(index, 1.0);
            if (!s.isFeasible()) {
                s.unset(index);

                Double w_max = new Double(instance.getCapacity());
                Double w_sum = new Double(s.getWeight());
                Double w_next = new Double(instance.getWeight(index));

                scale = Math.abs(w_sum - w_max) / w_next;
                assert scale > 0.0 : "Calculation error";
                s.set(index, scale);
            }
        }
        Logger.println("Weight: " + s.getWeight());
        return s;
    }
}