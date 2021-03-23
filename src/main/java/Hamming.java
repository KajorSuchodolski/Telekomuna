import java.io.*;
import java.sql.ResultSet;

public class Hamming {

    private byte[][] H = {
            {1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0},
            {1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1}};

    public void coding(File data) throws IOException {

        FileWriter encoded = new FileWriter("encodedFile.txt");

        char [] character = ReadFileToCharArray(data.getPath());
        int[] message = new int[8];
        int[] check = new int[8];

        for(int c = 0; c < character.length; c++) {

            for (int i = 0; i < 8; i++) {
                check[i] = 0;
            }

            for (int i = 7; i > -1; i--) {
                message[i] = character[c] % 2;
                character[c] /= 2;
            }

            for (int i = 0; i < 8; i++) {
                for(int j = 0; j < 8 ; j++) {
                    check[i] += message[j] * H[i][j];
                }
                check[i] %= 2;
            }

            for(int i = 0; i < 8; i++) {
                encoded.write(Integer.toString(message[i]));
            }
            for(int i = 0; i < 8; i++) {
                encoded.write(Integer.toString(check[i]));
            }
            encoded.write("\n");
        }
        encoded.close();

    }

    private static char[] ReadFileToCharArray(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[10];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();
        return  fileData.toString().toCharArray();
    }
}
