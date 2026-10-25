package prog2.residenciageriatricaspring.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * Habitación de la residencia, identificada por su código. Si está ocupada no se guarda acá:
 * se calcula a partir de las reservas.
 * @author Emanuel
 */

@Entity
@Table(name = "habitaciones")
public class Habitacion {

    @Id
    private String codigo;

    // JPA lo necesita para crear el objeto cuando lo lee de la base
    protected Habitacion() {
    }

    public Habitacion(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public static String normalizarCodigo(String codigo) {
        return codigo == null ? "" : codigo.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return "Habitacion con codigo: " + codigo;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Habitacion other = (Habitacion) obj;
        return Objects.equals(this.codigo, other.codigo);
    }
}
