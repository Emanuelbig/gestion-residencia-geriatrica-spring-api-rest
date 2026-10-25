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
import prog2.residenciageriatricaspring.modelos.Asignacion;
import prog2.residenciageriatricaspring.modelos.Enfermero;
import prog2.residenciageriatricaspring.modelos.Habitacion;
import prog2.residenciageriatricaspring.modelos.Reserva;
import prog2.residenciageriatricaspring.modelos.Residente;
import prog2.residenciageriatricaspring.repositorios.AsignacionRepository;
import prog2.residenciageriatricaspring.repositorios.EnfermeroRepository;
import prog2.residenciageriatricaspring.repositorios.HabitacionRepository;
import prog2.residenciageriatricaspring.repositorios.ReservaRepository;
import prog2.residenciageriatricaspring.repositorios.ResidenteRepository;

/**
 * Reglas de negocio de las habitaciones: alta, listado, consulta y baja. Si una
 * habitación está ocupada no se guarda: se calcula a partir de las reservas.
 * @author Emanuel
 */
@Service
public class HabitacionServicio {

    private static final String PREFIJO = "HAB-";
    private static final int PRIMER_NUMERO = 101;

    private final HabitacionRepository habitacionRepository;
    private final ReservaRepository reservaRepository;
    private final AsignacionRepository asignacionRepository;
    private final ResidenteRepository residenteRepository;   // para mostrar quién la ocupa
    private final EnfermeroRepository enfermeroRepository;   // para mostrar quién la atiende

    public HabitacionServicio(HabitacionRepository habitacionRepository,
                              ReservaRepository reservaRepository,
                              AsignacionRepository asignacionRepository,
                              ResidenteRepository residenteRepository,
                              EnfermeroRepository enfermeroRepository) {
        this.habitacionRepository = habitacionRepository;
        this.reservaRepository = reservaRepository;
        this.asignacionRepository = asignacionRepository;
        this.residenteRepository = residenteRepository;
        this.enfermeroRepository = enfermeroRepository;
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
            boolean ocupada = reservaRepository.existsById(h.getCodigo());
            resultado.add(new HabitacionDTO(h.getCodigo(), ocupada));
        }
        return resultado;
    }

    /** Detalle de una habitación: quién la ocupa y qué enfermero la atiende. */
    public HabitacionDetalleDTO obtener(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        verificarQueExiste(normalizado);
        PersonaDTO residente = null;                   // quedan null si no hay
        PersonaDTO enfermero = null;
        Optional<Reserva> reserva = reservaRepository.findById(normalizado);
        if (reserva.isPresent()) {
            Optional<Residente> r = residenteRepository.findById(reserva.get().getIdResidente());
            if (r.isPresent()) {
                residente = PersonaDTO.desde(r.get());
            }
        }
        Optional<Asignacion> asignacion = asignacionRepository.findById(normalizado);
        if (asignacion.isPresent()) {
            Optional<Enfermero> e = enfermeroRepository.findById(asignacion.get().getIdEnfermero());
            if (e.isPresent()) {
                enfermero = PersonaDTO.desde(e.get());
            }
        }
        return new HabitacionDetalleDTO(normalizado, residente, enfermero);
    }

    public void eliminar(String codigo) {
        String normalizado = Habitacion.normalizarCodigo(codigo);
        verificarQueExiste(normalizado);
        if (reservaRepository.existsById(normalizado)) {
            throw new OperacionNoPermitidaException("La habitación está ocupada. Liberala primero.");
        }
        if (asignacionRepository.existsById(normalizado)) {
            throw new OperacionNoPermitidaException("La habitación tiene un enfermero asignado. Quitalo primero.");
        }
        habitacionRepository.deleteById(normalizado);
    }

    private void verificarQueExiste(String codigo) {
        if (!habitacionRepository.existsById(codigo)) {
            throw new EntidadNoEncontradaException("No existe la habitación " + codigo + ".");
        }
    }
}
