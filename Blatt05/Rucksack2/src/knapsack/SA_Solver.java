package knapsack;

enum Cooldown_function {
    linear,
    polynomial
}

/**
 * Optimization algorithm based on physical annealing of solids
 */
public class SA_Solver implements SolverInterface {

    /**
     * initital temperature
     */
    private double T;

    /**
     * constant by which temperature decreases
     */
    private double t_scale;

    /**
     * iterations per decrese of temperature
     */
    private int iterations_per_t;

    /**
     * strategy to decrease temperature
     */
    private Cooldown_function f;

    public SA_Solver(Cooldown_function f, double T, double t_scale, int iterations_per_t) {
        this.T = T;
        this.t_scale = t_scale;
        this.iterations_per_t = iterations_per_t;
        switch (f) {
            case polynomial: this.f = Cooldown_function.polynomial; break;
            case linear: this.f = Cooldown_function.linear; break;
        }
    }

    public SA_Solver() {
        this(Cooldown_function.linear, 100.0, 1.0, 10);
    }

    public void setT(double t) {
        T = t;
    }

    public void setIterations_per_t(int iterations_per_t) {
        this.iterations_per_t = iterations_per_t;
    }

    public void setT_scale(double t_scale) {
        this.t_scale = t_scale;
    }

    @Override
    public Solution solve(Instance instance) {
        // Solution to store best so far
        Solution solution = generate_initial(instance);

        switch (this.f) {
            case linear:
                // TODO
                break;
            case polynomial:
                // TODO
                break;
        }
        Logger.println("Weight: " + solution.getWeight());
        return solution;
    }

    private Solution generate_initial(Instance instance) {
        Solution s = new Solution(instance);
        String template = Integer.toBinaryString((int)(Math.random() * (Math.pow(2, instance.getSize()))));
        if (template.length() < instance.getSize()) {
            do {
                template = "0" + template;
            } while (template.length() != instance.getSize());
        }
        for (int i = 0; i < instance.getSize(); i++) {
            if (template.charAt(i) == '1') {
                s.set(i, 1);
            }
        }
        return s;
    }
}
