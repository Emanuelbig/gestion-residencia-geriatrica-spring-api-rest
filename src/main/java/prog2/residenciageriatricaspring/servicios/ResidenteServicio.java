package prog2.residenciageriatricaspring.servicios;

import org.springframework.stereotype.Service;
import prog2.residenciageriatricaspring.modelos.Residente;
import prog2.residenciageriatricaspring.repositorios.EnfermeroRepository;
import prog2.residenciageriatricaspring.repositorios.ReservaRepository;
import prog2.residenciageriatricaspring.repositorios.ResidenteRepository;

/**
 * Reglas propias de los residentes. Lo común a toda persona está en PersonaServicio.
 * @author Emanuel
 */
@Service
public class ResidenteServicio extends PersonaServicio<Residente> {

    private final ReservaRepository reservaRepository;   // para no borrar un residente con habitación

    // Spring ve este constructor y le pasa los repositorios solo (inyección de dependencias)
    public ResidenteServicio(ResidenteRepository residenteRepository,
                             EnfermeroRepository enfermeroRepository,
                             ReservaRepository reservaRepository) {
        super(residenteRepository, enfermeroRepository);
        this.reservaRepository = reservaRepository;
    }

    @Override
    protected Residente crearPersona(Integer id, String dni, String nombre, String apellido) {
        return new Residente(id, dni, nombre, apellido);
    }

    @Override
    protected boolean estaEnUso(int id) {
        return reservaRepository.existsByIdResidente(id);
    }

    @Override
    protected String mensajeEnUso(int id) {
        return "El residente " + id + " tiene una habitación asignada. Liberala primero.";
    }

    @Override
    protected String nombreEntidad() {
        return "residente";
    }
}
