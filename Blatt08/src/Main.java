import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Reading file: " + args[0]);
        Graph g = Reader.read(args[0]);
        Solver solver = new Solver(g);
        ArrayList<Integer> solution = solver.solve(100, 100);
        // do stuff with your solution
    }
}
