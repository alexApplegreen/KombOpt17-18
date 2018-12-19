import java.util.ArrayList;

public class Vertex {

    private int id;
    private int outdegree;
    private ArrayList<Tupel> neighbors;

    public Vertex(int id) {
        this.id = id;
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(Tupel neighbor) {
        this.neighbors.add(neighbor);
    }

    public int getId() {
        return id;
    }

    public int getOutdegree() {
        return outdegree;
    }

    public int getNeighbors(int pos) {
        return neighbors.get(pos).getVertexID();
    }
}
