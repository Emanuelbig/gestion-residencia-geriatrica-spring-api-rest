package prog2.residenciageriatricaspring.dto;

/**
 * Todo lo que se sabe de una habitación: su código, quién la ocupa y qué enfermero la atiende.
 * Residente y enfermero son null cuando no hay.
 * @author Emanuel
 */
public class HabitacionDetalleDTO {

    private final String codigo;
    private final PersonaDTO residente;
    private final PersonaDTO enfermero;

    public HabitacionDetalleDTO(String codigo, PersonaDTO residente, PersonaDTO enfermero) {
        this.codigo = codigo;
        this.residente = residente;
        this.enfermero = enfermero;
    }

    public String getCodigo() {
        return codigo;
    }

    public PersonaDTO getResidente() {
        return residente;
    }

    public PersonaDTO getEnfermero() {
        return enfermero;
    }

    public boolean isOcupada() {
        return residente != null;
    }

    public boolean tieneEnfermero() {
        return enfermero != null;
    }
}
