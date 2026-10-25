package prog2.residenciageriatricaspring.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * Indica qué enfermero atiende una habitación. El id es el código de la habitación:
 * así cada habitación tiene como máximo un enfermero.
 * @author Emanuel
 */

@Entity
@Table(name = "asignaciones")
public class Asignacion {

    @Id
    private String codigoHabitacion;
    private int idEnfermero;

    // JPA lo necesita para crear el objeto cuando lo lee de la base
    protected Asignacion() {
    }

    public Asignacion(int idEnfermero, String codigoHabitacion) {
        this.idEnfermero = idEnfermero;
        this.codigoHabitacion = codigoHabitacion;
    }

    public int getIdEnfermero() {
        return idEnfermero;
    }

    public String getCodigoHabitacion() {
        return codigoHabitacion;
    }

    @Override
    public String toString() {
        return "Asignacion con id del enfermero: " + idEnfermero + " y codigo de habitacion: " + codigoHabitacion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigoHabitacion);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Asignacion other = (Asignacion) obj;
        return Objects.equals(this.codigoHabitacion, other.codigoHabitacion);
    }
}
