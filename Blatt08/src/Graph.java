import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

    private HashMap<Integer, ArrayList<Tupel>> graph;
    private ArrayList<Vertex> vertices;

    public Graph() {
        this.graph = new HashMap<>();
        this.vertices = new ArrayList<Vertex>();
    }

    public void setAdjacency(int id1, double edgeCost, int id2) {
        Tupel neighbor = new Tupel(id2, edgeCost);
        if (vertices.contains(new Vertex(id1))) {
            Vertex tmp = vertices.get(id1);
            tmp.addNeighbor(neighbor);
            graph.get(id1).add(neighbor);
        } else {
            Vertex v = new Vertex(id1);
            v.addNeighbor(neighbor);
            ArrayList<Tupel> list = new ArrayList<>();
            list.add(neighbor);
            graph.put(id1, list);
        }
    }

    public HashMap<Integer, ArrayList<Tupel> > getGraph() {
        return this.graph;
    }
}
