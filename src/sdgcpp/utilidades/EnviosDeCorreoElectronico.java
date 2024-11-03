package sdgcpp.utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import org.apache.log4j.Logger;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EnviosDeCorreoElectronico {
    private static final Logger LOG = Logger.getLogger(EnviosDeCorreoElectronico.class);

    private static Properties leerCredenciales() {
        Properties propiedadesCorreo = new Properties();
        try{
           DataInputStream archivoCorreo = new DataInputStream(new FileInputStream("src\\sdgcoilvic\\utilidades\\credenciales.txt"));
           propiedadesCorreo.load(archivoCorreo);
        }catch(FileNotFoundException archivoNoEncontrado){
            LOG.fatal(archivoNoEncontrado.getMessage());
        }catch(IOException excepcion){
            LOG.fatal(excepcion.getCause());
        }
        return propiedadesCorreo;
    }

    public static boolean enviarCorreo(String destinatario, String asunto, String mensaje) {
        boolean enviadoExitosamente = false;

        Properties propiedades = new Properties();
        Properties datosCorreo = leerCredenciales();
        String remitente = datosCorreo.getProperty("Remitente");
        String contraseniaRemitente = datosCorreo.getProperty("ContraseniaRemitente");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.starttls.enable", "true");

        Authenticator autenticador = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, contraseniaRemitente);
            }
        };

        Session sesion = Session.getInstance(propiedades, autenticador);

        try {
            Message mensajeCorreo = new MimeMessage(sesion);
            mensajeCorreo.setFrom(new InternetAddress(remitente));
            mensajeCorreo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensajeCorreo.setSubject(asunto);
            mensajeCorreo.setText(mensaje);
             Transport transport = sesion.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, contraseniaRemitente);
            transport.sendMessage(mensajeCorreo, mensajeCorreo.getAllRecipients());
            transport.close();

            enviadoExitosamente = true;
        }catch(SendFailedException excepcion){
            LOG.error(excepcion.getMessage());
            enviadoExitosamente = false;
        } catch (MessagingException e) {
            enviadoExitosamente = false;
            LOG.info("Error al enviar el correo: " + e);
        }

        return enviadoExitosamente;
    }

    public static boolean verificarEnvioCorreo(String destinatario, String asunto, String mensaje) {
        boolean correoEnviado = enviarCorreo(destinatario, asunto, mensaje);
        boolean enviadoExitosamente = false;
        if (!correoEnviado) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "El correo no se pudo enviar. ¿Desea intentarlo de nuevo?",
                    ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("Error al enviar correo, verifique su conexion a internet");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                enviadoExitosamente = verificarEnvioCorreo(destinatario, asunto, mensaje);
            } else {
                enviadoExitosamente = false;
            }
        } else {
            enviadoExitosamente = true;
        }
        return enviadoExitosamente;
    }
}

