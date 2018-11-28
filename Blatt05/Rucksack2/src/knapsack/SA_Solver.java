package knapsack;

import java.util.ArrayList;

enum Cooldown_function {
    linear,
    geometric
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

    /*
     * Constructor
     */
    public SA_Solver(Cooldown_function f, double T, double t_scale, int iterations_per_t) {
        if (T < 0) {
            throw new IllegalArgumentException("Temperature must be positive!");
        }

        switch (f) {
            case geometric:
                this.f = Cooldown_function.geometric;
                break;

            case linear:

                this.f = Cooldown_function.linear;
                break;
        }

        setT(T);
        setT_scale(t_scale);
        setIterations_per_t(iterations_per_t);
    }

    /*
     * setter methods
     */
    public SA_Solver() {
        this(Cooldown_function.geometric, 100.0, 0.9, 10);
    }

    public void setT(double t) {
        if (t < 0) {
            throw new IllegalArgumentException("Temperature cannot be negative");
        }
        T = t;
    }

    public void setIterations_per_t(int iterations_per_t) {
        if (iterations_per_t < 0) {
            throw new IllegalArgumentException("iterations per step cannot be negative");
        }
        this.iterations_per_t = iterations_per_t;
    }

    public void setT_scale(double t_scale) {
        switch (f) {
            case linear:
                if (t_scale < 0) {
                    throw new IllegalArgumentException("scale cannot be negative");
                }
                this.t_scale = t_scale;
                break;

            case geometric:
                if (t_scale < 0 || t_scale > 1) {
                    throw new IllegalArgumentException("scale must be 0 < scale < 1");
                }
                this.t_scale = t_scale;
                break;
        }
    }

    @Override
    public Solution solve(Instance instance) {
        // Solution to store best so far
        Solution s_star = generate_initial(instance);
        new Solution(instance);
        Solution temp;

        Logger.println("Temperature: " + T);
        Logger.println("Strategy: " + f);
        Logger.println("decrease in T: " + t_scale);
        Logger.println("iterations per step: " + iterations_per_t);

        switch (f) {
            case geometric:
                do {
                    for (int i = 0; i < iterations_per_t; i++) {
                        temp = generate_initial(instance);
                        if (temp.getValue() > s_star.getValue() ||
                                Math.random() < Math.pow(Math.E, -(temp.getValue() - s_star.getValue()) / T)) {

                            s_star = temp;
                            i++;
                        }
                    }
                    T *= t_scale;
                } while(T > 0.1);
                break;

            case linear:
                do {
                    for (int i = 0; i < iterations_per_t; i++) {
                        temp = generate_initial(instance);
                        if (temp.getValue() > s_star.getValue() ||
                                Math.random() < Math.pow(Math.E, -(temp.getValue() - s_star.getValue()) / T)) {

                            s_star = temp;
                            i++;
                        }
                    }
                    T -= t_scale;
                } while (T != 0);
                break;
        }

        Logger.printlnColor("Weight: " + s_star.getWeight());
        return s_star;
    }

    /**
     * generate random initial feasible solution
     * @param instance knapsack problem
     * @return knapsack solution
     */
    private Solution generate_initial(Instance instance) {
        new Solution(instance);
        Solution s;
        String template;

        do {
            template = Integer.toBinaryString((int) (Math.random() * (Math.pow(2, instance.getSize()))));
            template = binTrim(template, instance);
            s = binToSolution(template, instance);
        } while (!s.isFeasible());

        return binToSolution(template, instance);
    }

    /**
     * get a random neighbor from within radius
     * @param radius new Neighbor = template +/- radius
     * @param instance problem instance
     * @return knapsack Solution from neighbor
     */
    private Solution getNeighbor(Solution solution, int radius, Instance instance) {
        String template = "";

        for (int i = 0; i < instance.getSize(); i++) {
            if (solution.get(i) == 1) {
                template += "1";
            }
            else {
                template += "0";
            }
        }

        int in = Integer.parseInt(template, 2);

        if (Math.random() > 0.5) {
            in += radius;
        }
        else {
            in -= radius;
        }

        String sol = Integer.toBinaryString(in);
        sol = binTrim(sol, instance);

        return binToSolution(sol, instance);
    }

    /**
     * generates all n neighbors in direct neighborhood
     * @param radius int n specifies number of desired neighbors
     * @param instance knapsack problem
     * @return ArrayList of Knapsack Solutions
     */
    private ArrayList<Solution> getAllNeighbors(Solution solution, int radius, Instance instance) {
        ArrayList<Solution> neighbors = new ArrayList<>();
        String template = null;

        for (int i = 0; i < instance.getSize(); i++) {
            if (solution.get(i) == 1) {
                template += "1";
            }
            else {
                template += "0";
            }
        }

        int in = Integer.parseInt(template, 2);

        for (int i = 0; i < radius; i++) {
            int a = in + (i + 1);
            int b = in - (i - 1);

            String t1 = Integer.toBinaryString(a);
            neighbors.add(binToSolution(t1, instance));

            String t2 = Integer.toBinaryString(b);
            neighbors.add(binToSolution(t2, instance));
        }
        return neighbors;
    }

    /**
     * generates Knapsack Solution from bitvector
     * @param template bitvector
     * @param instance knapsack problem
     * @return knapsack Solution
     */
    private Solution binToSolution(String template, Instance instance) {
        Solution s = new Solution(instance);
        for (int i = 0; i < instance.getSize(); i++) {
            if (template.charAt(i) == '1') {
                s.set(i, 1);
            }
        }
        return s;
    }

    /**
     * trims bitvectorstring to right size
     * @param template bitvector
     * @param instance knapsack problem
     * @return trimmed String bitvector
     */
    private String binTrim(String template, Instance instance) {
        if (template.length() != instance.getSize()) {
            do {
                template += "0";
            } while (template.length() != instance.getSize());
        }
        return template;
    }
}
