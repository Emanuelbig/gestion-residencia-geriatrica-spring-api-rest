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
import prog2.residenciageriatricaspring.dto.PersonaDTO;
import prog2.residenciageriatricaspring.dto.PersonaHabitacionDTO;
import prog2.residenciageriatricaspring.servicios.ReservaServicio;

/**
 * Endpoints para reservar y liberar habitaciones.
 * @author Emanuel
 */
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaServicio servicio;

    public ReservaController(ReservaServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping                                    // habitaciones ocupadas con su residente
    public List<PersonaHabitacionDTO> listar() {
        return servicio.listar();
    }

    @GetMapping("/residentes-sin-habitacion")
    public List<PersonaDTO> residentesSinHabitacion() {
        return servicio.residentesSinHabitacion();
    }

    @GetMapping("/habitaciones-libres")
    public List<String> habitacionesLibres() {
        return servicio.habitacionesLibres();
    }

    @PostMapping                                   // POST /reservas?idResidente=1&codigo=HAB-101
    @ResponseStatus(HttpStatus.CREATED)
    public void reservar(@RequestParam int idResidente, @RequestParam String codigo) {
        servicio.reservar(idResidente, codigo);
    }

    @DeleteMapping("/{codigo}")                    // DELETE /reservas/HAB-101 → libera la habitación
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void liberar(@PathVariable String codigo) {
        servicio.liberar(codigo);
    }
}
