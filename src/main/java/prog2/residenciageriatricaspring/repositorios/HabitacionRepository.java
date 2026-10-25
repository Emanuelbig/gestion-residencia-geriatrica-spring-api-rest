package prog2.residenciageriatricaspring.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prog2.residenciageriatricaspring.modelos.Habitacion;

/**
 * @author Emanuel Morano Sole
 */

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, String> {
}
