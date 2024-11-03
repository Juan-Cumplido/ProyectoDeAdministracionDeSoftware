package sdgcpp.logicaDeNegocio.clases;

public class TablaProfesor {
    
    int idProfesor;
    String nombre;
    String apellidoPaterno;
    String apellidoMaterno;
    String correo;
    String claveInstitucional;
    String estadoProfesor;
    
    public TablaProfesor(int idProfesor,  String nombre, String apellidoPaterno, String apellidoMaterno, String correo, String claveInstitucional, String estadoProfesor) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correo = correo;
        this.claveInstitucional = claveInstitucional;
        this.estadoProfesor = estadoProfesor;
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
        this.nombre = nombre;
    }
    
    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }
    
    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }
    
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getClaveInstitucional() {
        return claveInstitucional;
    }

    public void setClaveInstitucional(String claveInstitucional) {
        this.claveInstitucional = claveInstitucional;
    }
    
    public String getEstadoProfesor() {
        return estadoProfesor;
    }

    public void setEstadoProfesor(String estadoProfesor) {
        this.estadoProfesor = estadoProfesor;
    }

}
