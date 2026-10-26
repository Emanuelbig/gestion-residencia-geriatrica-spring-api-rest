package prog2.residenciageriatricaspring.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import prog2.residenciageriatricaspring.dto.PersonaDTO;
import prog2.residenciageriatricaspring.dto.PersonaHabitacionDTO;
import prog2.residenciageriatricaspring.excepciones.EntidadNoEncontradaException;
import prog2.residenciageriatricaspring.excepciones.OperacionNoPermitidaException;
import prog2.residenciageriatricaspring.modelos.Habitacion;
import prog2.residenciageriatricaspring.modelos.Residente;
import prog2.residenciageriatricaspring.repositorios.HabitacionRepository;
import prog2.residenciageriatricaspring.repositorios.ResidenteRepository;

/**
 * Reglas de las reservas: un residente ocupa como máximo una habitación,
 * y una habitación tiene como máximo un residente. La reserva se guarda en la habitación (id_residente).
 * @author Emanuel
 */
@Service
public class ReservaServicio {

    private final HabitacionRepository habitacionRepository;
    private final ResidenteRepository residenteRepository;

    public ReservaServicio(HabitacionRepository habitacionRepository,
                           ResidenteRepository residenteRepository) {
        this.habitacionRepository = habitacionRepository;
        this.residenteRepository = residenteRepository;
    }

    public void reservar(int idResidente, String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        Optional<Residente> residente = residenteRepository.findById(idResidente);
        if (residente.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el residente " + idResidente + ".");
        }
        Habitacion habitacion = buscarHabitacion(normalizado);
        if (habitacionRepository.existsByResidenteId(idResidente)) {
            throw new OperacionNoPermitidaException("El residente " + idResidente + " ya tiene una habitación.");
        }
        if (habitacion.isOcupada()) {
            throw new OperacionNoPermitidaException("La habitación " + normalizado + " ya está ocupada.");
        }
        habitacionRepository.save(habitacion.conResidente(residente.get()));   // UPDATE de id_residente
    }

    public void liberar(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        Habitacion habitacion = buscarHabitacion(normalizado);
        if (!habitacion.isOcupada()) {
            throw new EntidadNoEncontradaException("La habitación " + normalizado + " no está ocupada.");
        }
        habitacionRepository.save(habitacion.conResidente(null));             // id_residente = NULL
    }

    /** Habitaciones ocupadas, cada una con su residente. */
    public List<PersonaHabitacionDTO> listar() {
        List<PersonaHabitacionDTO> resultado = new ArrayList<>();
        for (Habitacion h : habitacionRepository.findByResidenteIsNotNull()) {
            resultado.add(new PersonaHabitacionDTO(PersonaDTO.desde(h.getResidente()), h.getCodigo()));
        }
        return resultado;
    }

    /** Residentes que todavía no tienen habitación (los únicos que se pueden reservar). */
    public List<PersonaDTO> residentesSinHabitacion() {
        List<PersonaDTO> resultado = new ArrayList<>();
        for (Residente r : residenteRepository.findAll()) {
            if (!habitacionRepository.existsByResidenteId(r.getId())) {
                resultado.add(PersonaDTO.desde(r));
            }
        }
        return resultado;
    }

    /** Códigos de las habitaciones libres. */
    public List<String> habitacionesLibres() {
        List<String> resultado = new ArrayList<>();
        for (Habitacion h : habitacionRepository.findByResidenteIsNull()) {
            resultado.add(h.getCodigo());
        }
        return resultado;
    }

    private Habitacion buscarHabitacion(String codigo) {
        Optional<Habitacion> habitacion = habitacionRepository.findById(codigo);
        if (habitacion.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe la habitación " + codigo + ".");
        }
        return habitacion.get();
    }
}
