import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws IOException {

        Hamming hamming = new Hamming();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Wybierz co chcesz zrobić: ");
        System.out.println("1. Chcę zakodować tekst: ");
        System.out.println("2. Chcę odkodować tekst: ");

        int choose = scanner.nextInt();

        switch (choose) {
            case 1:

                Scanner scanner1 = new Scanner(System.in);
                System.out.println("Podaj nazwe pliku (wraz z rozszerzeniem .txt)");
                String pathToRead = scanner1.nextLine();
                hamming.coding(pathToRead);
                System.out.println("Plik zakodowano pomyślnie.");
                break;
            case 2:
                Scanner scanner2 = new Scanner(System.in);
                System.out.println("Podaj nazwe pliku (wraz z rozszerzeniem .txt)");
                String pathToWrite = scanner2.nextLine();
                //haming.decoding(pathToWrite);
                System.out.println("Plik został odkodowany pomyślnie.");
                break;

        }



    }
}
