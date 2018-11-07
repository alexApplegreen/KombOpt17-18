package knapsack;

import java.util.*;

public class BinarySolver implements SolverInterface {

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
    private Solution s;
    private List<Tupel> quotients;

    public BinarySolver(Instance instance) {
        this.instance = instance;
        this.quotients = new ArrayList<Tupel>();
        this.s = new Solution(instance);
    }

    /**
     * Solve given instance
     * @param instance
     * @return Solution instance
     */
    public Solution solve(Instance instance) {
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

        int index = 0;
        for (int i = 0; i < instance.getSize(); i++) {
            index = quotients.get(i).getIndex();
            s.set(index, 1);
            if (!s.isFeasible()) {
                s.unset(index);
            }
        }
        Logger.println("Weight: " + s.getWeight());
        return s;
    }
}