package prog2.residenciageriatricaspring.excepciones;

/**
 * Se lanza cuando una operación viola una regla del negocio (por ejemplo, reservar una habitación ocupada o borrar un residente que tiene habitación).
 * @author Emanuel
 */
public class OperacionNoPermitidaException extends GeriatricoException {

    private static final long serialVersionUID = 1L;

    public OperacionNoPermitidaException(String message) {
        super(message);
    }
    
}
