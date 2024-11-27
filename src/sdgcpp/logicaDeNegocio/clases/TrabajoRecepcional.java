package sdgcpp.logicaDeNegocio.clases;

import java.util.regex.Pattern;


public class TrabajoRecepcional {
    private int idTrabajoRecepcional;
    private String titulo;
    private String fecha;
    private String licenciatura;
    private String modalidad;
    private int resultadoObtenidoDefensa;
    private int idPeriodo;

    private final static String EXPRESION_REGULAR = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$";
      
    public TrabajoRecepcional() {
    }
    public int getIdTrabajoRecepcional() {
        return idTrabajoRecepcional;
    }

    public void setIdTrabajoRecepcional(int idTrabajoRecepcional) {
        this.idTrabajoRecepcional = idTrabajoRecepcional;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = validarCadena(titulo);
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
    
    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = validarCadena(modalidad);
    }

    public int getResultadoObtenidoDefensa() {
        return resultadoObtenidoDefensa;
    }

    public void setResultadoObtenidoDefensa(int resultadoObtenidoDefensa) {
        this.resultadoObtenidoDefensa = resultadoObtenidoDefensa;
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
