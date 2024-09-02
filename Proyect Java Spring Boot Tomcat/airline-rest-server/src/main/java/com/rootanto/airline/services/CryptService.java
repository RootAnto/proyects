package com.rootanto.airline.services;


import com.rootanto.airline.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class CryptService implements PasswordEncoder {
    private final SecurityUtil securityUtil;

    /**
     * Constructor de la clase CryptService.
     *
     * @param securityUtil Instancia de SecurityUtil para manejar operaciones de seguridad.
     */
    @Autowired
    public CryptService(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    /**
     * Método para cifrar un mensaje.
     *
     * @param message Mensaje a cifrar.
     * @return El mensaje cifrado.
     * @throws IllegalBlockSizeException Si el tamaño de bloque de datos no es válido.
     * @throws NoSuchPaddingException Si no se encuentra el esquema de relleno especificado.
     * @throws BadPaddingException Si se produce un error de relleno.
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de cifrado especificado.
     * @throws InvalidKeyException Si se proporciona una clave de cifrado no válida.
     */
    public String crypt(String message) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return securityUtil.crypt(message);
    }

    /**
     * Método para descifrar un mensaje.
     *
     * @param message Mensaje a descifrar.
     * @return El mensaje descifrado.
     * @throws IllegalBlockSizeException Si el tamaño de bloque de datos no es válido.
     * @throws NoSuchPaddingException Si no se encuentra el esquema de relleno especificado.
     * @throws BadPaddingException Si se produce un error de relleno.
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de cifrado especificado.
     * @throws InvalidKeyException Si se proporciona una clave de cifrado no válida.
     */
    public String decrypt(String message) throws IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return securityUtil.decrypt(message);
    }

    /**
     * Método para generar el hash de un mensaje.
     *
     * @param message Mensaje para el cual se generará el hash.
     * @return El hash del mensaje.
     * @throws NoSuchAlgorithmException Si no se encuentra el algoritmo de hash especificado.
     */
    public String hash(String message) throws NoSuchAlgorithmException {
        return securityUtil.createHash(message);
    }

    /**
     * Método de la interfaz PasswordEncoder para codificar una contraseña.
     *
     * @param rawPassword La contraseña sin procesar a codificar.
     * @return La contraseña codificada.
     */
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return hash(rawPassword.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error al codificar la contrasena", e);
        }
    }

    /**
     * Método de la interfaz PasswordEncoder para comparar contraseñas.
     *
     * @param rawPassword      La contraseña sin procesar.
     * @param encodedPassword  La contraseña codificada para comparar.
     * @return                 true si las contraseñas coinciden, false de lo contrario.
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            return encodedPassword.equals(hash(rawPassword.toString()));
        } catch (Exception e) {
            throw new RuntimeException("Error al comparar contrasenas", e);
        }
    }
}

