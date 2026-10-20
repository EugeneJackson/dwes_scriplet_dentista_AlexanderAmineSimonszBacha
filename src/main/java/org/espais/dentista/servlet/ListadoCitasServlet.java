package org.espais.dentista.servlet;

import org.espais.dentista.dao.CitaDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/citas")
public class ListadoCitasServlet extends HttpServlet {
    private final CitaDAO citaDAO = new CitaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean todas = "1".equals(request.getParameter("todas"));
        request.setAttribute("todas", todas);
        try {
            request.setAttribute("citas", citaDAO.listar(todas));
            String resultado = request.getParameter("resultado");
            if ("guardado".equals(resultado)) {
                request.setAttribute("mensaje", "Cita registrada.");
            } else if ("anulada".equals(resultado)) {
                request.setAttribute("mensaje", "Cita anulada.");
            } else if ("error".equals(resultado)) {
                request.setAttribute("error", "No se ha podido anular la cita.");
            }
        } catch (SQLException e) {
            getServletContext().log("No se pudo cargar el listado de citas.", e);
            request.setAttribute("error", "No se ha podido obtener el listado de citas.");
        }
        request.getRequestDispatcher("/WEB-INF/views/listadoCitas.jsp").forward(request, response);
    }
}
