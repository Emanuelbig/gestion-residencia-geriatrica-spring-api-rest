package prog2.residenciageriatricaspring.dto;

/**
 * Una persona (residente o enfermero) junto con el código de una habitación.
 * El código es null cuando la persona no tiene habitación.
 * @author Emanuel
 */
public class PersonaHabitacionDTO {

    private final PersonaDTO persona;
    private final String codigoHabitacion;

    public PersonaHabitacionDTO(PersonaDTO persona, String codigoHabitacion) {
        this.persona = persona;
        this.codigoHabitacion = codigoHabitacion;
    }

    public PersonaDTO getPersona() {
        return persona;
    }

    public String getCodigoHabitacion() {
        return codigoHabitacion;
    }

    public boolean tieneHabitacion() {
        return codigoHabitacion != null;
    }
}
