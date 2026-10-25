package prog2.residenciageriatricaspring.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import prog2.residenciageriatricaspring.dto.PersonaDTO;
import prog2.residenciageriatricaspring.dto.PersonaHabitacionDTO;
import prog2.residenciageriatricaspring.excepciones.EntidadNoEncontradaException;
import prog2.residenciageriatricaspring.excepciones.OperacionNoPermitidaException;
import prog2.residenciageriatricaspring.modelos.Asignacion;
import prog2.residenciageriatricaspring.modelos.Enfermero;
import prog2.residenciageriatricaspring.modelos.Habitacion;
import prog2.residenciageriatricaspring.repositorios.AsignacionRepository;
import prog2.residenciageriatricaspring.repositorios.EnfermeroRepository;
import prog2.residenciageriatricaspring.repositorios.HabitacionRepository;

/**
 * Reglas de las asignaciones: un enfermero puede atender varias habitaciones,
 * pero cada habitación tiene como máximo un enfermero.
 * @author Emanuel
 */
@Service
public class AsignacionServicio {

    private final AsignacionRepository asignacionRepository;
    private final EnfermeroRepository enfermeroRepository;
    private final HabitacionRepository habitacionRepository;

    public AsignacionServicio(AsignacionRepository asignacionRepository,
                              EnfermeroRepository enfermeroRepository,
                              HabitacionRepository habitacionRepository) {
        this.asignacionRepository = asignacionRepository;
        this.enfermeroRepository = enfermeroRepository;
        this.habitacionRepository = habitacionRepository;
    }

    public void asignar(int idEnfermero, String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (!enfermeroRepository.existsById(idEnfermero)) {
            throw new EntidadNoEncontradaException("No existe el enfermero " + idEnfermero + ".");
        }
        if (!habitacionRepository.existsById(normalizado)) {
            throw new EntidadNoEncontradaException("No existe la habitación " + normalizado + ".");
        }
        if (asignacionRepository.existsById(normalizado)) {
            throw new OperacionNoPermitidaException("La habitación " + normalizado + " ya tiene un enfermero asignado.");
        }
        asignacionRepository.save(new Asignacion(idEnfermero, normalizado));
    }

    public void quitar(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        if (!asignacionRepository.existsById(normalizado)) {
            throw new EntidadNoEncontradaException("La habitación " + normalizado + " no tiene enfermero asignado.");
        }
        asignacionRepository.deleteById(normalizado);
    }

    /** Habitaciones que tienen enfermero, cada una con el suyo. */
    public List<PersonaHabitacionDTO> listar() {
        List<PersonaHabitacionDTO> resultado = new ArrayList<>();
        for (Asignacion a : asignacionRepository.findAll()) {
            Optional<Enfermero> enfermero = enfermeroRepository.findById(a.getIdEnfermero());
            if (enfermero.isPresent()) {
                resultado.add(new PersonaHabitacionDTO(PersonaDTO.desde(enfermero.get()), a.getCodigoHabitacion()));
            }
        }
        return resultado;
    }

    /** Códigos de las habitaciones que todavía no tienen enfermero. */
    public List<String> habitacionesSinEnfermero() {
        List<String> resultado = new ArrayList<>();
        for (Habitacion h : habitacionRepository.findAll()) {
            if (!asignacionRepository.existsById(h.getCodigo())) {
                resultado.add(h.getCodigo());
            }
        }
        return resultado;
    }
}


