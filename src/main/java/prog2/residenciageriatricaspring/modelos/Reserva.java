package prog2.residenciageriatricaspring.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * Indica qué residente ocupa una habitación. El id es el código de la habitación:
 * así la base garantiza que cada habitación tenga como máximo una reserva.
 * @author Emanuel
 */

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    private String codigoHabitacion;
    private int idResidente;

    // JPA lo necesita para crear el objeto cuando lo lee de la base
    protected Reserva() {
    }

    public Reserva(int idResidente, String codigoHabitacion) {
        this.idResidente = idResidente;
        this.codigoHabitacion = codigoHabitacion;
    }

    public int getIdResidente() {
        return idResidente;
    }

    public String getCodigoHabitacion() {
        return codigoHabitacion;
    }

    @Override
    public String toString() {
        return "Reserva con id del residente: " + idResidente + " y codigo de habitacion: " + codigoHabitacion;
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
        final Reserva other = (Reserva) obj;
        return Objects.equals(this.codigoHabitacion, other.codigoHabitacion);
    }
}