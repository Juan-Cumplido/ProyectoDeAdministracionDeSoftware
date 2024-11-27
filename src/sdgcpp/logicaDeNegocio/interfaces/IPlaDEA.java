package sdgcpp.logicaDeNegocio.interfaces;
import java.sql.SQLException;
import java.util.List;
import sdgcpp.logicaDeNegocio.clases.PlaDEA;

public interface IPlaDEA {
        int registrarPlaDEA(PlaDEA plaDEA, List<Integer> listaIdProfesores)throws SQLException;
        public List<PlaDEA> obtenerPlaDEAsPorProfesorYPeriodo(int idProfesor, int idPeriodo) throws SQLException;
        
}
