import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Class to parse TSP.txt files
 */
public class Reader {

    /**
     * reads a txt file and creates a graph instance
     * @param filename
     * @return Graph
     * @throws IOException if file is not found
     */
    public static Graph read(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        char arr[];
        ArrayList<Character> num = new ArrayList<>();
        int nodes;
        int filePos = 0;

        Graph g = new Graph();

        nodes = Integer.parseInt(reader.readLine());

        while ((line = reader.readLine()) != null) {
            String s = "";
            int i = 0;
            int j = 0;
            for (; !Character.isSpaceChar(line.charAt(i)); i++) {
                s += line.charAt(i);
                j++;
            }
            j = 0;
            i++;
            int v1 = Integer.parseInt(s);
            s = "";

            for (; !Character.isSpaceChar(line.charAt(i)); i++) {
                s += line.charAt(i);
                j++;
            }
            j = 0;
            i++;
            int v2 = Integer.parseInt(s);
            s = "";

            for (; i < line.length(); i++) {
                s += line.charAt(i);
            }
            j = 0;
            double cost = Double.parseDouble(s);

            g.setAdjacency(v1, cost, v2);
            s = "";
        }
        return g;
    }
}
