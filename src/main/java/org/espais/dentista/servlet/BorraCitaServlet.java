package org.espais.dentista.servlet;

import org.espais.dentista.dao.CitaDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/citas/borrar")
public class BorraCitaServlet extends HttpServlet {
    private final CitaDAO citaDAO = new CitaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int idCita = Integer.parseInt(request.getParameter("id_cita"));
            if (idCita <= 0) {
                throw new NumberFormatException("El identificador debe ser positivo.");
            }
            citaDAO.eliminar(idCita);
            response.sendRedirect(request.getContextPath() + "/citas?resultado=anulada");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/citas?resultado=error");
        } catch (SQLException e) {
            getServletContext().log("No se pudo anular la cita.", e);
            response.sendRedirect(request.getContextPath() + "/citas?resultado=error");
        }
    }
}
