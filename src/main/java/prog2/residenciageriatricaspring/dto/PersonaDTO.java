package prog2.residenciageriatricaspring.dto;

import prog2.residenciageriatricaspring.modelos.Persona;

/**
 * Datos de una persona (residente o enfermero) que viajan entre la vista y el service. El id es null en el alta, porque lo genera el sistema.
 * @author Emanuel
 */

public class PersonaDTO {
    
    public static final String PATRON_DNI = "\\d{7,8}";
    public static final String PATRON_NOMBRE = "[\\p{L} ]{2,40}";

    private final String dni;
    private final String nombre;
    private final String apellido;
    
    private final Integer id;

    public PersonaDTO(String dni, String nombre, String apellido, Integer id) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
    }

    

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Integer getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /** Arma el DTO a partir de un residente o un enfermero (así no se repite en cada service). */
    public static PersonaDTO desde(Persona p) {
        return new PersonaDTO(p.getDni(), p.getNombre(), p.getApellido(), p.getId());
    }
}
