package prog2.residenciageriatricaspring.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import prog2.residenciageriatricaspring.dto.PersonaDTO;
import prog2.residenciageriatricaspring.excepciones.DatosInvalidosException;
import prog2.residenciageriatricaspring.excepciones.EntidadDuplicadaException;
import prog2.residenciageriatricaspring.excepciones.EntidadNoEncontradaException;
import prog2.residenciageriatricaspring.excepciones.OperacionNoPermitidaException;
import prog2.residenciageriatricaspring.modelos.Persona;
import prog2.residenciageriatricaspring.repositorios.PersonaRepository;

/**
 * Reglas comunes a todas las personas del sistema: alta, listado, modificación
 * y baja. Cada subclase indica cómo se crea su tipo de persona y cuándo está en
 * uso.
 *
 * @param <T> tipo de persona (Residente, Enfermero)
 * @author Emanuel
 */
public abstract class PersonaServicio<T extends Persona> {

    private final PersonaRepository<T> repository;
    private final PersonaRepository<? extends Persona> otrasPersonas;   // el DNI no se repite entre tipos

    protected PersonaServicio(PersonaRepository<T> repository,
            PersonaRepository<? extends Persona> otrasPersonas) {
        this.repository = repository;
        this.otrasPersonas = otrasPersonas;
    }

    /**
     * Crea el objeto concreto (new Residente, new Enfermero...).
     */
    protected abstract T crearPersona(Integer id, String dni, String nombre, String apellido);

    /**
     * Indica si la persona está siendo usada (reserva, asignaciones) y no se
     * puede borrar.
     */
    protected abstract boolean estaEnUso(int id);

    /**
     * Mensaje para cuando no se puede borrar porque está en uso.
     */
    protected abstract String mensajeEnUso(int id);

    /**
     * Nombre para los mensajes: "residente", "enfermero".
     */
    protected abstract String nombreEntidad();

    /**
     * Da de alta y devuelve la persona con el id que le asignó la base.
     */
    public PersonaDTO crear(PersonaDTO datos) {
        datos = limpiar(datos);
        validar(datos);
        if (existeDni(datos.getDni())) {
            throw new EntidadDuplicadaException("Ya existe una persona con el DNI " + datos.getDni() + ".");
        }
        // id null: lo genera MySQL al guardar
        T guardada = repository.save(crearPersona(null, datos.getDni(), datos.getNombre(), datos.getApellido()));
        return PersonaDTO.desde(guardada);
    }

    public List<PersonaDTO> listar() {
        List<PersonaDTO> resultado = new ArrayList<>();
        for (T p : repository.findAll()) {
            resultado.add(PersonaDTO.desde(p));
        }
        return resultado;
    }

    public PersonaDTO obtener(int id) {
        return PersonaDTO.desde(buscar(id));
    }

    public PersonaDTO modificar(int id, PersonaDTO datos) {
        datos = limpiar(datos);
        T actual = buscar(id);
        validar(datos);
        boolean cambioDni = !actual.getDni().equals(datos.getDni());
        if (cambioDni && existeDni(datos.getDni())) {
            throw new EntidadDuplicadaException("Ya existe una persona con el DNI " + datos.getDni() + ".");
        }
        // save con un id que ya existe hace UPDATE en vez de INSERT
        T guardada = repository.save(crearPersona(id, datos.getDni(), datos.getNombre(), datos.getApellido()));
        return PersonaDTO.desde(guardada);
    }

    public void eliminar(int id) {
        buscar(id);
        if (estaEnUso(id)) {
            throw new OperacionNoPermitidaException(mensajeEnUso(id));
        }
        repository.deleteById(id);
    }

    private T buscar(int id) {
        Optional<T> encontrado = repository.findById(id);
        if (encontrado.isEmpty()) {
            throw new EntidadNoEncontradaException("No existe el " + nombreEntidad() + " " + id + ".");
        }
        return encontrado.get();
    }

    private PersonaDTO limpiar(PersonaDTO datos) {
        String dni = datos.getDni() == null ? null : datos.getDni().trim();
        return new PersonaDTO(dni, Persona.normalizarNombre(datos.getNombre()),
                Persona.normalizarNombre(datos.getApellido()), datos.getId());
    }

    private void validar(PersonaDTO datos) {
        if (datos.getDni() == null || !datos.getDni().matches(PersonaDTO.PATRON_DNI)) {
            throw new DatosInvalidosException("El DNI debe tener 7 u 8 números.");
        }
        if (!esNombreValido(datos.getNombre())) {
            throw new DatosInvalidosException("El nombre solo puede tener letras y espacios.");
        }
        if (!esNombreValido(datos.getApellido())) {
            throw new DatosInvalidosException("El apellido solo puede tener letras y espacios.");
        }
    }

    private boolean esNombreValido(String texto) {
        return texto != null && texto.matches(PersonaDTO.PATRON_NOMBRE);
    }

    private boolean existeDni(String dni) {
        return repository.existsByDni(dni) || otrasPersonas.existsByDni(dni);
    }
}
