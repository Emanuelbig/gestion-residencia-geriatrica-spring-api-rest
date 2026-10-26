package prog2.residenciageriatricaspring.servicios;

import org.springframework.stereotype.Service;
import prog2.residenciageriatricaspring.modelos.Enfermero;
import prog2.residenciageriatricaspring.repositorios.EnfermeroRepository;
import prog2.residenciageriatricaspring.repositorios.HabitacionRepository;
import prog2.residenciageriatricaspring.repositorios.ResidenteRepository;

/**
 * Reglas propias de los enfermeros. Lo común a toda persona está en PersonaServicio.
 * @author Emanuel
 */
@Service
public class EnfermeroServicio extends PersonaServicio<Enfermero> {

    private final HabitacionRepository habitacionRepository;   // para no borrar un enfermero con habitaciones

    public EnfermeroServicio(EnfermeroRepository enfermeroRepository,
                             ResidenteRepository residenteRepository,
                             HabitacionRepository habitacionRepository) {
        super(enfermeroRepository, residenteRepository);
        this.habitacionRepository = habitacionRepository;
    }

    @Override
    protected Enfermero crearPersona(Integer id, String dni, String nombre, String apellido) {
        return new Enfermero(id, dni, nombre, apellido);
    }

    @Override
    protected boolean estaEnUso(int id) {
        return habitacionRepository.existsByEnfermeroId(id);
    }

    @Override
    protected String mensajeEnUso(int id) {
        return "El enfermero " + id + " atiende habitaciones. Quitale las asignaciones primero.";
    }

    @Override
    protected String nombreEntidad() {
        return "enfermero";
    }
}
