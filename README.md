# Clínica Dental

Aplicación web de gestión de pacientes y citas, implementada con **Servlets, JSP, JSTL y JDBC** sobre MySQL. La lógica HTTP está en Servlets, el acceso a datos en DAO y las JSP se usan como vistas.

## Estructura

```text
src/main/
├── java/org/espais/dentista/
│   ├── model/
│   │   ├── Paciente.java
│   │   ├── Odontologo.java
│   │   └── Cita.java
│   ├── dao/
│   │   ├── Database.java
│   │   ├── PacienteDAO.java
│   │   ├── OdontologoDAO.java
│   │   └── CitaDAO.java
│   └── servlet/
│       ├── ListadoPacientesServlet.java
│       ├── ListadoCitasServlet.java
│       ├── FormularioPacienteServlet.java
│       ├── FormularioCitaServlet.java
│       ├── GrabaPacienteServlet.java
│       ├── BorraPacienteServlet.java
│       ├── GrabaCitaServlet.java
│       └── BorraCitaServlet.java
├── resources/
│   └── dentista.sql
└── webapp/
    ├── WEB-INF/views/
    │   ├── listadoPacientes.jsp
    │   ├── listadoCitas.jsp
    │   ├── formularioPaciente.jsp
    │   └── formularioCita.jsp
    ├── index.jsp
    └── estilos.css
```

Las vistas están dentro de `WEB-INF`, por lo que no se pueden solicitar directamente: se accede a ellas mediante los Servlets.

## Rutas de la aplicación

| Ruta | Método | Función |
|---|---|---|
| `/pacientes` | GET | Lista pacientes y el número de citas de cada uno |
| `/pacientes/nuevo` | GET | Muestra el formulario de alta de paciente |
| `/pacientes/guardar` | POST | Registra un paciente |
| `/pacientes/borrar` | POST | Elimina un paciente y sus citas asociadas |
| `/citas` | GET | Lista las próximas citas; `?todas=1` muestra el histórico |
| `/citas/nueva` | GET | Muestra el formulario de cita |
| `/citas/guardar` | POST | Registra una cita |
| `/citas/borrar` | POST | Anula una cita |

La página inicial `index.jsp` redirige al listado de pacientes. Los formularios de borrado envían `POST`.

## Organización del código

- `model`: objetos de dominio `Paciente`, `Odontologo` y `Cita`.
- `dao`: consultas JDBC y operaciones CRUD. `Database` centraliza la conexión.
- `servlet`: recibe las peticiones, invoca los DAO y reenvía a las vistas o redirige tras una operación.
- `WEB-INF/views`: páginas JSP de presentación, usando JSTL para iterar y mostrar datos escapados.

## Base de datos

La base `dentista` usa tres tablas:

| Tabla | Clave | Datos |
|---|---|---|
| `paciente` | `id_paciente` | Datos personales y de contacto |
| `odontologos` | `id_odontologo` | Nombre y especialidad |
| `cita` | `id_cita` | Fecha, hora, paciente, odontólogo y motivo |

`cita.id_paciente` referencia a `paciente.id_paciente` con `ON DELETE CASCADE`. `cita.id_odontologo` referencia a `odontologos.id_odontologo`.

### Preparar MySQL

Importa el script de creación y datos de ejemplo:

```bash
mysql -u user -puser < src/main/resources/dentista.sql
```

Por defecto, `Database.java` conecta a `127.0.0.1:3306`, base `dentista`, con usuario y contraseña `user`. Si cambian la ubicación o las credenciales, actualiza las constantes de ese archivo.

## Compilar y desplegar

Requisitos: Java 21, Maven 3.8+ y **Tomcat 9.0.x**.

El proyecto usa Java EE 8 y el paquete `javax.servlet`; no es compatible directamente con Tomcat 10, que requiere `jakarta.*`.

```bash
mvn clean package
```

Se genera `target/scriplet_dentista.war`. Despliega ese WAR en Tomcat 9; si se conserva el nombre, el contexto será `/scriplet_dentista`.

## Entrega del ejercicio

Para crear el ZIP solicitado, comprime el proyecto excluyendo `target/` y cualquier carpeta `build/`. Incluye también el enlace al repositorio público en la entrega.
