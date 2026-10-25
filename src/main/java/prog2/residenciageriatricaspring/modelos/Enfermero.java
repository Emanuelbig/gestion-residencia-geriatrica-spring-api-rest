package prog2.residenciageriatricaspring.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Persona del personal que atiende una o varias habitaciones.
 * @author Emanuel
 */

@Entity
@Table(name = "enfermeros")
public class Enfermero extends Persona {

    // JPA lo necesita para crear el objeto cuando lo lee de la base
    protected Enfermero() {
    }

    public Enfermero(Integer id, String dni, String nombre, String apellido) {
        super(id, dni, nombre, apellido);
    }

    @Override
    public String getRol() {
        return "Enfermero";
    }
}