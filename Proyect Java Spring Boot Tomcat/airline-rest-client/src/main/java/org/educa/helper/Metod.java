package org.educa.helper;

import java.util.Scanner;

public class Metod {

    /**
     * Valida una entrada de texto utilizando una expresion regular, con un limite de intentos.
     * Muestra un mensaje de solicitud al usuario y verifica si la entrada coincide con el patron proporcionado.
     * Si la entrada no coincide con el patrón, se solicita al usuario que ingrese nuevamente hasta alcanzar el limite de intentos.
     * Si se supera el limite de intentos, se lanza una excepción.
     *
     * @param message El mensaje de solicitud que se mostrara al usuario.
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     * @param pattern La expresion regular que se utilizara para validar la entrada.
     * @return La entrada validada, con espacios en blanco eliminados alrededor.
     * @throws RuntimeException Si se supera el limite maximo de intentos y la entrada no es valida.
     */
    public static String stringValidation(String message, Scanner scanner, String pattern) {
        int maxAttempts = 5;
        int attempts = 0;
        String input;
        boolean valid = false;

        do {
            System.out.print(message);
            input = scanner.nextLine();
            valid = input.matches(pattern);
            attempts++;
            if (!valid && attempts < maxAttempts) {
                System.out.println("Remaining attempts: " + (maxAttempts - attempts));
            }
        } while (!valid && attempts < maxAttempts);

        if (!valid) {
            throw new RuntimeException("Maximum number of attempts exceeded. No valid data was entered.");
        }

        return input.trim();
    }

    /**
     * Este metodo comprueba si el usuario confirma una accion.
     *
     * @param message El mensaje que se muestra al usuario.
     * @param scanner El objeto Scanner utilizado para leer la entrada del usuario.
     * @return true si el usuario confirma con "yes" o "y", false de lo contrario.
     */
    public static boolean confirmation(String message, Scanner scanner) {
        String response = stringValidation("(Y/N)",scanner,"[yYnN]").trim().toLowerCase();

        return response.equals("yes") || response.equals("y");
    }
}