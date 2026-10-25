package prog2.residenciageriatricaspring.repositorios;

import org.springframework.stereotype.Repository;
import prog2.residenciageriatricaspring.modelos.Enfermero;

/**
 * @author Emanuel Morano Sole
 */

@Repository
public interface EnfermeroRepository extends PersonaRepository<Enfermero> {
}
