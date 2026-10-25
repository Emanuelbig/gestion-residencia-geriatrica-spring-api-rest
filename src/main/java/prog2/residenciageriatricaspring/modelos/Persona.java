package prog2.residenciageriatricaspring.modelos;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * Datos comunes de toda persona del sistema. Cada subclase define su rol.
 * No es una tabla: sus campos se copian a las tablas de las hijas (residentes, enfermeros).
 *
 * @author Emanuel Morano Sole
 */

@MappedSuperclass
public abstract class Persona {

    // lo genera MySQL al guardar (AUTO_INCREMENT) y nunca se reutiliza;
    // es Integer porque vale null hasta que la persona se guarda
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dni;
    private String nombre;
    private String apellido;

    // JPA lo necesita para crear el objeto cuando lo lee de la base
    protected Persona() {
    }

    // solo pueden usar este constructor las clases hijas
    protected Persona(Integer id, String dni, String nombre, String apellido) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Integer getId() {
        return id;
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

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Saca espacios de más y pone mayúscula inicial: " morano sole" → "Morano Sole".
     */
    public static String normalizarNombre(String texto) {
        if (texto == null) {
            return null;
        }
        StringBuilder resultado = new StringBuilder();
        for (String palabra : texto.trim().toLowerCase().split(" ")) {
            if (!palabra.isEmpty()) {
                if (resultado.length() > 0) {
                    resultado.append(" ");
                }
                resultado.append(Character.toUpperCase(palabra.charAt(0)));
                resultado.append(palabra.substring(1));
            }
        }
        return resultado.toString();
    }

    public abstract String getRol();

    @Override
    public String toString() {
        return getRol() + " con id: " + id + ", dni: " + dni + ", nombre: " + nombre + ", apellido: " + apellido + '.';
    }

    // dos personas son iguales si tienen el mismo id
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Persona other = (Persona) obj;
        return Objects.equals(this.id, other.id);
    }
}