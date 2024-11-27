package sdgcpp.logicaDeNegocio.clases;

import java.util.regex.Pattern;


public class Profesor{
    
    private int idProfesor;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private int idAcceso;
    private int idCategoriaContratacionUV;
    private int idTipoContratacionUV;
    private String rol;

    

    private final static String EXPRESION_REGULAR = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s'\\-]{1,60}$";
    private final static String EXPRESION_REGULAR_APELLIDO_MATERNO = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s'\\-]{0,60}$";
    private final static String EXPRESION_REGULAR_CORREO_ELECTRONICO = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
     public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }
    
    public Profesor() {
    }
    
    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre!=null&&Pattern.matches(EXPRESION_REGULAR, nombre.trim())) {
            this.nombre = nombre.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
        
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        if (apellidoPaterno!=null&&Pattern.matches(EXPRESION_REGULAR, apellidoPaterno.trim())) {
            this.apellidoPaterno = apellidoPaterno.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        }
        
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        if (apellidoMaterno == null || Pattern.matches(EXPRESION_REGULAR_APELLIDO_MATERNO, apellidoMaterno.trim())) {
            this.apellidoMaterno = apellidoMaterno.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        
        if (correo!=null&&Pattern.matches(EXPRESION_REGULAR_CORREO_ELECTRONICO, correo.trim())) {
            this.correo = correo.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        }
    }
    
    public int getIdAcceso() {
        return idAcceso;
    }

    public void setIdAcceso(int idAcceso) {
        this.idAcceso = idAcceso;
    }
    
    public int getIdCategoriaContratacionUV() {
        return idCategoriaContratacionUV;
    }

    public void setIdCategoriaContratacionUV(int idCategoriaContratacionUV) {
        this.idCategoriaContratacionUV = idCategoriaContratacionUV;
    }

    public int getIdTipoContratacionUV() {
        return idTipoContratacionUV;
    }

    public void setIdTipoContratacionUV(int idTipoContratacionUV) {
        this.idTipoContratacionUV = idTipoContratacionUV;
    }
    
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    @Override
    public String toString() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno + " " + correo;
    }
    
    @Override
    public boolean equals(Object object) {
        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        } 
        final Profesor otroProfesor = (Profesor) object;
        return (this.nombre == null ? otroProfesor.nombre == null : this.nombre.equals(otroProfesor.nombre))
            && (this.apellidoPaterno == null ? otroProfesor.apellidoPaterno == null : this.apellidoPaterno.equals(otroProfesor.apellidoPaterno))   
            && (this.apellidoMaterno == null ? otroProfesor.apellidoMaterno == null : this.apellidoMaterno.equals(otroProfesor.apellidoMaterno))  
            && (this.correo == null ? otroProfesor.correo == null : this.correo.equals(otroProfesor.correo))
           && this.idCategoriaContratacionUV == otroProfesor.idCategoriaContratacionUV
            && this.idTipoContratacionUV == otroProfesor.idTipoContratacionUV;
    }

}
