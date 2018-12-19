import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Reading file: " + args[0]);
        Reader.read(args[0]);
    }
}
