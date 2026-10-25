package prog2.residenciageriatricaspring.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prog2.residenciageriatricaspring.servicios.EnfermeroServicio;

/**
 * Endpoints de enfermeros: todos heredados de PersonaController.
 * @author Emanuel
 */
@RestController
@RequestMapping("/enfermeros")
public class EnfermeroController extends PersonaController {

    public EnfermeroController(EnfermeroServicio servicio) {
        super(servicio);
    }
}
