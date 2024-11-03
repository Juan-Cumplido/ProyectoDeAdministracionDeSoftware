package sdgcpp.logicaDeNegocio.clases;

import java.util.regex.Pattern;

public class ProfesorUV extends Profesor{
    private String noPersonal;
    private String disciplina;
    private int idRegion;
    private int idCategoriaContratacionUV;
    private int idTipoContratacionUV;
    private int idAreaAcademica;
    private int idProfesorUV;

    private final static String EXPRESION_REGULAR_NO_PERSONAL = "^[1-9]\\d{0,19}$";
    private final static String EXPRESION_REGULAR_DISCIPLINA = "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,200}$";
    
    public ProfesorUV() {
        super();
    }

    public String getNoPersonal() {
        return noPersonal;
    }

    public void setNoPersonal(String noPersonal) {
        if (noPersonal!=null&&Pattern.matches(EXPRESION_REGULAR_NO_PERSONAL, noPersonal.trim())) {
            this.noPersonal = noPersonal.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        if (disciplina!=null&&Pattern.matches(EXPRESION_REGULAR_DISCIPLINA, disciplina.trim())) {
            this.disciplina = disciplina.trim().replaceAll("\\s+", " ");
        }else{
            throw new IllegalArgumentException();
        } 
    }
    
    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }

    public int getIdCategoriaContratacionUV() {
        return idCategoriaContratacionUV;
    }

    public void setIdCategoriaContratacionUV(int idCategoriaContratacionUV) {
        this.idCategoriaContratacionUV = idCategoriaContratacionUV;
    }

    public int getIdTipoContratacionUV() {
        return idTipoContratacionUV;
    }

    public void setIdTipoContratacionUV(int idTipoContratacionUV) {
        this.idTipoContratacionUV = idTipoContratacionUV;
    }

    public int getIdAreaAcademica() {
        return idAreaAcademica;
    }

    public void setIdAreaAcademica(int idAreaAcademica) {
        this.idAreaAcademica = idAreaAcademica;
    }
    
    public int getIdProfesorUV() {
        return idProfesorUV;
    }

    public void setIdProfesoerUV(int idProfesorUV) {
        this.idProfesorUV = idProfesorUV;
    }
    
    
        @Override
    public boolean equals(Object object) {
        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        } 
        final ProfesorUV otroProfesorUV = (ProfesorUV) object;
        return (this.noPersonal == null ? otroProfesorUV.noPersonal == null : this.noPersonal.equals(otroProfesorUV.noPersonal))
            && (this.disciplina == null ? otroProfesorUV.disciplina == null : this.disciplina.equals(otroProfesorUV.disciplina))   
            && this.idRegion == otroProfesorUV.idRegion
            && this.idCategoriaContratacionUV == otroProfesorUV.idCategoriaContratacionUV
            && this.idTipoContratacionUV == otroProfesorUV.idTipoContratacionUV
            && this.idAreaAcademica == otroProfesorUV.idAreaAcademica;
    }

}
