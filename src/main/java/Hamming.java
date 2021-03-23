import java.io.*;

public class Hamming {

    /*
    * Macierz H (w tym przypadku o wymiarach 8x16) musi spełniać
    * następujące warunki:
    *
    * 1. Ma wszystkie kolumny różne
    * 2. Żadna z nich nie jest sumą dwóch innych
    * 3. Nie posiada kolumny zerowej
    *
    * Rozmiar jej wynika długośći słowa (8 bitów) oraz sumy długości
    * słowa wraz z bitami parzystości (8+8).
    *
    * */

    private final byte[][] H = {
            {1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0},
            {1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1}};



    //Funkcja ta pobiera z pliku znaki i zwraca jako wartosc tablice char
    private static char[] ReadFileToCharArray(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        char[] buf = new char[10];
        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();
        return  fileData.toString().toCharArray();
    }

    private char convertBinToString(int [] t) {

        // Funkcja zamieniająca tablice intów (bitów)
        int ascii = 128;
        char sign = 0;
        for (int i = 0; i < 8; i++) {
            sign += ascii * t[i];
            ascii /= 2;
        }
        return sign;
    }

    public void coding(String data) throws IOException {

        //Stworzenie nowego pliku do zapisu
        FileWriter encoded = new FileWriter("encodedFile.txt");

        char [] characters = ReadFileToCharArray(data); // Znaki pobrane z pliku źródłowego
        int[] wordBits = new int[8]; // Bity danego słowa
        int[] controlBits = new int[8]; // Bity parzystości

        for(int c = 0; c < characters.length; c++) {

            for (int i = 0; i < 8; i++) {
                controlBits[i] = 0;
            }

            // Pętla ma za zadanie przekonwertować dany znak na postać binarną
            for (int i = 7; i > -1; i--) {

                wordBits[i] = characters[c] % 2;
                characters[c] /= 2;
            }

            // W tym miejscu tworzymy bity parzystośći
            for (int i = 0; i < 8; i++) {
                for(int j = 0; j < 8 ; j++) {
                    controlBits[i] += wordBits[j] * H[i][j]; // Iloczyn miedzy bitami słowa i macierzy H
                }
                controlBits[i] %= 2; // Poniewaz operujemy na intach to musimy wykonac operacje modulo 2
            }

            // Zapisanie zakodowanej wiadomości wraz z bitami parzystości do pliku
            for(int i = 0; i < 8; i++) {
                encoded.write(Integer.toString(wordBits[i]));
            }
            for(int i = 0; i < 8; i++) {
                encoded.write(Integer.toString(controlBits[i]));
            }
            encoded.write("\n");
        }
        encoded.close();

    }

    public void decode(String data) throws IOException {

        FileWriter encoded = new FileWriter("decodedFile.txt"); // Tworzenie nowego pliku do zapisu

        char [] characters = ReadFileToCharArray(data); // Pobranie bitów z pliku
        int [] encodedFileArray = new int [16]; // Zakodowany 16 bitowy znak
        int [] mistakesArray = new int [8]; // Tablica przechowujaca bledy

        int counter = 0; // Licznik uzywany w celu zapisania bitow
        int mistakes = 0; // Ilosc błędów

        for(int c = 0; c < characters.length; c++) {

            // Idź do przodu dopóki nie napotkasz końca linii (kopiujemy tylko słowo)
            if(characters[c] != '\n' ) {

                // Zamieniamy 0 i 1 - 48 to 0 w postaci ASCII
                encodedFileArray[counter] = characters[c] - 48;
                counter++;

            } else {


                // Indeksy błędnych bitów (pierwszego oraz drugiego)
                int firstBit;
                int secondBit;

                for(int i = 0; i < 8; i++) {
                    // Zerujemy tablice, w celu wyeliminowania błędów przy kompilacji
                    mistakesArray[i] = 0;
                    for(int j = 0; j < 16; j++) {

                        // Tutaj mnożymy macierz T przez macierz H
                        mistakesArray[i] += encodedFileArray[j] * H[i][j];
                    }
                    // Działanie z modulo daje albo 0 albo 1
                    mistakesArray[i] %= 2;

                    // Sprawdzanie czy znaleziono błąd
                    if(mistakesArray[i] == 1) {
                        mistakes = 1;
                    }
                }

                if(mistakes != 0) {

                    int exists = 0;
                    for(int i = 0; i < 15; i++) {
                        for(int j = i + 1; j < 16; j++) {
                            exists = 1;
                            for(int k = 0; k < 8; k++) {

                                // Wykonyujemy operacje XOR i przyrównujemy dla warunku
                                if(mistakesArray[k] != (H[k][i] ^ H[k][j])) {

                                    // Wartosc tego argumentu dla 0 oznacza jeden błąd, 1 oznacza 2 błędy
                                    exists = 0;
                                    break;
                                }
                            }
                            if(exists == 1) {

                                // Wartosc indeksu bitów jest równa wartości iteracji
                                firstBit = i;
                                secondBit = j;

                                // Poprawiamy błędne bity wykorzystując operator warunkowy
                                encodedFileArray[firstBit] = encodedFileArray[firstBit] == 0 ? 1 : 0;
                                encodedFileArray[secondBit] = encodedFileArray[secondBit] == 0 ? 1 : 0;
                                // I kończymy pętlę (wychodzimy z najwyzszej, pierwszej) i wychodzimy z aktualnej
                                i = 16;
                                break;
                            }
                        }
                    }

                    if (mistakes == 1) {
                        for(int i = 0; i < 16; i++) {
                            for(int j = 0; j < 8; j++) {
                                if(H[j][i] != mistakesArray[j]) {
                                    break;
                                }
                                if(j == 7) {
                                    // Poprawiamy błędny bit
                                    encodedFileArray[i] = encodedFileArray[i] == 0 ? 1 : 0;
                                    // I kończymy pętlę (wychodzimy z najwyzszej, pierwszej)
                                    i = 16;
                                }
                            }
                        }
                    }
                }
                // Zerowanie wartośći w celu wykorzystania do następnych iteracji
                counter = 0;
                mistakes = 0;

                char information = convertBinToString(encodedFileArray);
                encoded.write(Character.toString(information));
            }
        }
        encoded.close();
    }


}
