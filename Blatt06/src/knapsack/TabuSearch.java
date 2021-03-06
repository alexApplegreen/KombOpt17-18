package knapsack;

import java.util.ArrayList;
import java.util.Date;

enum Criteria {
    counter,
    timeout
}

/**
 * algorithm that implements tabusearch
 */
public class TabuSearch implements SolverInterface {

    /**
     * Tabu List
     */
    private ArrayList<Solution> tabuList;

    /**
     * step counter, time to use in seconds
     */
    private int counter;

    /**
     * specifies what strategy to use
     */
    private Criteria criteria;

    /**
     * specifies whether to generate only feasibly solutions while solving
     */
    private boolean useFeasibleOnly;

    // Constructor
    public TabuSearch(Criteria c, int max_c, boolean useFeasibleOnly) {
        this.tabuList = new ArrayList<>();
        if (max_c < 0) {
            throw new IllegalArgumentException("max_c cannot be negative");
        }
        this.counter = max_c;
        this.criteria = c;
        this.useFeasibleOnly = useFeasibleOnly;
    }

    public void setUseFeasibleOnly(boolean useFeasibleOnly) {
        this.useFeasibleOnly = useFeasibleOnly;
    }

    /**
     * generates solution based on tabusearch
     * @param instance The given knapsack instance
     * @return Solution
     */
    @Override
    public Solution solve(Instance instance) {

        Logger.println("Strategy: " + criteria);
        Logger. println("maximum trials: " + counter);
        Logger.println("use feasible only: " + useFeasibleOnly);
        Solution solution = generateRandom(instance);
        new Solution(instance);
        // best solution so far.
        Solution temp;
        temp = solution;

        switch (criteria) {

            case counter:

                int left;
                for (int i = 0; i < counter; i++) {

                    if (useFeasibleOnly) {
                        solution = generateFeasible(instance);
                        if (!isTabu(solution) || solution.getValue() > temp.getValue()) {
                            temp = solution;
                            tabuList.add(solution);
                        }
                        else {
                            tabuList.add(solution);
                        }
                    }

                    else {
                        solution = generateRandom(instance);
                        if (!isTabu(solution) || solution.getValue() > temp.getValue()) {
                            temp = solution;
                            tabuList.add(solution);
                        }
                        else {
                            tabuList.add(solution);
                        }
                    }
                    left = counter - i;
                    System.out.printf("\r%s", "Trials left: " + left);
                }
                Logger.println("\nWeight: " + temp.getWeight());

                return temp;

            case timeout:

                long end = new Date().getTime() + counter * 1000;
                long time_left = end;
                time_left = time_left / 1000;
                long current_time;

                do {
                    if (useFeasibleOnly) {
                        solution = generateFeasible(instance);
                        // Aspiration criteria: value must be better than local maximum
                        if (!isTabu(solution) || solution.getValue() > temp.getValue()) {
                            temp = solution;
                            tabuList.add(solution);
                        }
                        else {
                            tabuList.add(solution);
                        }
                    }

                    else {
                        solution = generateRandom(instance);
                        if (!isTabu(solution) || solution.getValue() > temp.getValue()) {
                            temp = solution;
                            tabuList.add(solution);
                        }
                        else {
                            tabuList.add(solution);
                        }
                    }
                    current_time = new Date().getTime();
                    time_left = (end - current_time) / 1000;
                    System.out.printf("\r%s", "time left: " + time_left + "s");

                } while (current_time < end);

                Logger.println("\nWeight: " + temp.getWeight());

                return temp;

            default:
                Logger.println("no criteria found");
                return null;
        }
    }

    /**
     * checks tabulist for occurence of solution
     * @param item knapsack solution
     * @return true if item is located in tabulist
     */
    private boolean isTabu(Solution item) {
        for (Solution s : tabuList) {
            if (s.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * generates random feasible solution
     * @return knapsack Solution
     */
    private Solution generateFeasible(Instance instance) {
        Solution solution = new Solution(instance);
        for (int i = 0; i < instance.getSize(); i++) {
            if (Math.random() > 0.5) {
                if (instance.getWeight(i) + solution.getWeight() > instance.getCapacity()) {
                    i++;
                } else {
                    solution.set(i, 1);
                }
            } else {
                i++;
            }
        }
        return solution;
    }

    /**
     * generates random feasible AND unfeasible solutions
     * @param instance knapsack problem
     * @return knapsack Solution
     */
    private Solution generateRandom(Instance instance) {
        Solution solution = new Solution(instance);
        String template = Integer.toBinaryString((int) (Math.random() * Math.pow(2, instance.getSize())));
        template = trimBinary(template, instance);
        for (int i = 0; i < template.length(); i++) {
            if (template.charAt(i) == '1') {
                solution.set(i, 1);
            }
        }
        return solution;
    }

    /**
     * trim vitvector string to needed length
     * @param template bitvector
     * @param instance knapsack problem
     * @return string bitvector
     */
    private String trimBinary(String template, Instance instance) {
        if (template.length() != instance.getSize()) {
            do {
                template += "0";
            } while (template.length() != instance.getSize());
        }
        return template;
    }
}
