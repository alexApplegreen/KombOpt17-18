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

    private class ListSol {
        private List<Integer> list;
        private int UB;
        private int c_star;

        public ListSol(List<Integer> list) {
            this.list = new ArrayList<Integer>(list);
            calcBounds(this, quotients);
        }
    }

    private Instance instance;
    private Solution s;
    private List<Tupel> quotients;
    private List<Integer> temp;
    private ListSol l;
    private int UB_MAX;
    private int c_star_MAX;


    public BinarySolver(Instance instance) {
        this.instance = instance;
        this.quotients = new ArrayList<Tupel>();
        this.temp = new ArrayList<Integer>();
        this.l = new ListSol(temp);
        this.s = new Solution(instance);
        this.UB_MAX = 0;
        this.c_star_MAX = 0;
    }

    public void calcBounds(ListSol t, List<Tupel> q) {
        int w = 0;
        int W = instance.getCapacity();
        int c = 0;
        int i;
        Logger.println(t.list.get(0));
        for(i = 0; i < t.list.size(); i++) {
            if(t.list.get(i) == 1) {
                w += instance.getWeight(q.get(i).getIndex());
                c += instance.getValue(q.get(i).getIndex());
            }
        }
        for(int j = i; j < q.size(); j++) {

            if(w + instance.getWeight(q.get(j).getIndex()) <= W) {
                w += instance.getWeight(q.get(j).getIndex());
                c += instance.getValue(q.get(j).getIndex());
            }
            else {
                if (w < W) {
                    t.UB = (W - w) * instance.getValue(q.get(j + 1).getIndex())
                                      / instance.getWeight(q.get(j + 1).getIndex())
                                      + c;
                }
                else {
                    t.UB = w;
                }
                t.c_star = c;
                return;
            }
        }
    }

    public void calcSolution(ListSol z) {

        if(z.UB < UB_MAX && z.c_star < c_star_MAX) {
            return;
        }

        if(z.list.size() == quotients.size() && z.UB == UB_MAX && z.c_star == c_star_MAX) {

        }

        ListSol a = new ListSol(z.list);
        ListSol b = new ListSol(z.list);

        a.list.add(1);
        b.list.add(0);
        if(a.UB >= b.UB) {
            if(a.c_star >= b.c_star){
                calcSolution(a);
            }
            else
                calcSolution(a);
                calcSolution(b);

        }
        else {
            calcSolution(b);
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

        // take the first objects where its still feasible
        /*
        int index = 0;
        for (int i = 0; i < instance.getSize(); i++) {
            index = quotients.get(i).getIndex();
            s.set(index, 1);
            if (!s.isFeasible()) {
                s.unset(index);
            }
        }
        */
        calcSolution(l);
        return s;
    }
}