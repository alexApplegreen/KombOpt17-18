import java.util.ArrayList;

public class Solver {

    private Graph graph;

    public Solver(Graph g) {
        graph = g;
    }

    public ArrayList<Integer> solve(int ants, int iterations) {

        System.out.println("Iterations: " + iterations);
        System.out.println("number of ants: " + ants);
        ArrayList<Integer> best = new ArrayList<>();
        // Pheromon Matrix
        int[][] antsVisitedVertex = new int[ants][graph.getSize()];
        double lenBest = Integer.MAX_VALUE;

        // Iterations loop
        for (int i = 0; i < iterations; i++) {
            // Ants loop
            for (int j = 0; j < ants; j++) {
                int start;

                // generate random starting vertex which has an outdegree of > 0
                do {
                    System.out.println("generating start");
                    start = (int)(Math.random() * graph.getSize());
                    if (start == 0) {
                        start = 1;
                    }
                } while (graph.getGraph().get(start).isEmpty());

                ArrayList<Integer> path = new ArrayList<>();
                double length = 0;
                // add starting node to path
                path.add(0, start);

                // vertices loop
                for (int k = 1; k < graph.getSize(); k++) {

                    // choose strategy: either follow pheromones or look for shortest path (initial)
                    if (Math.random() > 0.5 || j == 0) {
                        int nearest = graph.getNearest(start);
                        length += graph.getEdgeLength(start, nearest);
                        // add step to path
                        path.add(k, nearest);
                        // increment level of pheromones
                        antsVisitedVertex[j][k]++;
                        // remove node from graph, so it cannot be visited again
                        graph.getGraph().remove(nearest);
                        if (nearest == -1) {
                            System.err.println("Sackgasse!");
                            System.out.println("Best Solution " + path + " Length: " + length);
                            return best;
                        }
                        // update current position
                        start = nearest;

                    } else {
                        // follow pheromones
                        int[] possibleNearest = antsVisitedVertex[j - 1];
                        int mostPheromones = 0;
                        int next = 0;

                        // linear search on path of last ant
                        for (int x = 0; x < graph.getSize(); x++) {
                            if (possibleNearest[x] > mostPheromones) {
                                mostPheromones = possibleNearest[x];
                                next = x;
                            }
                        }

                        // add step to path
                        path.add(k, next);
                        length += graph.getEdgeLength(start, next);
                        // increment level of pheromones
                        antsVisitedVertex[j][k]++;
                        // update current position
                        start = next;
                        // remove node from graph to prevent cycles
                        graph.getGraph().remove(next);
                    }
                }
                // look for best tracked result
                if (length < lenBest) {
                    lenBest = length;
                    best = new ArrayList<>();
                    best.addAll(path);
                }
            }

            // randomize level of pheromones
            for (int y = 0; y < ants; y++) {
                for (int j = 0; j < graph.getSize(); j++) {
                    antsVisitedVertex[y][j] = (int)(Math.random() * antsVisitedVertex[y][j]);
                }
            }

        }
        System.out.println("Best Solution: " + best + " Length: " + lenBest);
        return best;
    }
}
