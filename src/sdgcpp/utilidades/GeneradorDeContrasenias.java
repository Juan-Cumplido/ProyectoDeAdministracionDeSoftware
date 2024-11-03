package sdgcpp.utilidades;

import java.security.SecureRandom;

public class GeneradorDeContrasenias {
    private static final String EXPRESION_REGULAR_CONTRASENIA = 
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{15,}$";
    private static final String CARACTERES_PERMITIDOS = 
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@$!%*?&";
    private static final SecureRandom aleatorio = new SecureRandom();

    public static String generarContraseña() {
        StringBuilder contraseñaGenerada;
        do {
            contraseñaGenerada = new StringBuilder();
            boolean tieneMinúscula = false;
            boolean tieneMayúscula = false;
            boolean tieneDígito = false;
            boolean tieneEspecial = false;

            for (int i = 0; i < 15; i++) {
                char caracter = CARACTERES_PERMITIDOS.charAt(aleatorio.nextInt(CARACTERES_PERMITIDOS.length()));
                contraseñaGenerada.append(caracter);
                if (Character.isLowerCase(caracter)) tieneMinúscula = true;
                if (Character.isUpperCase(caracter)) tieneMayúscula = true;
                if (Character.isDigit(caracter)) tieneDígito = true;
                if ("@$!%*?&".indexOf(caracter) >= 0) tieneEspecial = true;
            }

            if (!(tieneMinúscula && tieneMayúscula && tieneDígito && tieneEspecial)) {
                continue;
            }

            int length = 15 + aleatorio.nextInt(5);
            while (contraseñaGenerada.length() < length) {
                char caracter = CARACTERES_PERMITIDOS.charAt(aleatorio.nextInt(CARACTERES_PERMITIDOS.length()));
                contraseñaGenerada.append(caracter);
            }
        } while (!contraseñaGenerada.toString().matches(EXPRESION_REGULAR_CONTRASENIA));

        return contraseñaGenerada.toString();
    }

}
