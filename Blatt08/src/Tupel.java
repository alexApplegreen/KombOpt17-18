public class Tupel {

    private double edgeCost;
    private int vertexID;

    public Tupel(int vertexID, double edgeCost) {
        this.edgeCost = edgeCost;
        this.vertexID = vertexID;
    }

    public double getEdgeCost() {
        return this.edgeCost;
    }

    public int getVertexID() {
        return this.vertexID;
    }
}
