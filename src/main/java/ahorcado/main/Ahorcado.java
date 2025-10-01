package ahorcado.main;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author Hayran Andrés López González
 * @version 1.2
 */

public class Ahorcado {
    Random r = new Random();
    Scanner s = new Scanner(System.in);

    // Atributos estáticos
    private static final int MAX_ERRORS = 7; // Máximo de errores permitidos
    private static final String HANGMAN_STRING = "q(x_x)p"; // Dibujo ASCII del monigote

    // Arreglo de palabras predefinidas para elegir (también estático)
    private static final String[] wordsArray = { "herencia", "instancia", "interfaz", "compilar", "mensajes",
            "privadas", "paquetes", "ejecutar", "propiedad", "abstracto" };

    // Atributos de instancia
    private String word; // Palabra a adivinar
    private ArrayList<Character> usedLetters; // Lista de letras usadas
    private int currErrors;// Cantidad de errores actuales

    // Constructor
    public Ahorcado() {
        this.word = wordsArray[r.nextInt(wordsArray.length)];
        this.usedLetters = new ArrayList<Character>();
        this.currErrors = 0;
    }

    /**
     * Dibuja la figura del ahorcado en función del número de errores cometidos y la
     * cantidad máxima de errores permitidos
     */
    public String drawHangman(int errorCount) {
        // Calcula la proporción de la figura a dibujar basándose en los intentos
        int charsToDraw = (int) ((double) errorCount / Ahorcado.MAX_ERRORS * Ahorcado.HANGMAN_STRING.length());

        // Obtiene la parte de la figura que se debe mostrar
        String hangmanPart = Ahorcado.HANGMAN_STRING.substring(0, charsToDraw);

        return hangmanPart;
    }

    /**
     * Dibuja la palabra en función de la lista de letras usadas. La lista también
     * incluye letras que son elecciones incorrectas. El truco para que funcione
     * esta verificación, entonces, está en que esas letras no están en la palabra
     * (por eso son incorrectas) y por ende pasan derecho el test sin afectar el
     * resultado
     */
    public String drawWord() {
        String finalWord = "";

        // Recorre por cada letra de la palabra
        for (int i = 0; i < this.word.length(); i++) {
            char letter = this.word.charAt(i);

            // Si la letra está en la lista, la añade a la palabra final y se sale del bucle
            // Si no se encontró la letra, pone una barra baja
            if (this.usedLetters.contains(letter)) {
                finalWord += letter + " ";
            } else {
                finalWord += "_ ";
            }

            // Alternativa con operador ternario ?:
            // finalWord += this.usedLetters.contains(letter) ? letter + " " : "_ ";
        }

        return finalWord;
    }

    /**
     * Verifica que la letra elegida esté en la palabra y no haya sido usada antes.
     * Res => 1: letra correcta, 0: letra ya evaluada antes, -1: letra incorrecta
     */
    public int checkChar(char letter) {
        // Verifica que la letra fue usada ya y retorna 0 en ese caso
        if (this.usedLetters.contains(letter)) {
            return 0;
        }

        // Si no, busca en la palabra a por la letra y retorna 1 si la encuentra
        if (this.word.contains(String.valueOf(letter))) {
            return 1;
        }

        // Si no la encuentra... -1
        return -1;
    }

    /**
     * Imprime la lista de letras usadas en el orden en el que se usaron
     */
    public String printUsedLetters() {
        String finalString = "";

        for (int i = 0; i < this.usedLetters.size(); i++) {
            finalString += this.usedLetters.get(i);
        }

        return finalString;
    }

    /**
     * Juega una única partida de ahorcado
     */
    public void play() {
        // Repite hasta que no ganemos o perdamos
        while (true) {
            // Dibuja la palabra actual y el ahorcado
            System.out.printf("\n%s\t%s\t%s\n", drawWord(),
                    drawHangman(currErrors), printUsedLetters());
            System.out.println("Ingresa una letra");

            // Letra elegida
            char letter = s.next().toLowerCase().charAt(0);

            // Verifica la letra y reacciona dependiendo del caso
            int checkResult = checkChar(letter);
            switch (checkResult) {
                // Letra correcta: añade la letra como usada
                case 1:
                    System.out.println("Letra correcta");
                    this.usedLetters.add(letter);
                    break;
                // Letra ya evaluada antes
                case 0:
                    System.out.println("Letra ya evaluada antes");
                    break;
                // Letra incorrecta
                case -1:
                    currErrors++;
                    System.out.printf("Letra incorrecta. Te quedan %d intentos\n", MAX_ERRORS - currErrors);
                    this.usedLetters.add(letter);
                    break;
            }

            // Si llegamos al límite de errores, perdemos
            if (currErrors == MAX_ERRORS) {
                System.out.printf("\n> %s ¡Haz perdido! la palabra era: %s <\n", HANGMAN_STRING, this.word);
                break;
            }

            // Si la palabra (sin los espacios añadidos) coincide con la adivinada, ganamos
            if (drawWord().replace(" ", "").equals(this.word)) {
                System.out.printf("\n> ¡Haz ganado! la palabra es: %s <\n", this.word);
                break;
            }
        }
    }

    /**
     * Resetea los valores del juego a su estado original para empezar otra vez
     */
    public void reset() {
        this.usedLetters.clear();
        this.currErrors = 0;
        this.word = wordsArray[r.nextInt(wordsArray.length)];
    }

    /**
     * Método principal que gestiona el juego
     */
    public void start() {
        // Bandera que indica si se repite el juego
        boolean repeat = true;

        System.out.println("--------------------------------------");
        System.out.println("> ¡Bienvenido al juego del ahorcado! <");
        System.out.println("--------------------------------------");

        // Repite el juego mientras queramos repetir
        while (repeat) {
            play();

            // Le pedimos al jugador si quiere repetir
            System.out.println("\n¿Quieres jugar de nuevo? (s/n)");
            char choice = ' ';

            // Exige al jugador una respuesta entre si o no (s/n)
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
