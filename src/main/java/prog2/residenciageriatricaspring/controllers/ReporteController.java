package prog2.residenciageriatricaspring.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prog2.residenciageriatricaspring.dto.PersonaDTO;
import prog2.residenciageriatricaspring.dto.PersonaHabitacionDTO;
import prog2.residenciageriatricaspring.servicios.ReporteServicio;

/**
 * Las 3 consultas de la consigna.
 * @author Emanuel
 */
@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteServicio servicio;

    public ReporteController(ReporteServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/residentes/{id}/habitacion")     // 1. el residente con su habitación
    public PersonaHabitacionDTO habitacionDeResidente(@PathVariable int id) {
        return servicio.habitacionDeResidente(id);
    }

    @GetMapping("/enfermeros/{id}/residentes")     // 2. los residentes que atiende un enfermero
    public List<PersonaDTO> residentesDeEnfermero(@PathVariable int id) {
        return servicio.residentesDeEnfermero(id);
    }

    @GetMapping("/residentes/{id}/enfermero")      // 3. el enfermero de un residente
    public ResponseEntity<PersonaDTO> enfermeroDeResidente(@PathVariable int id) {
        Optional<PersonaDTO> enfermero = servicio.enfermeroDeResidente(id);
        if (enfermero.isPresent()) {
            return ResponseEntity.ok(enfermero.get());            // 200 con el enfermero
        }
        return ResponseEntity.noContent().build();                // 204: existe, pero no tiene enfermero
    }
}
