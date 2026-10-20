package org.espais.dentista.servlet;

import org.espais.dentista.dao.PacienteDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/pacientes/borrar")
public class BorraPacienteServlet extends HttpServlet {
    private final PacienteDAO pacienteDAO = new PacienteDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int idPaciente = Integer.parseInt(request.getParameter("id_paciente"));
            if (idPaciente <= 0) {
                throw new NumberFormatException("El identificador debe ser positivo.");
            }
            pacienteDAO.eliminar(idPaciente);
            response.sendRedirect(request.getContextPath() + "/pacientes?resultado=eliminado");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/pacientes?resultado=error");
        } catch (SQLException e) {
            getServletContext().log("No se pudo eliminar el paciente.", e);
            response.sendRedirect(request.getContextPath() + "/pacientes?resultado=error");
        }
    }
}
