package org.espais.dentista.servlet;

import org.espais.dentista.dao.OdontologoDAO;
import org.espais.dentista.dao.PacienteDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/citas/nueva")
public class FormularioCitaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            prepararFormulario(request);
        } catch (SQLException e) {
            getServletContext().log("No se pudieron cargar los datos del formulario de cita.", e);
            request.setAttribute("error", "No se han podido cargar pacientes y odontólogos.");
        }
        request.getRequestDispatcher("/WEB-INF/views/formularioCita.jsp").forward(request, response);
    }

    static void prepararFormulario(HttpServletRequest request) throws SQLException {
        request.setAttribute("pacientes", new PacienteDAO().listar());
        request.setAttribute("odontologos", new OdontologoDAO().listar());
        String pacienteSeleccionado = request.getParameter("paciente");
        if (pacienteSeleccionado == null) {
            pacienteSeleccionado = request.getParameter("id_paciente");
        }
        request.setAttribute("pacienteSeleccionado", pacienteSeleccionado);
    }
}
