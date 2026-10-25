package prog2.residenciageriatricaspring.repositorios;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prog2.residenciageriatricaspring.modelos.Reserva;

/**
 * @author Emanuel
 */

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, String> {
    
    // la reserva de un residente (vacío si no tiene habitación)
    Optional<Reserva> findByIdResidente(int idResidente);

    boolean existsByIdResidente(int idResidente);
    
}
