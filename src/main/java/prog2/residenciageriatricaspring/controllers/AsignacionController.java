package prog2.residenciageriatricaspring.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import prog2.residenciageriatricaspring.dto.PersonaHabitacionDTO;
import prog2.residenciageriatricaspring.servicios.AsignacionServicio;

/**
 * Endpoints para asignar y quitar enfermeros de las habitaciones.
 * @author Emanuel
 */
@RestController
@RequestMapping("/asignaciones")
public class AsignacionController {

    private final AsignacionServicio servicio;

    public AsignacionController(AsignacionServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping                                    // habitaciones con su enfermero
    public List<PersonaHabitacionDTO> listar() {
        return servicio.listar();
    }

    @GetMapping("/habitaciones-sin-enfermero")
    public List<String> habitacionesSinEnfermero() {
        return servicio.habitacionesSinEnfermero();
    }

    @PostMapping                                   // POST /asignaciones?idEnfermero=1&codigo=HAB-101
    @ResponseStatus(HttpStatus.CREATED)
    public void asignar(@RequestParam int idEnfermero, @RequestParam String codigo) {
        servicio.asignar(idEnfermero, codigo);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void quitar(@PathVariable String codigo) {
        servicio.quitar(codigo);
    }
}
