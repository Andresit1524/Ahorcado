package ahorcado.main;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author Hayran Andrés López González
 * @version 1.1
 */

public class Ahorcado {
    public static int MAX_ERRORS = 7; // Máximo de errores permitidos
    public static String hangmanString = "q(x_x)p"; // Dibujo ASCII del monigote

    // Arreglo de palabras predefinidas para elegir
    public static String[] wordsArray = { "herencia", "instancia", "interfaz", "compilar", "mensajes", "privadas",
            "paquetes", "ejecutar", "propiedad", "abstracto" };

    /**
     * Dibuja la figura del ahorcado en función del número de intentos y el límite
     * de errores permitidos
     */
    public static String drawHangman(String hangmanFigure, int maxAttempts, int currentAttempts) {
        // Calcula la proporción de la figura a dibujar basándose en los intentos
        int charsToDraw = (int) ((double) currentAttempts / maxAttempts * hangmanFigure.length());

        // Obtiene la parte de la figura que se debe mostrar
        String hangmanPart = hangmanFigure.substring(0, charsToDraw);

        return hangmanPart;
    }

    /**
     * Dibuja la palabra en función de la lista de letras usadas. La lista también
     * incluye letras que son elecciones incorrectas. El truco para que funcione,
     * entonces, está en que esas letras no están en la palabra (por eso son
     * incorrectas) y por ende pasan derecho el test sin afectar el resultado
     */
    public static String drawWord(String word, ArrayList<String> usedLetters) {
        String finalWord = "";

        // Recorre por cada letra de la palabra
        for (int i = 0; i < word.length(); i++) {
            // Bandera que indica que se encontró la letra
            boolean found = false;

            // Verifica por cada letra de la lista de letras usadas
            for (int j = 0; j < usedLetters.size(); j++) {
                // Si la letra está en la lista, la añade a la palabra final y se sale del bucle
                if (word.charAt(i) == usedLetters.get(j).charAt(0)) {
                    finalWord += word.charAt(i) + " ";
                    found = true;
                    break;
                }
            }

            // Si no se encontró la letra, pone una barra baja
            if (!found) {
                finalWord += "_ ";
            }
        }

        return finalWord;
    }

    /**
     * Verifica que la letra elegida esté en la palabra y no haya sido usada antes.
     * Res => 1: letra correcta, 0: letra ya evaluada antes, -1: letra incorrecta
     */
    public static int checkChar(char letter, String word, ArrayList<String> usedLetters) {
        // Verifica que la letra fue usada ya y retorna 0 en ese caso
        for (int i = 0; i < usedLetters.size(); i++) {
            if (letter == usedLetters.get(i).charAt(0)) {
                return 0;
            }
        }

        // Si no, busca en la palabra a por la letra y retorna 1 si la encuentra
        for (int i = 0; i < word.length(); i++) {
            if (letter == word.charAt(i)) {
                return 1;
            }
        }

        // Si no la encuentra... -1
        return -1;
    }

    /**
     * Imprime la lista de letras usadas en el orden en el que se usaron
     */
    public static String printUsedLetters(ArrayList<String> usedLetters) {
        String finalString = "";

        for (int i = 0; i < usedLetters.size(); i++) {
            finalString += usedLetters.get(i);
        }

        return finalString;
    }

    public static void main(String[] args) {
        Random r = new Random();
        Scanner s = new Scanner(System.in);

        // Indica si se repite el juego
        boolean repeat = true;

        System.out.println("--------------------------------------");
        System.out.println("> ¡Bienvenido al juego del ahorcado! <");
        System.out.println("--------------------------------------");

        // Repite el juego mientras queramos repetir
        while (repeat) {
            // Variables
            ArrayList<String> usedLetters = new ArrayList<String>(); // Lista de letras usadas
            int currErrors = 0; // Cantidad de errores actuales

            // Palabra a adivinar
            String word = wordsArray[r.nextInt(wordsArray.length)];

            // Repite hasta que no ganemos o perdamos
            while (true) {
                // Dibuja la palabra actual y el ahorcado
                System.out.printf("\n%s\t%s\t%s\n", drawWord(word, usedLetters),
                        drawHangman(hangmanString, MAX_ERRORS, currErrors), printUsedLetters(usedLetters));
                System.out.println("Ingresa una letra");

                // Letra elegida
                char letter = s.next().toLowerCase().charAt(0);

                // Verifica la letra y reacciona dependiendo del caso
                int checkResult = checkChar(letter, word, usedLetters);
                switch (checkResult) {
                    // Letra correcta: añade la letra como usada
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

                // Si llegamos al límite de errores, perdemos
                if (currErrors == MAX_ERRORS) {
                    System.out.printf("\n> %s ¡Haz perdido! la palabra era: %s <\n", hangmanString, word);
                    break;
                }

                // Si la palabra (sin los espacios añadidos) coincide con la adivinada, ganamos
                if (drawWord(word, usedLetters).replace(" ", "").equals(word)) {
                    System.out.printf("\n> ¡Haz ganado! la palabra es: %s <\n", word);
                    break;
                }
            }

            // Le pedimos al jugador si quiere repetir
            System.out.println("\n¿Quieres jugar de nuevo? (s/n)");
            char choice = ' ';

            // Exige al jugador una respuesta entre si o no (s/n)
            while (choice != 's' && choice != 'n') {
                choice = s.next().toLowerCase().charAt(0);

                if (choice == 's') {
                    System.out.println("\n> ¡Genial! juguemos de nuevo <");
                } else if (choice == 'n') {
                    System.out.println("\n> ¡Gracias por jugar! <");
                    repeat = false;
                } else {
                    System.out.println("Escribe una opción válida\n");
                }
            }
        }

        s.close();
    }
}
