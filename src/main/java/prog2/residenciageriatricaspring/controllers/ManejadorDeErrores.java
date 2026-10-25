package prog2.residenciageriatricaspring.controllers;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import prog2.residenciageriatricaspring.excepciones.DatosInvalidosException;
import prog2.residenciageriatricaspring.excepciones.EntidadDuplicadaException;
import prog2.residenciageriatricaspring.excepciones.EntidadNoEncontradaException;
import prog2.residenciageriatricaspring.excepciones.GeriatricoException;
import prog2.residenciageriatricaspring.excepciones.OperacionNoPermitidaException;

/**
 * Atrapa las excepciones de todos los controllers y las traduce a un código HTTP
 * con el mensaje en JSON: {"error": "..."}. Cumple el papel que en la consola tenía la vista.
 * @author Emanuel
 */
@RestControllerAdvice
public class ManejadorDeErrores {

    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<Map<String, String>> datosInvalidos(DatosInvalidosException e) {
        return responder(HttpStatus.BAD_REQUEST, e.getMessage());                  // 400
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> noEncontrada(EntidadNoEncontradaException e) {
        return responder(HttpStatus.NOT_FOUND, e.getMessage());                    // 404
    }

    // las dos son "choca con lo que ya hay": mismo código
    @ExceptionHandler({EntidadDuplicadaException.class, OperacionNoPermitidaException.class})
    public ResponseEntity<Map<String, String>> conflicto(GeriatricoException e) {
        return responder(HttpStatus.CONFLICT, e.getMessage());                     // 409
    }

    // el cuerpo del pedido no es un JSON válido (por ejemplo, falta una coma en Postman)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> jsonInvalido(HttpMessageNotReadableException e) {
        return responder(HttpStatus.BAD_REQUEST, "El cuerpo del pedido no es un JSON válido.");
    }

    // un dato de la URL con el tipo equivocado, por ejemplo /residentes/abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> tipoInvalido(MethodArgumentTypeMismatchException e) {
        return responder(HttpStatus.BAD_REQUEST, "El dato '" + e.getName() + "' no tiene el formato correcto.");
    }

    private ResponseEntity<Map<String, String>> responder(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado).body(Map.of("error", mensaje));
    }
}
