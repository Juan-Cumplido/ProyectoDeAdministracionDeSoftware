package sdgcpp.logicaDeNegocio.clases;

import java.util.regex.Pattern;

public class ProyectoCampo {
    private int idProyectoCampo;
    private String nombre;
    private String fechaInicio;
    private String fechaFin;
    private String lugar;
    private String impactoObtenido;
    private int idPeriodo;

    private final static String EXPRESION_REGULAR = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$";
      
    public ProyectoCampo() {
    }
    public int getIdProyectoCampo() {
        return idProyectoCampo;
    }

    public void setIdProyectoCampo(int idProyectoCampo) {
        this.idProyectoCampo = idProyectoCampo;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarCadena(nombre);
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
        this.lugar = validarCadena(lugar);
    }
    
    public String getImpactoObtenido() {
        return impactoObtenido;
    }

    public void setImpactoObtenido(String impactoObtenido) {
        this.impactoObtenido = validarCadena(impactoObtenido);
    }
    
    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }
    
    private String validarCadena(String cadena) {
        if (cadena != null && Pattern.matches(EXPRESION_REGULAR, cadena.trim())) {
            return cadena.trim().replaceAll("\\s+", " ");
        } else {
            throw new IllegalArgumentException("La cadena no cumple con las reglas de validación.");
        }
    }
}
