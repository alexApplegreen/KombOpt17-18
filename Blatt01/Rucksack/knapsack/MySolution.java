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
        // generate initial solution
        Solution solution = new Solution(instance);
        int i = 0;
        do {
            solution.set(i, 1);
            if (!solution.isFeasible()) {
                solution.set(i, 0);
                break;
            }
            else {
                i++;
            }
        } while(solution.isFeasible());
        Logger.println("Weight: " + solution.getWeight());
        this.s = solution;
        return s;
    }
}