package org.espais.dentista.servlet;

import org.espais.dentista.dao.PacienteDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/pacientes")
public class ListadoPacientesServlet extends HttpServlet {
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("pacientes", pacienteDAO.listar());
            String resultado = request.getParameter("resultado");
            if ("guardado".equals(resultado)) {
                request.setAttribute("mensaje", "Paciente dado de alta.");
            } else if ("eliminado".equals(resultado)) {
                request.setAttribute("mensaje", "Paciente eliminado junto con sus citas asociadas.");
            } else if ("error".equals(resultado)) {
                request.setAttribute("error", "No se ha podido eliminar el paciente.");
            }
        } catch (SQLException e) {
            getServletContext().log("No se pudo cargar el listado de pacientes.", e);
            request.setAttribute("error", "No se ha podido obtener el listado de pacientes.");
        }
        request.getRequestDispatcher("/WEB-INF/views/listadoPacientes.jsp").forward(request, response);
    }
}
