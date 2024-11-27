package sdgcpp.logicaDeNegocio.clases;
 
public class TablaPC {
    private String nombre;
    private String fechaInicio;
    private String fechaFin;
    private String lugar;
    private String impactoObtenido;
    private String alumnos;

    public TablaPC() {
    }

    public TablaPC(String nombre, String fechaInicio, String fechaFin, String lugar, 
                   String impactoObtenido, String alumnos) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.lugar = lugar;
        this.impactoObtenido = impactoObtenido;
        this.alumnos = alumnos;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getImpactoObtenido() {
        return impactoObtenido;
    }

    public void setImpactoObtenido(String impactoObtenido) {
        this.impactoObtenido = impactoObtenido;
    }

    public String getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(String alumnos) {
        this.alumnos = alumnos;
    }
}


