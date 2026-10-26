package prog2.residenciageriatricaspring.repositorios;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prog2.residenciageriatricaspring.modelos.Habitacion;

/**
 * Habitaciones, y de paso las reservas y asignaciones, que se guardan adentro de cada habitación.
 * Spring arma el SQL leyendo el nombre del método: findByResidenteId → WHERE id_residente = ?
 * @author Emanuel Morano Sole
 */

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, String> {

    // reservas: la habitación de un residente (vacío si no tiene)
    Optional<Habitacion> findByResidenteId(int idResidente);

    boolean existsByResidenteId(int idResidente);

    List<Habitacion> findByResidenteIsNotNull();    // ocupadas

    List<Habitacion> findByResidenteIsNull();       // libres

    // asignaciones: las habitaciones que atiende un enfermero
    List<Habitacion> findByEnfermeroId(int idEnfermero);

    boolean existsByEnfermeroId(int idEnfermero);

    List<Habitacion> findByEnfermeroIsNotNull();    // con enfermero

    List<Habitacion> findByEnfermeroIsNull();       // sin enfermero
}
