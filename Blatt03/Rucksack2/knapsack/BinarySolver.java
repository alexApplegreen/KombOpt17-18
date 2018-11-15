package knapsack;

import java.util.*;

/**
 * Solution of a fractional knapsack problem
 *
 * @author Alexander Tepe, Marcel Joschko
 */

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

    /**
     * inner class which is representing one node of the search tree
     * if we start the calcSolution method
     */
    private class SolutionList {
        private List<Integer> list;
        private int UB;
        private int c;
        private boolean valid;

        public SolutionList(List<Integer> list) {
            this.list = new ArrayList<Integer>(list);
        }

        public void init() {
            this.valid = calcBounds(this, quotients);
        }

        /**
         * Check if the solution is feasible.
         */
        public boolean isFeasible() {
            int w = 0;
            for (int i = 0; i < z.list.size(); i++) {
                w += instance.getWeight(quotients.get(i).getIndex());
            }
            return w <= instance.getCapacity();
        }
    }

    private Instance instance;
    private Solution s;
    private List<Tupel> quotients;
    private List<Integer> zList;
    private List<Integer> bestSol;
    private SolutionList z;
    private int UB_MAX;
    private int c_MAX;

    public BinarySolver(Instance instance) {
        this.instance = instance;
        this.quotients = new ArrayList<Tupel>();
        this.zList = new ArrayList<Integer>();
        this.bestSol = new ArrayList<Integer>();
        this.s = new Solution(instance);
        this.z = new SolutionList(zList);
        this.UB_MAX = 0;
        this.c_MAX = 0;
    }


    /**
     * Calculates the "Upper-Bound" (UB) and the c* of the given node
     * @param z the given Node of the search tree
     * @param q the sorted quotients list
     * @return if the exceeds the capacity or not
     */
    public boolean calcBounds(SolutionList z, List<Tupel> q) {

        // checks if its feasible
        if (!z.isFeasible()) {
            return false;
        }

        int w = 0;
        int c = 0;
        int W = instance.getCapacity();

        // the actual value of the list
        for (int i = 0; i < z.list.size(); i++) {
            if (z.list.get(i) == 1) {
                w += instance.getWeight(q.get(i).getIndex());
                c += instance.getValue(q.get(i).getIndex());
            }
        }

        int index = 0;
        //what we could get if we pick the next best objects
        for (int i = z.list.size(); i < q.size(); i++) {
            if (w + instance.getWeight(q.get(i).getIndex()) <= W) {
                w += instance.getWeight(q.get(i).getIndex());
                c += instance.getValue(q.get(i).getIndex());
            }
            else {
                index = i;
                break;
            }

        }

        // also take the fractal part to the upper bound
        if ((index + 1) < q.size()) {
            if (w < W) {
                z.UB = (W - w) * instance.getValue(q.get(index + 1).getIndex())
                        / instance.getWeight(q.get(index + 1).getIndex())
                        + c;
            }
            else {
                z.UB = w;
            }
        }
        z.c = c;
        return true;
    }

    /**
     * Calculates recursive the best solution from the quotients list
     * @param z a new node of the search tree
     */
    public void calcSolution(SolutionList z) {

        // if the solution is not valid
        if(!z.valid) {
            return;
        }

        // if the UB and c are lower then the already found ones
        if(z.UB < UB_MAX && z.c < c_MAX) {
            return;
        }

        // update the best values
        if(z.UB > UB_MAX) {
            UB_MAX = z.UB;
            c_MAX = z.c;
        }

        // if the list is full and the UB and c are equal, we found our solution
        if(z.list.size() == quotients.size()) {
            if(z.c == c_MAX)
            {
                this.bestSol = new ArrayList<Integer>(z.list);
            }
            return;
        }

        // create new instances and fork with 1 and 0
        SolutionList a = new SolutionList(z.list);
        SolutionList b = new SolutionList(z.list);
        a.init();
        b.init();
        a.list.add(1);
        b.list.add(0);

        // recursive stuff
        if(a.UB >= b.UB) {
            calcSolution(a);
            calcSolution(b);
        }
        else {
            calcSolution(b);
            calcSolution(a);
        }
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

        // initialize the SolutionList and start the recursion
        this.z.init();
        calcSolution(z);

        // write the list in the solution
        int index = 0;
        for(int i = 0; i < instance.getSize(); i++) {
            index = quotients.get(i).getIndex();
            s.set(index, bestSol.get(i));
        }

        return s;
    }
}