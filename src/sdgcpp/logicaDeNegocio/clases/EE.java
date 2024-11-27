package sdgcpp.logicaDeNegocio.clases;


public class EE {
     private int idEE;
    private String programaEducativo;
    private String experienciaEducativa;
    private String bloque;
    private String sesion;    
    private int creditos;
    private int horas;
    private String semanas;    
    private String mes;
    private int idPeriodo;
    private int idProfesor;
    
    public EE(String programaEducativo, String experienciaEducativa, String bloque, 
              String sesion, int creditos, int horas, String semanas, String mes) {
        this.programaEducativo = programaEducativo;
        this.experienciaEducativa = experienciaEducativa;
        this.bloque = bloque;
        this.sesion = sesion;
        this.creditos = creditos;
        this.horas = horas;
        this.semanas = semanas;
        this.mes = mes;
    }

    public EE() {
    }
    
    public int getEE() {
        return idEE;
    }

    public void setIdEE(int idEE) {
        this.idEE = idEE;
    }
    
    public String getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(String programaEducativo) {
        this.programaEducativo = programaEducativo;
    }
    
    public String getExperienciaEducativa() {
        return experienciaEducativa;
    }

    public void setExperienciaEducativa(String experienciaEducativa) {
        this.experienciaEducativa = experienciaEducativa;
    }
    
    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }
    
    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }
    
    public String getSemanas() {
        return semanas;
    }

    public void setSemanas(String semanas) {
        this.semanas =semanas;
    }
    
     public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
    
    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }
    
    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }
    
    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }
    
    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }
    
}
