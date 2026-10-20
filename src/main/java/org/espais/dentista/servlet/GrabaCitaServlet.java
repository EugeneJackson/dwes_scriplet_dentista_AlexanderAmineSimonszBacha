package org.espais.dentista.servlet;

import org.espais.dentista.dao.CitaDAO;
import org.espais.dentista.model.Cita;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@WebServlet("/citas/guardar")
public class GrabaCitaServlet extends HttpServlet {
    private final CitaDAO citaDAO = new CitaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            Cita cita = new Cita();
            cita.setFecha(LocalDate.parse(valor(request, "fecha")));
            cita.setHora(LocalTime.parse(valor(request, "hora")));
            cita.setIdPaciente(Integer.parseInt(valor(request, "id_paciente")));
            cita.setIdOdontologo(Integer.parseInt(valor(request, "id_odontologo")));
            cita.setMotivo(valor(request, "motivo"));
            if (cita.getIdPaciente() <= 0 || cita.getIdOdontologo() <= 0) {
                throw new NumberFormatException("Los identificadores deben ser positivos.");
            }

            citaDAO.insertar(cita);
            response.sendRedirect(request.getContextPath() + "/citas?resultado=guardado");
        } catch (DateTimeParseException | NumberFormatException e) {
            mostrarFormulario(request, response, "Revisa la fecha, la hora, el paciente y el odontólogo seleccionados.");
        } catch (SQLException e) {
            getServletContext().log("No se pudo guardar la cita.", e);
            mostrarFormulario(request, response, "No se ha podido registrar la cita.");
        }
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, String error)
            throws ServletException, IOException {
        request.setAttribute("error", error);
        try {
            FormularioCitaServlet.prepararFormulario(request);
        } catch (SQLException e) {
            getServletContext().log("No se pudieron recargar los datos del formulario de cita.", e);
            request.setAttribute("error", "No se han podido cargar pacientes y odontólogos.");
        }
        request.getRequestDispatcher("/WEB-INF/views/formularioCita.jsp").forward(request, response);
    }

    private static String valor(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor == null ? "" : valor.trim();
    }
}
