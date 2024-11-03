package sdgcpp.logicaDeNegocio.clases;

import java.util.regex.Pattern;

public class Estudiante {
    private int idEstudiante;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String claveInstitucional;

    private final static String EXPRESION_REGULAR = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s'\\-]{1,60}$";
    private final static String EXPRESION_REGULAR_APELLIDO_MATERNO = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s'\\-]{0,60}$";
    private final static String EXPRESION_REGULAR_CORREO_ELECTRONICO = "^[a-zA-Z0-9'._%+-]{1,64}@[a-zA-Z.-]{2,255}\\.[a-zA-Z]{2,}$";
    
    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
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

    public String getClaveInstitucional() {
        return claveInstitucional;
    }

    public void setClaveInstitucional(String claveInstitucional) {
        this.claveInstitucional = claveInstitucional;
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
        final Estudiante otroEstudiante = (Estudiante) object;
        return (this.nombre == null ? otroEstudiante.nombre == null : this.nombre.equals(otroEstudiante.nombre))
            && (this.apellidoPaterno == null ? otroEstudiante.apellidoPaterno == null : this.apellidoPaterno.equals(otroEstudiante.apellidoPaterno))   
            && (this.apellidoMaterno == null ? otroEstudiante.apellidoMaterno == null : this.apellidoMaterno.equals(otroEstudiante.apellidoMaterno))  
            && (this.correo == null ? otroEstudiante.correo == null : this.correo.equals(otroEstudiante.correo))
            && (this.claveInstitucional == null ? otroEstudiante.claveInstitucional == null : this.claveInstitucional.equals(otroEstudiante.claveInstitucional));
            
    }
}
