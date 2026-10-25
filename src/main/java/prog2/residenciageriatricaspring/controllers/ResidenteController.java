package prog2.residenciageriatricaspring.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prog2.residenciageriatricaspring.servicios.ResidenteServicio;

/**
 * Endpoints de residentes: todos heredados de PersonaController.
 * @author Emanuel
 */
@RestController
@RequestMapping("/residentes")
public class ResidenteController extends PersonaController {

    public ResidenteController(ResidenteServicio servicio) {
        super(servicio);
    }
}
