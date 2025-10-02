// Para funcionar fuera del proyecto de NetBeans, comenta o borra esta línea
package ahorcado.main;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Implementa la lógica para un juego del ahorcado basado en consola. Incluye un
 * monigote del ahorcado en ASCII, cantidad de errores máximos y indicador de
 * letras usadas.
 *
 * @author Hayran Andrés López González
 * @version 1.3.3
 */
public class Ahorcado {
    Random r = new Random();
    Scanner s = new Scanner(System.in);

    private static final int MAX_ERRORS = 7; // Máximo de errores permitidos
    private static final String HANGMAN_STRING = "q(x_x)p"; // Dibujo ASCII del ahorcado

    // Lista de palabras predefinidas para jugar
    private static final String[] wordsArray = { "herencia", "instancia", "interfaz", "compilar", "mensajes",
            "privadas", "paquetes", "ejecutar", "propiedad", "abstracto" };

    private String word; // Palabra a adivinar
    private ArrayList<Character> usedLetters; // Lista de letras que el usuario ya ha intentado
    private int currErrors; // Cantidad de errores actuales

    public Ahorcado() {
        word = wordsArray[r.nextInt(wordsArray.length)];
        usedLetters = new ArrayList<Character>();
        currErrors = 0;
    }

    /**
     * Dibuja la palabra en función de la lista de letras usadas, mostrando barras
     * bajas donde no se ha adivinado.
     * 
     * Sin embargo, la lista de letras usadas también incluye letras que son
     * elecciones incorrectas. El truco para que funcione esta función, entonces,
     * está en que esas letras no están en la palabra (por eso son incorrectas) y
     * por ende son ignoradas sin afectar el resultado.
     */
    private String drawWord() {
        String finalWord = "";

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);

            // Si la letra está en la lista, la añade a la palabra final
            // Si no, pone una barra baja
            // También coloca un espacio después de cada letra
            if (usedLetters.contains(letter)) {
                finalWord += letter + " ";
            } else {
                finalWord += "_ ";
            }

            // Alternativa con operador ternario ?:
            // finalWord += usedLetters.contains(letter) ? letter + " " : "_ ";
        }

        return finalWord;
    }

    /**
     * Dibuja la figura del ahorcado en función del número de errores cometidos y la
     * cantidad máxima de errores permitidos.
     */
    private String drawHangman(int errorCount) {
        // Calcula la proporción de la figura a dibujar basándose en los errores
        int charsToDraw = (int) ((double) errorCount / Ahorcado.MAX_ERRORS * Ahorcado.HANGMAN_STRING.length());

        return Ahorcado.HANGMAN_STRING.substring(0, charsToDraw);
    }

    /**
     * Convierte la lista de letras usadas en una cadena.
     */
    private String usedLettersString() {
        String finalString = "";

        for (int i = 0; i < usedLetters.size(); i++) {
            finalString += usedLetters.get(i);
        }

        return finalString;
    }

    /**
     * Solicita al usuario que ingrese una letra y valida:
     * 
     * 1. Que sea un solo caracter
     * 2. Que sea una letra
     */
    private char getUserChar() {
        // Repite hasta que el usuario ingrese una única letra válida
        while (true) {
            System.out.printf("Ingresa una letra\n> ");
            String userChar = s.next().toLowerCase();

            if (userChar.length() != 1) {
                System.out.println("Escribe un solo caracter\n");
                continue;
            }

            char gotChar = userChar.charAt(0);

            if (Character.isLetter(gotChar)) {
                return gotChar;
            }

            System.out.println("Entrada no válida. Solo se admiten letras\n");
        }
    }

    /**
     * Verifica que la letra elegida esté en la palabra y no haya sido usada antes.
     * 
     * 1: letra correcta.
     * 0: letra ya evaluada antes.
     * -1: letra incorrecta.
     */
    private int checkChar(char letter) {
        if (usedLetters.contains(letter)) {
            return 0;
        }

        if (word.contains(String.valueOf(letter))) {
            return 1;
        }

        return -1;
    }

    /**
     * Contiene la lógica para ejecutar una única partida del ahorcado. El bucle se
     * interrumpe cuando el jugador gana o pierde.
     * 
     * Nota: el uso de break como forma de salir del 'while (true)' permite mejoras
     * en el tamaño del código y a la lógica del mismo al gestionar directamente las
     * condiciones para ganar y perder.
     */
    private void play() {
        // Repite hasta que ganemos o perdamos (ver la nota arriba)
        while (true) {
            System.out.printf("\n%s\t%s\tLetras usadas: %s\n", drawWord(), drawHangman(currErrors),
                    usedLettersString());

            // Recibe la letra del usuario y la valida
            char letter = getUserChar();
            int checkResult = checkChar(letter);

            switch (checkResult) {
                case 1: // Letra correcta
                    System.out.println("Letra correcta");
                    usedLetters.add(letter);
                    break;
                case 0: // Letra ya evaluada
                    System.out.println("Letra ya evaluada antes");
                    break;
                case -1: // Letra incorrecta
                    currErrors++;
                    System.out.printf("Letra incorrecta. Te quedan %d intentos\n", MAX_ERRORS - currErrors);
                    usedLetters.add(letter);
                    break;
            }

            // Si llegamos al límite de errores, perdemos
            if (currErrors == MAX_ERRORS) {
                System.out.printf("\n> ¡Haz perdido! la palabra era: %s <\n", word);
                break;
            }

            // Si la palabra (sin los espacios añadidos) coincide con la adivinada, ganamos
            if (drawWord().replace(" ", "").equals(word)) {
                System.out.printf("\n> ¡Haz ganado! la palabra es: %s <\n", word);
                break;
            }
        }
    }

    /**
     * Reinicia el estado del juego para una nueva partida.
     */
    private void reset() {
        usedLetters.clear();
        currErrors = 0;
        word = wordsArray[r.nextInt(wordsArray.length)];
    }

    /**
     * Inicia y gestiona el flujo principal del juego, permitiendo jugar múltiples
     * partidas a disposición del usuario.
     */
    public void start() {
        System.out.println("--------------------------------------");
        System.out.println("> ¡Bienvenido al juego del ahorcado! <");
        System.out.println("--------------------------------------");
        System.out.printf("> Tienes %d intentos para acertar la palabra\n", MAX_ERRORS);

        boolean repeat = true;

        // Repite el juego mientras lo indique la bandera
        while (repeat) {
            play();

            // Exige al jugador una respuesta entre si o no (s/n)
            System.out.printf("\n¿Quieres jugar de nuevo? (s/n)\n> ");
            char choice = ' ';

            while (choice != 's' && choice != 'n') {
                choice = s.next().toLowerCase().charAt(0);

                switch (choice) {
                    case 's':
                        System.out.println("\n> ¡Genial! juguemos de nuevo <");
                        reset();
                        break;
                    case 'n':
                        System.out.println("\n> ¡Gracias por jugar! <");
                        repeat = false;
                        break;
                    default:
                        System.out.println("Escribe una opción válida\n");
                        break;
                }
            }
        }

        s.close();
    }

    public static void main(String[] args) {
        Ahorcado game = new Ahorcado();
        game.start();
    }
}
