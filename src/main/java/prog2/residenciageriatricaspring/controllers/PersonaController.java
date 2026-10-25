package prog2.residenciageriatricaspring.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import prog2.residenciageriatricaspring.dto.PersonaDTO;
import prog2.residenciageriatricaspring.servicios.PersonaServicio;

/**
 * Endpoints comunes a residentes y enfermeros. Cada hija dice su URL (/residentes, /enfermeros).
 * Es la misma idea que VistaPersonas en la consola: una sola clase para los dos tipos.
 * @author Emanuel
 */
public abstract class PersonaController {

    private final PersonaServicio<?> servicio;

    protected PersonaController(PersonaServicio<?> servicio) {
        this.servicio = servicio;
    }

    @GetMapping                                    // GET /residentes
    public List<PersonaDTO> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")                           // GET /residentes/1
    public PersonaDTO buscar(@PathVariable int id) {
        return servicio.obtener(id);
    }

    @PostMapping                                   // POST /residentes con el JSON en el body
    @ResponseStatus(HttpStatus.CREATED)            // 201: se creó
    public PersonaDTO alta(@RequestBody PersonaDTO datos) {
        return servicio.crear(datos);
    }

    @PutMapping("/{id}")                           // PUT /residentes/1 con el JSON en el body
    public PersonaDTO modificar(@PathVariable int id, @RequestBody PersonaDTO datos) {
        return servicio.modificar(id, datos);
    }

    @DeleteMapping("/{id}")                        // DELETE /residentes/1
    @ResponseStatus(HttpStatus.NO_CONTENT)         // 204: se borró, no hay nada que devolver
    public void baja(@PathVariable int id) {
        servicio.eliminar(id);
    }
}