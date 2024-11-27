package sdgcpp.logicaDeNegocio.clases;

import java.util.regex.Pattern;

public class Estudiante {
    private int idEstudiante;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int trabajoRecepcional;
    private String matricula;

    private final static String EXPRESION_REGULAR = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s'\\-]{1,60}$";
    private final static String EXPRESION_REGULAR_APELLIDO_MATERNO = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s'\\-]{0,60}$";
     public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }
    
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
    
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
        
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
       this.apellidoMaterno = apellidoMaterno;
       
    }

    public int getTrabajoRecepcional() {
        return trabajoRecepcional;
    }

    public void setTrabajoRecepcional(int trabajoRecepcional) {
       this.trabajoRecepcional = trabajoRecepcional;
        
    }
    
        @Override
    public String toString() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno + " " + trabajoRecepcional;
    }
    
    @Override
    public boolean equals(Object object) {
        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        } 
        final Estudiante otroEstudiante = (Estudiante) object;
        return (this.nombre == null ? otroEstudiante.nombre == null : this.nombre.equals(otroEstudiante.nombre))
            && (this.apellidoPaterno == null ? otroEstudiante.apellidoPaterno == null : this.apellidoPaterno.equals(otroEstudiante.apellidoPaterno))   
            && (this.apellidoMaterno == null ? otroEstudiante.apellidoMaterno == null : this.apellidoMaterno.equals(otroEstudiante.apellidoMaterno));
            
    }
}
