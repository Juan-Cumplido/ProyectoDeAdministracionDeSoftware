package sdgcpp.utilidades;

public class ColaboracionEnCursoSinglenton {
    private static ColaboracionEnCursoSinglenton instance;
    private int idColaboracionEnCurso;

    private ColaboracionEnCursoSinglenton() {
    }

    public static ColaboracionEnCursoSinglenton getInstance() {
        if (instance == null) {
            instance = new ColaboracionEnCursoSinglenton();
        }
        return instance;
    }

    public int getIdColaboracionEnCurso() {
        return idColaboracionEnCurso;
    }

    public void setIdColaboracionEnCurso(int idColaboracionEnCurso) {
        this.idColaboracionEnCurso = idColaboracionEnCurso;
    }

    public void destruirColaboracionEnCursoSinglenton() {
        this.idColaboracionEnCurso = -1;
    }
}
