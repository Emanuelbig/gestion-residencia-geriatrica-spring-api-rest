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
import prog2.residenciageriatricaspring.modelos.Reserva;
import prog2.residenciageriatricaspring.modelos.Residente;
import prog2.residenciageriatricaspring.repositorios.HabitacionRepository;
import prog2.residenciageriatricaspring.repositorios.ReservaRepository;
import prog2.residenciageriatricaspring.repositorios.ResidenteRepository;

/**
 * Reglas de las reservas: un residente ocupa como máximo una habitación,
 * y una habitación tiene como máximo un residente.
 * @author Emanuel
 */
@Service
public class ReservaServicio {

    private final ReservaRepository reservaRepository;
    private final ResidenteRepository residenteRepository;
    private final HabitacionRepository habitacionRepository;

    public ReservaServicio(ReservaRepository reservaRepository,
                           ResidenteRepository residenteRepository,
                           HabitacionRepository habitacionRepository) {
        this.reservaRepository = reservaRepository;
        this.residenteRepository = residenteRepository;
        this.habitacionRepository = habitacionRepository;
    }

    public void reservar(int idResidente, String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (!residenteRepository.existsById(idResidente)) {
            throw new EntidadNoEncontradaException("No existe el residente " + idResidente + ".");
        }
        if (!habitacionRepository.existsById(normalizado)) {
            throw new EntidadNoEncontradaException("No existe la habitación " + normalizado + ".");
        }
        if (reservaRepository.existsByIdResidente(idResidente)) {
            throw new OperacionNoPermitidaException("El residente " + idResidente + " ya tiene una habitación.");
        }
        if (reservaRepository.existsById(normalizado)) {
            throw new OperacionNoPermitidaException("La habitación " + normalizado + " ya está ocupada.");
        }
        reservaRepository.save(new Reserva(idResidente, normalizado));
    }

    public void liberar(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (!reservaRepository.existsById(normalizado)) {
            throw new EntidadNoEncontradaException("La habitación " + normalizado + " no está ocupada.");
        }
        reservaRepository.deleteById(normalizado);
    }

    /** Habitaciones ocupadas, cada una con su residente. */
    public List<PersonaHabitacionDTO> listar() {
        List<PersonaHabitacionDTO> resultado = new ArrayList<>();
        for (Reserva r : reservaRepository.findAll()) {
            Optional<Residente> residente = residenteRepository.findById(r.getIdResidente());
            if (residente.isPresent()) {
                resultado.add(new PersonaHabitacionDTO(PersonaDTO.desde(residente.get()), r.getCodigoHabitacion()));
            }
        }
        return resultado;
    }

    /** Residentes que todavía no tienen habitación (los únicos que se pueden reservar). */
    public List<PersonaDTO> residentesSinHabitacion() {
        List<PersonaDTO> resultado = new ArrayList<>();
        for (Residente r : residenteRepository.findAll()) {
            if (!reservaRepository.existsByIdResidente(r.getId())) {
                resultado.add(PersonaDTO.desde(r));
            }
        }
        return resultado;
    }

    /** Códigos de las habitaciones libres. */
    public List<String> habitacionesLibres() {
        List<String> resultado = new ArrayList<>();
        for (Habitacion h : habitacionRepository.findAll()) {
            if (!reservaRepository.existsById(h.getCodigo())) {
                resultado.add(h.getCodigo());
            }
        }
        return resultado;
    }
}
