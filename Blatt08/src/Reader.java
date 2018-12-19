import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Reader {

    public static void read(String filename) throws IOException {
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        char arr[];
        ArrayList<Character> num = new ArrayList<>();
        int nodes;
        int filePos = 0;

        ArrayList<Character> id1 = new ArrayList<>();
        ArrayList<Character> id2 = new ArrayList<>();
        ArrayList<Character> edgeCost = new ArrayList<>();

        nodes = Integer.parseInt(reader.readLine());
        System.out.println("Number of nodes: " + nodes);

        while ((line = reader.readLine()) != null) {
            int i = 0;
            int j = 0;
            for (; !Character.isSpaceChar(line.charAt(i)); i++) {
                id1.add(j, line.charAt(i));
                j++;
            }
            j = 0;
            i++;
            String s = id1.toString();
            s = s.substring(1, s.length() - 1);
            int v1 = Integer.parseInt(s);
            for (; !Character.isSpaceChar(line.charAt(i)); i++) {
                id2.add(j, line.charAt(i));
                j++;
            }
            j = 0;
            i++;
            s = id2.toString();
            s = s.substring(1, s.length() - 1);
            int v2 = Integer.parseInt(s);
            for (; i < line.length(); i++) {
                edgeCost.add(j, line.charAt(i));
            }
            j = 0;
            s = edgeCost.toString();
            s = s.substring(1, s.length() - 1);
            double cost = Double.parseDouble(s);

            System.out.println("read line: " + v1 + " " + v2 + " " + cost);
        }
    }
}
