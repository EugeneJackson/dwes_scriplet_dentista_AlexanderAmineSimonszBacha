package org.espais.dentista.servlet;

import org.espais.dentista.dao.PacienteDAO;
import org.espais.dentista.model.Paciente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/pacientes/guardar")
public class GrabaPacienteServlet extends HttpServlet {
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String nombre = valor(request, "nombre");
        String apellidos = valor(request, "apellidos");
        String dni = valor(request, "dni");
        String nacimiento = valor(request, "fecha_nacimiento");
        if (nombre.isBlank() || apellidos.isBlank() || dni.isBlank()) {
            request.setAttribute("error", "Nombre, apellidos y DNI son obligatorios.");
            request.getRequestDispatcher("/WEB-INF/views/formularioPaciente.jsp").forward(request, response);
            return;
        }

        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellidos(apellidos);
        paciente.setDni(dni);
        paciente.setTelefono(valor(request, "telefono"));
        paciente.setEmail(valor(request, "email"));
        paciente.setDireccion(valor(request, "direccion"));
        paciente.setAlergias(valor(request, "alergias"));

        try {
            paciente.setFechaNacimiento(nacimiento.isBlank() ? null : LocalDate.parse(nacimiento));
            pacienteDAO.insertar(paciente);
            response.sendRedirect(request.getContextPath() + "/pacientes?resultado=guardado");
        } catch (DateTimeParseException e) {
            request.setAttribute("error", "La fecha de nacimiento no es válida.");
            request.getRequestDispatcher("/WEB-INF/views/formularioPaciente.jsp").forward(request, response);
        } catch (SQLException e) {
            getServletContext().log("No se pudo guardar el paciente.", e);
            request.setAttribute("error", "No se ha podido registrar el paciente. Comprueba que el DNI no esté repetido.");
            request.getRequestDispatcher("/WEB-INF/views/formularioPaciente.jsp").forward(request, response);
        }
    }

    private static String valor(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor == null ? "" : valor.trim();
    }
}
