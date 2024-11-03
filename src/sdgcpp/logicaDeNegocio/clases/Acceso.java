package sdgcpp.logicaDeNegocio.clases;

import java.util.regex.Pattern;

public class Acceso {
    private int idAcceso;
    private String contrasenia;
    private String usuario;
    private String tipoUsuario;

    private final static String EXPRESION_REGULAR_USUARIO = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String EXPRESION_REGULAR_CONTRASENIA = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    
    public int getIdAcceso() {
        return idAcceso;
    }

    public void setIdAcceso(int idAcceso) {
        this.idAcceso = idAcceso;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        if(contrasenia!=null&&Pattern.matches(EXPRESION_REGULAR_CONTRASENIA, contrasenia)){
            this.contrasenia = contrasenia;
        }else{
            throw new IllegalArgumentException();
        }
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        if(Pattern.matches(EXPRESION_REGULAR_USUARIO, String.valueOf(usuario))){
            this.usuario = usuario;
        }else{
            throw new IllegalArgumentException();
        }
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
        @Override
    public boolean equals(Object obj){
        Acceso accesoTemporal = (Acceso)obj;
        return this.contrasenia.equals(accesoTemporal.getContrasenia())&&
                this.usuario.equals(accesoTemporal.getUsuario())&&
                this.tipoUsuario.equals(accesoTemporal.getTipoUsuario()); 
    }


}
