package prog2.residenciageriatricaspring.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * Habitación de la residencia. Guarda qué residente la ocupa y qué enfermero la atiende (null si no hay),
 * así las reservas y las asignaciones no necesitan una tabla propia.
 * @author Emanuel
 */

@Entity
@Table(name = "habitaciones")
public class Habitacion {

    @Id
    private String codigo;

    // columna id_residente con FK a residentes. unique: un residente ocupa una sola habitación (uno a uno)
    @OneToOne
    @JoinColumn(name = "id_residente", unique = true)
    private Residente residente;            // null = libre

    // columna id_enfermero con FK a enfermeros. Sin unique: un enfermero atiende varias habitaciones (uno a muchos)
    @ManyToOne
    @JoinColumn(name = "id_enfermero")
    private Enfermero enfermero;            // null = sin enfermero

    // JPA lo necesita para crear el objeto cuando lo lee de la base
    protected Habitacion() {
    }

    public Habitacion(String codigo) {
        this(codigo, null, null);           // una habitación nueva está libre y sin enfermero
    }

    private Habitacion(String codigo, Residente residente, Enfermero enfermero) {
        this.codigo = codigo;
        this.residente = residente;
        this.enfermero = enfermero;
    }

    public String getCodigo() {
        return codigo;
    }

    public Residente getResidente() {
        return residente;
    }

    public Enfermero getEnfermero() {
        return enfermero;
    }

    public boolean isOcupada() {
        return residente != null;
    }

    public boolean tieneEnfermero() {
        return enfermero != null;
    }

    // Igual que en la consola: reservar o asignar devuelve una copia con el dato nuevo, y el servicio la guarda con save.
    /** Copia de esta habitación con otro residente (null para liberarla). */
    public Habitacion conResidente(Residente residente) {
        return new Habitacion(codigo, residente, enfermero);
    }

    /** Copia de esta habitación con otro enfermero (null para quitarlo). */
    public Habitacion conEnfermero(Enfermero enfermero) {
        return new Habitacion(codigo, residente, enfermero);
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
