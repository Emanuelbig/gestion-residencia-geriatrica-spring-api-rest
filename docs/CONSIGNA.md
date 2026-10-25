# Consigna del parcial

[← Volver al README](../README.md)

Parcial de **Programación II** (INSPT-UTN, Prof. Miguel Silva Campos). La consigna original pedía un sistema por consola; esta versión resuelve lo mismo como API REST con Spring Boot.

## Sistema de Gestión de Residencia Geriátrica

Desarrollar una aplicación en Java para gestionar la información de una residencia geriátrica, incluyendo residentes, enfermeros y habitaciones. El sistema debe permitir realizar operaciones CRUD sobre cada entidad y gestionar sus relaciones.

### Objetos

| Objeto | Datos |
|---|---|
| Residente | ID, DNI, nombre, apellido |
| Enfermero | ID, DNI, nombre, apellido |
| Habitación | Código, estado (ocupada / libre) |
| Reserva | Código de habitación, ID de residente |
| Asignación | Código de habitación, ID de enfermero |

### Relaciones

- Residentes ↔ Habitaciones: **uno a uno**.
- Enfermeros → Habitaciones: **uno a muchos**.

### Funcionalidades

Un administrativo debe tener control absoluto sobre el sistema:

- Ingresar, modificar, consultar o eliminar datos de residentes, enfermeros y habitaciones.
- Asignar residentes a habitaciones.
- Asignar enfermeros a habitaciones.
- Consultar:
  - El nombre del residente con su habitación asignada.
  - Los residentes atendidos por un enfermero.
  - El enfermero asignado a un residente.

Cada entidad debe tener sus operaciones CRUD y funcionalidades relacionadas.

Los ids y los códigos se generan automáticamente y de manera incremental. El usuario solo los ingresa para consultar.

---

## Dónde se cumple cada punto

| Punto de la consigna | Endpoint | Clase |
|---|---|---|
| CRUD de residentes | `GET` / `POST` `/residentes`, `GET` / `PUT` / `DELETE` `/residentes/{id}` | `ResidenteController` → `PersonaServicio<T>` |
| CRUD de enfermeros | Los mismos, en `/enfermeros` | `EnfermeroController` → `PersonaServicio<T>` |
| CRUD de habitaciones | `GET` / `POST` `/habitaciones`, `GET` / `DELETE` `/habitaciones/{codigo}` | `HabitacionController` → `HabitacionServicio`. No hay `PUT`: el código es la identidad y el estado se calcula |
| Estado ocupada / libre | `GET /habitaciones` (campo `ocupada`) | Se calcula a partir de las reservas (`HabitacionServicio.listar()`), no se guarda en la tabla |
| Residente ↔ habitación, uno a uno | `POST /reservas?idResidente=&codigo=`, `DELETE /reservas/{codigo}` | `ReservaServicio.reservar()`. Además, la clave primaria de `reservas` es el código de habitación |
| Enfermero → habitaciones, uno a muchos | `POST /asignaciones?idEnfermero=&codigo=`, `DELETE /asignaciones/{codigo}` | `AsignacionServicio.asignar()`. La clave de `asignaciones` es el código de habitación |
| Consulta 1: residente con su habitación | `GET /reportes/residentes/{id}/habitacion` | `ReporteServicio.habitacionDeResidente()` |
| Consulta 2: residentes de un enfermero | `GET /reportes/enfermeros/{id}/residentes` | `ReporteServicio.residentesDeEnfermero()` |
| Consulta 3: enfermero de un residente | `GET /reportes/residentes/{id}/enfermero` | `ReporteServicio.enfermeroDeResidente()` |
| Ids automáticos e incrementales | — | `@GeneratedValue(IDENTITY)` en `Persona`: MySQL los genera con `AUTO_INCREMENT` y nunca los reutiliza |
| Códigos de habitación automáticos | `POST /habitaciones` (sin body) | `HabitacionServicio.crear()`: HAB-101, HAB-102... |
| Validaciones y errores | Respuestas 400 / 404 / 409 con `{"error": "..."}` | Excepciones propias + `ManejadorDeErrores` (`@RestControllerAdvice`) |
