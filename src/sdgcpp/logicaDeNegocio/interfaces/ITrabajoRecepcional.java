package sdgcpp.logicaDeNegocio.interfaces;
import java.sql.SQLException;
import java.util.List;
import sdgcpp.logicaDeNegocio.clases.TablaTR;
import sdgcpp.logicaDeNegocio.clases.TrabajoRecepcional;


public interface ITrabajoRecepcional {
    int registrarTrabajoRecepcional(TrabajoRecepcional trabajoRecepcional, List<List<String>> profesores , List<Integer> listaIdEstudiantes)throws SQLException; 
       public List<TablaTR> obtenerTrabajosRecepcionales(int idProfesor, int idPeriodo) throws SQLException;
 
}
