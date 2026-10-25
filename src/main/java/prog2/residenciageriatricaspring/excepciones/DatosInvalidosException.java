package prog2.residenciageriatricaspring.excepciones;

/**
 * Se lanza cuando un dato ingresado no tiene el formato correcto (por ejemplo, un DNI con letras).
 * @author Emanuel
 */
public class DatosInvalidosException extends GeriatricoException {

    private static final long serialVersionUID = 1L;

    public DatosInvalidosException(String message) {
        super(message);
    }
    
}
