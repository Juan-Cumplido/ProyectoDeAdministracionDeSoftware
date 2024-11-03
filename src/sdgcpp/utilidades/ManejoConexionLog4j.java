package sdgcpp.utilidades;

import com.sun.mail.util.MailConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.AddressException;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.OnlyOnceErrorHandler;
import org.apache.log4j.spi.LoggingEvent;

public class ManejoConexionLog4j extends OnlyOnceErrorHandler{
    
    private static final Logger LOG=Logger.getLogger(ManejoConexionLog4j.class);
    
    @Override
    public void error(String message, Exception excepcion, int errorCode, LoggingEvent event) {
        if (excepcion != null) {
            if (excepcion instanceof MailConnectException) {
                LOG.fatal(excepcion.getMessage());
            } else if (excepcion instanceof UnknownHostException) {
                LOG.fatal(excepcion.getMessage());
            } else if (excepcion instanceof AuthenticationFailedException) {
                LOG.fatal(excepcion.getMessage());
            } else if (excepcion instanceof MessagingException) {
                LOG.fatal(excepcion.getMessage());
            } else if (excepcion instanceof SendFailedException) {
                LOG.fatal(excepcion.getMessage());
            } else if (excepcion instanceof AddressException) {
                LOG.fatal(excepcion.getMessage());
            } else if (excepcion instanceof SocketTimeoutException) {
                LOG.fatal(excepcion.getMessage());
            }else {
                LOG.fatal(excepcion.getMessage());
            }
        }
    }
  
}
