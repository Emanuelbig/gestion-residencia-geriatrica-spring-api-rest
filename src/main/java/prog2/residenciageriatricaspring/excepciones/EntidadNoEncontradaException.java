package prog2.residenciageriatricaspring.excepciones;

/**
 * Se lanza cuando se busca un id o código que no existe.
 * @author Emanuel
 */
public class EntidadNoEncontradaException extends GeriatricoException {

    private static final long serialVersionUID = 1L;

    public EntidadNoEncontradaException(String message) {
        super(message);
    }
    
}
