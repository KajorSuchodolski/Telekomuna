import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws IOException {

        File file = new File("test.txt");

        Hamming hamming = new Hamming();
        hamming.coding(file);



    }
}
