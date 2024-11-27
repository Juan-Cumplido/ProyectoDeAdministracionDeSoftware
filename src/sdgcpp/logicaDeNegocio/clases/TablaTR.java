package sdgcpp.logicaDeNegocio.clases;
 
public class TablaTR {
    private String rol;
    private String alumnos;
    private String titulo;
    private String modalidad;
    private String fecha;
    private String licenciatura;
    private String defensa;

    public TablaTR() {
    }

    public TablaTR(String rol, String alumnos, String titulo, String modalidad, 
                   String fecha, String licenciatura, String defensa) {
        this.rol = rol;
        this.alumnos = alumnos;
        this.titulo = titulo;
        this.modalidad = modalidad;
        this.fecha = fecha;
        this.licenciatura = licenciatura;
        this.defensa = defensa;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(String alumnos) {
        this.alumnos = alumnos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLicenciatura() {
        return licenciatura;
    }

    public void setLicenciatura(String licenciatura) {
        this.licenciatura = licenciatura;
    }

    public String getDefensa() {
        return defensa;
    }

    public void setDefensa(String defensa) {
        this.defensa = defensa;
    }
}

