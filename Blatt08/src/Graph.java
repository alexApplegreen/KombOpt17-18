import java.util.*;

/**
 * represents a weighted graph using a HashMap
 */
public class Graph {

    /**
     * graph representation
     */
    private HashMap<Integer, ArrayList<Tupel>> graph;

    // Constructor
    public Graph() {
        this.graph = new HashMap<>();
    }

    /**
     * insert a new neighborhood
     * @param id1 vertex 1
     * @param edgeCost weight
     * @param id2 vertex 2
     */
    public void setAdjacency(int id1, double edgeCost, int id2) {
        Tupel neighbor = new Tupel(id2, edgeCost);
        // if vertex 1 is already known, add a Tupel to his list of neighbors
        if (graph.containsKey(id1)) {
            graph.get(id1).add(neighbor);
        } else {
            // else create a new neighborhood
            ArrayList<Tupel> list = new ArrayList<>();
            list.add(neighbor);
            graph.put(id1, list);
        }
    }

    /**
     * grants reference to underlying graph structure
     * @return HashMap
     */
    public HashMap<Integer, ArrayList<Tupel> > getGraph() {
        return this.graph;
    }

    /**
     * getter for number of nodes inside graph
     * @return int size of HashMap
     */
    public int getSize() {
        return graph.size();
    }

    /**
     * getter for amount of neighbors of a vertex
     * @param id int vertexID
     * @return int number of Tupels stored for vertex
     */
    public int getNumNeighbors(int id) {
        if (!graph.containsKey(id)) {
            return -1;
        }
        return graph.get(id).size();
    }

    /**
     * searches for nearest possible neighbor of a vertex
     * @param id int VertexID
     * @return int ID of nearest neighbor
     */
    public int getNearest(int id) {
        List<Tupel> tupel = graph.get(id);
        if (!graph.containsKey(id)) {
            return -1;
        }
        // sort weights of neighborhoods ascending
        tupel.sort((o1, o2) -> (int) (o1.getEdgeCost() - o2.getEdgeCost()));
        // first Tupel holds the ID of closest neighbor
        return tupel.get(0).getVertexID();
    }

    /**
     * lookup for weight of edge
     * @param id1 vertex1
     * @param id2 vertex2
     * @return double cost
     */
    public double getEdgeLength(int id1, int id2) {
        if (graph.containsKey(id1)) {
            ArrayList<Tupel> tupel = graph.get(id1);
            for (Tupel t : tupel) {
                if (t.getVertexID() == id2) {
                    return t.getEdgeCost();
                }
            }
        }
        return -1.0;
    }
}
