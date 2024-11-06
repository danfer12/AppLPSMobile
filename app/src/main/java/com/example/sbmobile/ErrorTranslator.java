package com.example.sbmobile;

import java.util.HashMap;
import java.util.Map;


public class ErrorTranslator {
    private static final Map<String, String> errorMessages;

    static {
        errorMessages = new HashMap<>();
        errorMessages.put("The email address is badly formatted.", "El formato de la dirección de correo es incorrecto.");
        errorMessages.put("The password is invalid or the user does not have a password.", "La contraseña es inválida o el usuario no tiene una contraseña.");
        errorMessages.put("There is no user record corresponding to this identifier. The user may have been deleted.", "No existe un usuario con este correo electrónico.");
        errorMessages.put("The supplied credentials are invalid.", "Las credenciales proporcionadas son inválidas.");
        errorMessages.put("The email address is already in use by another account.", "La dirección de correo ya está en uso por otra cuenta.");
        errorMessages.put("This operation is not allowed. You must enable this service in the console.", "Esta operación no está permitida. Debes habilitar este servicio en la consola.");
        errorMessages.put("The given password is invalid.", "La contraseña proporcionada es inválida.");
        errorMessages.put("The user account has been disabled by an administrator.", "La cuenta de usuario ha sido deshabilitada por un administrador.");
        errorMessages.put("We have blocked all requests from this device due to unusual activity. Try again later.", "Hemos bloqueado todas las solicitudes de este dispositivo debido a actividad inusual. Inténtalo de nuevo más tarde.");
        errorMessages.put("The SMS verification code used to create the phone auth credential is invalid.", "El código de verificación de SMS utilizado para crear la credencial de autenticación telefónica es inválido.");
        errorMessages.put("The verification ID used to create the phone auth credential is invalid.", "El ID de verificación utilizado para crear la credencial de autenticación telefónica es inválido.");
        // Añadir más traducciones según sea necesario
    }

    public static String translate(String errorMessage) {
        String translatedMessage = errorMessages.get(errorMessage);
        return translatedMessage != null ? translatedMessage : errorMessage;
    }
}