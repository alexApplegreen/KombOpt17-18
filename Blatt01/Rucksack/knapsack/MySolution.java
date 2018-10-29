package knapsack;

import java.util.ArrayList;

public class MySolution implements SolverInterface {

    // The problem
    private Instance instance;

    // Array of quantities to generate all permutations of solutions
    private ArrayList<int[]> solutions;

    private ArrayList<Solution> results;

    /**
     * Constructor
     * @param instance the problem to solve
     */
    public MySolution(Instance instance) {
        this.instance = instance;
        this.solutions = new ArrayList<int[]>();
        this.results = new ArrayList<Solution>();
    }

    /**
     * solves the instance and returns a solution
     * @param instance the problem to solve
     * @return instance of Solution
     */
    @Override
    public Solution solve(Instance instance) {
        generateSolutions();
        // set items according to generated quantity list
        for (int[] item : solutions) {
            Solution s = new Solution(instance);
            for (int i = 0; i < item.length; i++) {
                s.set(i, item[i]);
            }
            results.add(s);
        }
        // linear search over all results to find best
        int maxVal = 0;
        int solutionIndex = 0;
        int counter = 0;
        for (Solution sol : results) {
            if (sol.isFeasible() && sol.getValue() > maxVal) {
                maxVal = sol.getValue();
                solutionIndex = counter;
            }
            counter++;
        }
        return results.get(solutionIndex);
    }

    private void generateSolutions() {
        // create array of n zeros
        int[] a = new int[instance.getSize()];
        int[] b = new int[a.length];
        for (int i = 0; i < instance.getSize(); i++) {
            a[i] = 0;
            b[i] = 1;
        }
        solutions.add(a);
        solutions.add(b);
        // fire up permutations
        permutate(a, a.length - 1);
    }

    private void permutate(int[] a, int index) {
        // recursion anchor
        if (index > 0) {
            for (int i = index; i > 0; i--) {
                if (a[i] == 0) {
                    int[] n = new int[a.length];
                    for (int j = 0; j < a.length; j++) {
                        n[j] = a[j];
                    }
                    n[i] = 1;
                    solutions.add(n);
                    permutate(n, i);
                }
                else {
                    solutions.add(a);
                }
            }
        }
    }
}