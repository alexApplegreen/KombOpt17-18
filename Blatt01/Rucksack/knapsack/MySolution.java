package knapsack;

public class MySolution implements SolverInterface {

    // The problem
    private Instance instance;

    // stores a Solution for recursive looping
    private Solution s;

    /**
     * Constructor
     * @param instance the problem to solve
     */
    public MySolution(Instance instance) {
        this.instance = instance;
    }

    /**
     * solves the instance and returns a solution
     * @param instance the problem to solve
     * @return instance of Solution
     */
    @Override
    public Solution solve(Instance instance) {
        // TODO stop method from adding last element before loop exits
        Solution solution = new Solution(instance);
        int i = 0;
        while (solution.isFeasible()) {
            solution.set(i, 1);
            i++;
        }
        Logger.println("Weight: " + solution.getWeight());
        this.s = solution;
        return s;
    }

    /**
     * recursively solves the problem
     * @param s Solution to start off
     * @return the maximized solution
     */
    private Solution branchAndBound(Solution s) {
        return null;
    }
}