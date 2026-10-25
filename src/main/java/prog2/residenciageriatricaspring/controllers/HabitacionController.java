package prog2.residenciageriatricaspring.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import prog2.residenciageriatricaspring.dto.HabitacionDTO;
import prog2.residenciageriatricaspring.dto.HabitacionDetalleDTO;
import prog2.residenciageriatricaspring.servicios.HabitacionServicio;

/**
 * Endpoints de habitaciones. No hay PUT: el código es la identidad y el estado se calcula.
 * @author Emanuel
 */
@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    private final HabitacionServicio servicio;

    public HabitacionController(HabitacionServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<HabitacionDTO> listar() {
        return servicio.listar();
    }

    @GetMapping("/{codigo}")                       // GET /habitaciones/HAB-101
    public HabitacionDetalleDTO buscar(@PathVariable String codigo) {
        return servicio.obtener(codigo);
    }

    @PostMapping                                   // POST /habitaciones (sin body: el código se genera)
    @ResponseStatus(HttpStatus.CREATED)
    public HabitacionDTO alta() {
        return servicio.crear();
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void baja(@PathVariable String codigo) {
        servicio.eliminar(codigo);
    }
}
