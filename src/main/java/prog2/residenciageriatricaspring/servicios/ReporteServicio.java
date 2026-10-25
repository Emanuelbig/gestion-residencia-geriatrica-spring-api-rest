package prog2.residenciageriatricaspring.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import prog2.residenciageriatricaspring.dto.PersonaDTO;
import prog2.residenciageriatricaspring.dto.PersonaHabitacionDTO;
import prog2.residenciageriatricaspring.excepciones.EntidadNoEncontradaException;
import prog2.residenciageriatricaspring.modelos.Asignacion;
import prog2.residenciageriatricaspring.modelos.Enfermero;
import prog2.residenciageriatricaspring.modelos.Reserva;
import prog2.residenciageriatricaspring.modelos.Residente;
import prog2.residenciageriatricaspring.repositorios.AsignacionRepository;
import prog2.residenciageriatricaspring.repositorios.EnfermeroRepository;
import prog2.residenciageriatricaspring.repositorios.ReservaRepository;
import prog2.residenciageriatricaspring.repositorios.ResidenteRepository;

/**
 * Consultas que cruzan datos de varias entidades.
 * @author Emanuel
 */
@Service
public class ReporteServicio {

    private final ResidenteRepository residenteRepository;
    private final EnfermeroRepository enfermeroRepository;
    private final ReservaRepository reservaRepository;
    private final AsignacionRepository asignacionRepository;

    public ReporteServicio(ResidenteRepository residenteRepository,
                           EnfermeroRepository enfermeroRepository,
                           ReservaRepository reservaRepository,
                           AsignacionRepository asignacionRepository) {
        this.residenteRepository = residenteRepository;
        this.enfermeroRepository = enfermeroRepository;
        this.reservaRepository = reservaRepository;
        this.asignacionRepository = asignacionRepository;
    }

    /** 1. El residente con la habitación que ocupa (sin código si no tiene). */
    public PersonaHabitacionDTO habitacionDeResidente(int idResidente) {
        Residente residente = buscarResidente(idResidente);
        Optional<Reserva> reserva = reservaRepository.findByIdResidente(idResidente);
        String codigo = null;                                   // queda null si no tiene habitación
        if (reserva.isPresent()) {
            codigo = reserva.get().getCodigoHabitacion();
        }
        return new PersonaHabitacionDTO(PersonaDTO.desde(residente), codigo);
    }

    /** 2. ¿Qué residentes atiende el enfermero? */
    public List<PersonaDTO> residentesDeEnfermero(int idEnfermero) {
        if (!enfermeroRepository.existsById(idEnfermero)) {
            throw new EntidadNoEncontradaException("No existe el enfermero " + idEnfermero + ".");
        }
        List<PersonaDTO> resultado = new ArrayList<>();
        for (Asignacion a : asignacionRepository.findByIdEnfermero(idEnfermero)) {
            Optional<Reserva> reserva = reservaRepository.findById(a.getCodigoHabitacion());
            if (reserva.isPresent()) {
                Optional<Residente> r = residenteRepository.findById(reserva.get().getIdResidente());
                if (r.isPresent()) {
                    resultado.add(PersonaDTO.desde(r.get()));
                }
            }
        }
        return resultado;
    }

    /** 3. ¿Qué enfermero atiende al residente? Vacío si no tiene. */
    public Optional<PersonaDTO> enfermeroDeResidente(int idResidente) {
        buscarResidente(idResidente);                           // si no existe, lanza la excepción
        Optional<Reserva> reserva = reservaRepository.findByIdResidente(idResidente);
        if (reserva.isEmpty()) {
            return Optional.empty();
        }
        Optional<Asignacion> asignacion = asignacionRepository.findById(reserva.get().getCodigoHabitacion());
        if (asignacion.isEmpty()) {
            return Optional.empty();
        }
        Optional<Enfermero> enfermero = enfermeroRepository.findById(asignacion.get().getIdEnfermero());
        if (enfermero.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(PersonaDTO.desde(enfermero.get()));
    }

    private Residente buscarResidente(int idResidente) {
        Optional<Residente> residente = residenteRepository.findById(idResidente);
        if (residente.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el residente " + idResidente + ".");
        }
        return residente.get();
    }
}
