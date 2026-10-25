package prog2.residenciageriatricaspring.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prog2.residenciageriatricaspring.modelos.Asignacion;

/**
 * @author Emanuel
 */

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, String> {
    
    //son las habitaciones que atiende un enfermero
    List<Asignacion> findByIdEnfermero(int idEnfermero);
    
    boolean existsByIdEnfermero(int idEnfermero);
}
