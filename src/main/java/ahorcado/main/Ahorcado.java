package ahorcado.main;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author Hayran Andrés López González
 * @version 1.0
 */

public class Ahorcado {
    // Variables estáticas
    public static int MAX_ERRORS = 14; // Máximo de intentos
    public static String manString = "q(x_x)p"; // Dibujo ASCII del monigote

    // Dibuja el monigote en función de los intentos máximo y actual
    public static String drawMan(String manString, int maxErrors, int currErrors) {
        // Cantidad de caracteres a dibujar. Es la proporción entre errores actuales y
        // totales multiplicado sobre el largo del monigote
        int printCharactersCount = (int) ((double) currErrors / maxErrors * manString.length());

        // Define el monigote final a dibujar
        String finalManString = manString.substring(0, printCharactersCount);

        return finalManString;
    }

    // Dibuja la palabra en función de la lista de letras usadas
    public static String drawWord(String word, ArrayList<String> usedLetters) {
        String finalWord = "";

        // Si la lista de letras usadas está vacía, dibuja solo barras bajas
        if (usedLetters.isEmpty()) {
            for (int i = 0; i < word.length(); i++) {
                finalWord += "_ ";
            }
            return finalWord;
        }

        // Recorre la palabra letra a letra
        for (int i = 0; i < word.length(); i++) {
            // Verifica por cada letra de la lista de letras usadas
            for (int j = 0; j < usedLetters.size(); j++) {
                // Si la letra está en la liste de letras usadas, la añade a la palabra final
                // Si no, verifica si terminamos de revisar la lista, para poner una barra baja
                if (word.charAt(i) == usedLetters.get(j).charAt(0)) {
                    finalWord += word.charAt(i) + " ";
                    break;
                } else if (j == usedLetters.size() - 1) {
                    finalWord += "_ ";
                    break;
                }
            }
        }

        return finalWord;
    }

    // Verifica que la letra elegida esté en la palabra y no haya sido usada antes
    // 1: letra correcta, 0: letra ya evaluada antes, -1: letra incorrecta
    public static int checkChar(char letter, String word, ArrayList<String> usedLetters) {
        for (int i = 0; i < word.length(); i++) {
            for (int j = 0; j < usedLetters.size(); j++) {
                if (letter == usedLetters.get(j).charAt(0)) {
                    return 0;
                }
            }

            if (letter == word.charAt(i)) {
                return 1;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        Random r = new Random();
        Scanner s = new Scanner(System.in);

        // Variables
        ArrayList<String> usedLetters = new ArrayList<String>(); // Letras usadas
        int currErrors = 0; // Cantidad de errores actuales
        boolean lose = false; // Bandera al perder
        boolean win = false; // Bandera al ganar

        // Arreglo de palabras predefinidas
        String[] wordsArray = { "herencia", "instancia", "interfaz", "compilar", "mensajes", "privadas", "paquetes",
                "ejecutar", "propiedad", "abstracto" };

        // Palabra a adivinar (normal y versión en barras bajas)
        String word = wordsArray[r.nextInt(wordsArray.length)];

        System.out.println("--------------------------------------");
        System.out.println("> ¡Bienvenido al juego del ahorcado! <");
        System.out.println("--------------------------------------");

        // Repite el juego mientras no hayamos perdido ni ganado
        while (lose == false && win == false) {
            System.out
                    .println("\n" + drawWord(word, usedLetters) + "    " + drawMan(manString, MAX_ERRORS, currErrors));
            System.out.println("Ingresa una letra");

            // Letra elegida
            char letter = s.next().toLowerCase().charAt(0);

            // Verifica la letra y reacciona dependiendo del caso
            int checkResult = checkChar(letter, word, usedLetters);
            switch (checkResult) {
                // Letra correcta
                case 1:
                    System.out.println("Letra correcta");
                    usedLetters.add(String.valueOf(letter));
                    break;
                // Letra ya evaluada antes
                case 0:
                    System.out.println("Letra ya evaluada antes");
                    break;
                // Letra incorrecta
                case -1:
                    currErrors++;
                    System.out.printf("Letra incorrecta. Te quedan %d intentos\n", MAX_ERRORS - currErrors);
                    usedLetters.add(String.valueOf(letter));
                    break;
            }

            // Acciona la bandera de ganar o perder si es pertinente
            if (currErrors == MAX_ERRORS) {
                lose = true;
            } else if (drawWord(word, usedLetters).replace(" ", "").equals(word)) {
                win = true;
            }
        }

        // Si perdimos, el juego lo indica
        if (lose) {
            System.out.printf("\n> %s ¡Haz perdido! <", manString);
        }

        // Si ganamos, el juego lo indica también
        if (win) {
            System.out.println("\n> ¡Haz ganado! <");
        }

        s.close();
    }
}
