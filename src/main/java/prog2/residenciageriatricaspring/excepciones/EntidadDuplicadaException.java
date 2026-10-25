package prog2.residenciageriatricaspring.excepciones;

/**
 * Se lanza cuando se intenta registrar algo que ya existe (por ejemplo, un DNI repetido).
 * @author Emanuel
 */
public class EntidadDuplicadaException extends GeriatricoException {

    private static final long serialVersionUID = 1L;

    public EntidadDuplicadaException(String message) {
        super(message);
    }
    
}
