package sdgcpp.logicaDeNegocio.clases;

import java.util.regex.Pattern;


public class PlaDEA {
    
    private int idPlaDEA;
    private String ejeEstrategico;
    private String programaEstrategico;
    private String objetivos;
    private String acciones;
    private String metas;
    private int idPeriodo;

    private final static String EXPRESION_REGULAR = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,500}$";
    public PlaDEA(String ejeEstrategico, String programaEstrategico, String objetivos, 
              String acciones, String metas) {
        this.ejeEstrategico = ejeEstrategico;
        this.programaEstrategico = programaEstrategico;
        this.objetivos = objetivos;
        this.acciones = acciones;
        this.metas = metas;
    }
    public PlaDEA() {
    }
    
    public int getIdPlaDEA() {
        return idPlaDEA;
    }

    public void setIdPlaDEA(int idPlaDEA) {
        this.idPlaDEA = idPlaDEA;
    }
    
    public String getEjeEstrategico() {
        return ejeEstrategico;
    }

    public void setEjeEstrategico(String ejeEstrategico) {
        this.ejeEstrategico = validarCadena(ejeEstrategico);
    }
    
    public String getProgramaEstrategico() {
        return programaEstrategico;
    }

    public void setProgramaEstrategico(String programaEstrategico) {
        this.programaEstrategico = validarCadena(programaEstrategico);
    }
    
    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = validarCadena(objetivos);
    }
    
    public String getAcciones() {
        return acciones;
    }

    public void setAcciones(String acciones) {
        this.acciones = validarCadena(acciones);
    }
    
    public String getMetas() {
        return metas;
    }

    public void setMetas(String metas) {
        this.metas = validarCadena(metas);
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
