package knapsack;


/**
 * Solver Using genetic Algorithms
 */
public class GeneticSolver implements SolverInterface {

    @Override
    public Object solve(Instance instance) {
        return null;
    }

    private Solution onePointCrossover(Solution mother, Solution father, Instance instance) {
        Solution child = new Solution(instance);
        return child;
    }

    private Solution twoPointCrossover(Solution mother, Solution father, Instance instance) {
        Solution child = new Solution(instance);
        return child;
    }
}
