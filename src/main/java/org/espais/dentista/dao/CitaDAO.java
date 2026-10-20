package org.espais.dentista.dao;

import org.espais.dentista.model.Cita;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {
    public List<Cita> listar(boolean todas) throws SQLException {
        String sql = "SELECT c.id_cita, c.fecha, c.hora, c.motivo, "
                + "p.id_paciente, p.nombre AS nombre_paciente, p.apellidos, "
                + "o.id_odontologo, o.nombre AS nombre_odontologo "
                + "FROM cita c "
                + "JOIN paciente p ON p.id_paciente = c.id_paciente "
                + "JOIN odontologos o ON o.id_odontologo = c.id_odontologo "
                + (todas ? "" : "WHERE c.fecha >= CURDATE() ")
                + "ORDER BY c.fecha, c.hora";
        List<Cita> citas = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Cita cita = new Cita();
                cita.setIdCita(result.getInt("id_cita"));
                cita.setFecha(result.getDate("fecha").toLocalDate());
                cita.setHora(result.getTime("hora").toLocalTime());
                cita.setIdPaciente(result.getInt("id_paciente"));
                cita.setIdOdontologo(result.getInt("id_odontologo"));
                cita.setMotivo(result.getString("motivo"));
                cita.setNombrePaciente(result.getString("nombre_paciente"));
                cita.setApellidosPaciente(result.getString("apellidos"));
                cita.setNombreOdontologo(result.getString("nombre_odontologo"));
                citas.add(cita);
            }
        }
        return citas;
    }

    public void insertar(Cita cita) throws SQLException {
        String sql = "INSERT INTO cita (fecha, hora, id_paciente, id_odontologo, motivo) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(cita.getFecha()));
            statement.setTime(2, Time.valueOf(cita.getHora()));
            statement.setInt(3, cita.getIdPaciente());
            statement.setInt(4, cita.getIdOdontologo());
            statement.setString(5, cita.getMotivo());
            statement.executeUpdate();
        }
    }

    public void eliminar(int idCita) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM cita WHERE id_cita = ?")) {
            statement.setInt(1, idCita);
            statement.executeUpdate();
        }
    }
}
