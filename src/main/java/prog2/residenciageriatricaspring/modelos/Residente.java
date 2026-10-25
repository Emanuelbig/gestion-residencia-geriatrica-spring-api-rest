package prog2.residenciageriatricaspring.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Persona que vive en la residencia y puede ocupar una habitación.
 * @author Emanuel
 */

@Entity
@Table(name = "residentes")
public class Residente extends Persona {

    // JPA lo necesita para crear el objeto cuando lo lee de la base
    protected Residente() {
    }

    public Residente(Integer id, String dni, String nombre, String apellido) {
        super(id, dni, nombre, apellido);
    }

    @Override
    public String getRol() {
        return "Residente";
    }
}
