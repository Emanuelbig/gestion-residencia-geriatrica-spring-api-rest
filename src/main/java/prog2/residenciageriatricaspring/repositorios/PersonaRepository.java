package prog2.residenciageriatricaspring.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import prog2.residenciageriatricaspring.modelos.Persona;

/**
 * Lo que se puede consultar de cualquier persona. Spring no crea un repositorio para esta
 * interfaz (@NoRepositoryBean): solo para sus hijas.
 * @param <T> tipo de persona (Residente, Enfermero)
 * @author Emanuel Morano Sole
 */
@NoRepositoryBean
public interface PersonaRepository<T extends Persona> extends JpaRepository<T, Integer> {

    // Spring arma el SQL leyendo el nombre: SELECT COUNT(*) > 0 ... WHERE dni = ?
    boolean existsByDni(String dni);
}
