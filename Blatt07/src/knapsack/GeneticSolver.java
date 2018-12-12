package knapsack;

import java.util.*;

enum CrossoverStrategy {
    onePoint,
    twoPoint,
}

/**
 * Solver Using genetic Algorithms
 */
public class GeneticSolver implements SolverInterface {

    /**
     * number of iterations to perform
     */
    private int iterations;

    /**
     * strategy to use for mutating
     */
    private CrossoverStrategy strategy;

    /**
     * chance in % by which a mutation will happen
     */
    private int mutationChance;

    /**
     * desired size of population,
     * exceeding will result in decemating the popultation (selective)
     */
    private int populationSize;

    /**
     * a set to represent the population
     */
    private HashMap<Integer, Solution> population;

    // Constructor
    public GeneticSolver(int iterations, int mutationChance, int populationSize, CrossoverStrategy strategy) {
        if (iterations < 0) {
            throw new IllegalArgumentException("iterations must be greater than 0");
        }
        if (mutationChance > 100 || mutationChance < 0) {
            throw new IllegalArgumentException("Mutation chance must be 0 < chance < 100");
        }
        if (populationSize < 0) {
            throw new IllegalArgumentException("size of population cannot be negative");
        }
        this.iterations = iterations;
        this.mutationChance = mutationChance;
        this.strategy = strategy;
        this.populationSize = populationSize;
        this.population = new HashMap<>();
    }

    @Override
    public Object solve(Instance instance) {
        Solution mother = generateRandomFeasible(instance);
        Solution father = generateRandomFeasible(instance);
        population.put(mother.getValue(), mother);
        population.put(father.getValue(), father);
         switch (strategy) {

             case twoPoint:
                 for (int i = 0; i < iterations; i++) {
                     Solution[] fittest = findBest(instance);
                     mother = fittest[0];
                     father = fittest[1];
                     Solution child = twoPointCrossover(mother, father, instance);
                     if ((Math.random() * (double) mutationChance / 100) >= mutationChance) {
                         child = mutate(child, instance);
                     }
                     population.put(child.getValue(), child);
                     if (population.size() >= populationSize) {
                         decimatePopulation();
                     }
                 }
                 Logger.println("trials: " + iterations);
                 Logger.println("Population size: " + populationSize);
                 Logger.println("Mutation chance: " + mutationChance + "%");
                 Logger.println("Weight: " + findBest(instance)[0].getWeight());
                 return findBest(instance)[0];

             case onePoint:
                 for (int i = 0; i < iterations; i++) {
                     Solution[] fittest = findBest(instance);
                     mother = fittest[0];
                     father = fittest[1];
                     Solution child = onePointCrossover(mother, father, instance);
                     if ((Math.random() * (double) mutationChance / 100) >= mutationChance) {
                         child = mutate(child, instance);
                     }
                     population.put(child.getValue(), child);
                     if (population.size() >= populationSize) {
                         decimatePopulation();
                     }
                 }
                 Logger.println("Weight: " + findBest(instance)[0].getWeight());
                 Logger.println("trials: " + iterations);
                 Logger.println("Population size: " + populationSize);
                 return findBest(instance)[0];
         }
         return null;
    }

    /**
     * searches population for fittest objects and returns them
     * @param instance knapsack problem
     * @return array of 2 fittest objects
     */
    private Solution[] findBest(Instance instance) {
        Solution best, second;
        List<Integer> fitness = new ArrayList<>();
        fitness.addAll(population.keySet());
        Collections.sort(fitness);
        best = population.get(fitness.get(fitness.size() - 1));
        second = population.get(fitness.get(fitness.size() - 2));
        return new Solution[] {best, second};
    }

    /**
     * generates new Solution based on genetic crossing of predecessors
     * Child might not be Feasiable!
     * @param mother genetic predecessor 1
     * @param father genetic predecessor 2
     * @param instance knapsack problem
     * @return product of genetic crossing
     */
    private Solution onePointCrossover(Solution mother, Solution father, Instance instance) {
        Solution child = new Solution(instance);
        int pivot;
        do {
            pivot = (int)(Math.random() * instance.getSize());
        } while (pivot <= 1 || pivot >= instance.getSize() - 2);
        Logger.println("Using pivot: " + pivot);
        int i = 0;
        for (; i < pivot; i++) {
            child.set(i, mother.get(i));
        }
        for (; i < instance.getSize(); i++) {
            child.set(i, father.get(i));
        }
        return child;
    }

    /**
     * generates new Solution based on two point crossover of predecessors
     * child might not be Feasible!
     * @param mother genetic predecessor 1
     * @param father genetic predecessor 2
     * @param instance knapsack problem
     * @return product of genetic crossing
     */
    private Solution twoPointCrossover(Solution mother, Solution father, Instance instance) {
        Solution child = new Solution(instance);
        int pivot1, pivot2;
        do {
            pivot1 = (int)(Math.random() * instance.getSize()) + instance.getSize() / 2;
        } while (pivot1 >= instance.getSize() - 3);

        do {
            pivot2 = (int)(Math.random() * pivot1);
        } while (pivot2 <= 2);

        int i = 0;
        for (; i < pivot2; i++) {
            child.set(i, mother.get(i));
        }
        for (; i < pivot1; i++) {
            child.set(i, father.get(i));
        }
        for (; i < instance.getSize(); i++) {
            child.set(i, mother.get(i));
        }
        return child;
    }

    /**
     * generate random feasible solution
     * @param instance knapsacl problem
     * @return knapsack solution
     */
    private Solution generateRandomFeasible(Instance instance) {
        Solution solution = new Solution(instance);
        for (int i = 0; i < instance.getSize(); i++) {
            if (Math.random() > 0.5) {
                solution.set(i, 1);
                if (!solution.isFeasible()) {
                    solution.unset(i);
                }
            }
        }
        return solution;
    }

    /**
     * flip random entry in bitvector
     * @param solution knapsack Solution
     * @param instance knapsack problem
     * @return knapsack Solution
     */
    private Solution mutate(Solution solution, Instance instance) {
        int pivot = (int)(Math.random() * instance.getSize());
        if (solution.get(pivot) == 1) {
            solution.unset(pivot);
        } else {
            solution.set(pivot, 1);
        }
        return solution;
    }

    /**
     * kill all members of population that are not feasible OR
     * below the average value
     */
    private void decimatePopulation() {
        int threshold = 0;
        int counter = 0;
        Collection<Solution> pop = population.values();
        if (!pop.isEmpty()) {
            for (Solution s : pop) {
                if (!s.isFeasible()) {
                    population.remove(s);
                }
                threshold += s.getValue();
                counter++;
            }
            threshold = threshold / counter;
            for (Solution s : pop) {
                if (s.getValue() < threshold) {
                    population.remove(s);
                }
            }
        }
    }
}
