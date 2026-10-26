package prog2.residenciageriatricaspring.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import prog2.residenciageriatricaspring.dto.HabitacionDTO;
import prog2.residenciageriatricaspring.dto.HabitacionDetalleDTO;
import prog2.residenciageriatricaspring.dto.PersonaDTO;
import prog2.residenciageriatricaspring.excepciones.EntidadNoEncontradaException;
import prog2.residenciageriatricaspring.excepciones.OperacionNoPermitidaException;
import prog2.residenciageriatricaspring.modelos.Habitacion;
import prog2.residenciageriatricaspring.repositorios.HabitacionRepository;

/**
 * Reglas de negocio de las habitaciones: alta, listado, consulta y baja.
 * La habitación está ocupada cuando tiene un residente guardado (id_residente).
 * @author Emanuel
 */
@Service
public class HabitacionServicio {

    private static final String PREFIJO = "HAB-";
    private static final int PRIMER_NUMERO = 101;

    private final HabitacionRepository habitacionRepository;

    public HabitacionServicio(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    /** Da de alta una habitación con el siguiente código libre y lo devuelve. */
    public HabitacionDTO crear() {
        String codigo = PREFIJO + generarNumero();
        habitacionRepository.save(new Habitacion(codigo));
        return new HabitacionDTO(codigo, false);           // recién creada: siempre libre
    }

    // Busca el número más alto y le suma 1: HAB-105 → HAB-106
    private int generarNumero() {
        int mayor = PRIMER_NUMERO - 1;
        for (Habitacion h : habitacionRepository.findAll()) {
            int numero = Integer.parseInt(h.getCodigo().substring(PREFIJO.length()));
            if (numero > mayor) {
                mayor = numero;
            }
        }
        return mayor + 1;
    }

    public List<HabitacionDTO> listar() {
        List<HabitacionDTO> resultado = new ArrayList<>();
        for (Habitacion h : habitacionRepository.findAll()) {
            resultado.add(new HabitacionDTO(h.getCodigo(), h.isOcupada()));
        }
        return resultado;
    }

    /** Detalle de una habitación: quién la ocupa y qué enfermero la atiende. */
    public HabitacionDetalleDTO obtener(String codigo) {
        Habitacion habitacion = buscar(Habitacion.normalizarCodigo(codigo));
        PersonaDTO residente = null;                   // quedan null si no hay
        PersonaDTO enfermero = null;
        if (habitacion.isOcupada()) {
            residente = PersonaDTO.desde(habitacion.getResidente());
        }
        if (habitacion.tieneEnfermero()) {
            enfermero = PersonaDTO.desde(habitacion.getEnfermero());
        }
        return new HabitacionDetalleDTO(habitacion.getCodigo(), residente, enfermero);
    }

    public void eliminar(String codigo) {
        Habitacion habitacion = buscar(Habitacion.normalizarCodigo(codigo));
        if (habitacion.isOcupada()) {
            throw new OperacionNoPermitidaException("La habitación está ocupada. Liberala primero.");
        }
        if (habitacion.tieneEnfermero()) {
            throw new OperacionNoPermitidaException("La habitación tiene un enfermero asignado. Quitalo primero.");
        }
        habitacionRepository.deleteById(habitacion.getCodigo());
    }

    private Habitacion buscar(String codigo) {
        Optional<Habitacion> habitacion = habitacionRepository.findById(codigo);
        if (habitacion.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe la habitación " + codigo + ".");
        }
        return habitacion.get();
    }
}
