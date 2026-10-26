package prog2.residenciageriatricaspring.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import prog2.residenciageriatricaspring.dto.PersonaDTO;
import prog2.residenciageriatricaspring.dto.PersonaHabitacionDTO;
import prog2.residenciageriatricaspring.excepciones.EntidadNoEncontradaException;
import prog2.residenciageriatricaspring.modelos.Habitacion;
import prog2.residenciageriatricaspring.modelos.Residente;
import prog2.residenciageriatricaspring.repositorios.EnfermeroRepository;
import prog2.residenciageriatricaspring.repositorios.HabitacionRepository;
import prog2.residenciageriatricaspring.repositorios.ResidenteRepository;

/**
 * Consultas que cruzan datos de varias entidades. Todas pasan por la habitación,
 * que es la que sabe quién la ocupa y quién la atiende.
 * @author Emanuel
 */
@Service
public class ReporteServicio {

    private final ResidenteRepository residenteRepository;
    private final EnfermeroRepository enfermeroRepository;
    private final HabitacionRepository habitacionRepository;

    public ReporteServicio(ResidenteRepository residenteRepository,
                           EnfermeroRepository enfermeroRepository,
                           HabitacionRepository habitacionRepository) {
        this.residenteRepository = residenteRepository;
        this.enfermeroRepository = enfermeroRepository;
        this.habitacionRepository = habitacionRepository;
    }

    /** 1. El residente con la habitación que ocupa (sin código si no tiene). */
    public PersonaHabitacionDTO habitacionDeResidente(int idResidente) {
        Residente residente = buscarResidente(idResidente);
        Optional<Habitacion> habitacion = habitacionRepository.findByResidenteId(idResidente);
        String codigo = null;                                   // queda null si no tiene habitación
        if (habitacion.isPresent()) {
            codigo = habitacion.get().getCodigo();
        }
        return new PersonaHabitacionDTO(PersonaDTO.desde(residente), codigo);
    }

    /** 2. ¿Qué residentes atiende el enfermero? Los de las habitaciones ocupadas que tiene asignadas. */
    public List<PersonaDTO> residentesDeEnfermero(int idEnfermero) {
        if (!enfermeroRepository.existsById(idEnfermero)) {
            throw new EntidadNoEncontradaException("No existe el enfermero " + idEnfermero + ".");
        }
        List<PersonaDTO> resultado = new ArrayList<>();
        for (Habitacion h : habitacionRepository.findByEnfermeroId(idEnfermero)) {
            if (h.isOcupada()) {
                resultado.add(PersonaDTO.desde(h.getResidente()));
            }
        }
        return resultado;
    }

    /** 3. ¿Qué enfermero atiende al residente? Vacío si no tiene. */
    public Optional<PersonaDTO> enfermeroDeResidente(int idResidente) {
        buscarResidente(idResidente);                           // si no existe, lanza la excepción
        Optional<Habitacion> habitacion = habitacionRepository.findByResidenteId(idResidente);
        if (habitacion.isEmpty() || !habitacion.get().tieneEnfermero()) {
            return Optional.empty();
        }
        return Optional.of(PersonaDTO.desde(habitacion.get().getEnfermero()));
    }

    private Residente buscarResidente(int idResidente) {
        Optional<Residente> residente = residenteRepository.findById(idResidente);
        if (residente.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el residente " + idResidente + ".");
        }
        return residente.get();
    }
}
