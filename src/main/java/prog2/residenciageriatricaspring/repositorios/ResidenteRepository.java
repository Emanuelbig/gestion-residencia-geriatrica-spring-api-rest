package prog2.residenciageriatricaspring.repositorios;

import org.springframework.stereotype.Repository;
import prog2.residenciageriatricaspring.modelos.Residente;

/**
 * @author Emanuel Morano Sole
 */

@Repository
public interface ResidenteRepository extends PersonaRepository<Residente> {
}
