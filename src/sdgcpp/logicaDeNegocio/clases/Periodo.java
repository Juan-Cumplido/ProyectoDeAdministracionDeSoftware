package sdgcpp.logicaDeNegocio.clases;
import java.sql.Date;


public class Periodo {
    private int idPeriodo;
    private Date fechaInicio;
    private Date fechaFin;
    private String nombrePeriodo;


    public Periodo(int idPeriodo, Date fechaInicio, Date fechaFin, String nombrePeriodo) {
        this.idPeriodo = idPeriodo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombrePeriodo = nombrePeriodo;
    }

    public Periodo() {

    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNombrePeriodo() {
        return nombrePeriodo;
    }

    public void setNombrePeriodo(String nombrePeriodo) {
        this.nombrePeriodo = nombrePeriodo;
    }

}
