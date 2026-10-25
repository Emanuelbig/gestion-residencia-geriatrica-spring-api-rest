package prog2.residenciageriatricaspring.dto;

/**
 * Datos de una habitación para mostrar al usuario. Incluye si está ocupada, que se calcula a partir de las reservas y no se guarda en Habitacion.
 * @author Emanuel
 */
public class HabitacionDTO {
    
    public static final String PATRON_CODIGO = "(?i)HAB-\\d{3,}";
    
    private final String codigo;
    private final boolean ocupada;

    public HabitacionDTO(String codigo, boolean ocupada) {
        this.codigo = codigo;
        this.ocupada = ocupada;
    }

    public String getCodigo() {
        return codigo;
    }

    public boolean isOcupada() {
        return ocupada;
    }
    
}
