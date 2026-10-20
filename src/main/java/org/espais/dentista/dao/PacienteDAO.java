package org.espais.dentista.dao;

import org.espais.dentista.model.Paciente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {
    public List<Paciente> listar() throws SQLException {
        String sql = "SELECT p.id_paciente, p.nombre, p.apellidos, p.dni, p.fecha_nacimiento, "
                + "p.telefono, p.email, p.direccion, p.alergias, "
                + "(SELECT COUNT(*) FROM cita c WHERE c.id_paciente = p.id_paciente) AS num_citas "
                + "FROM paciente p ORDER BY p.apellidos, p.nombre";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Paciente paciente = new Paciente();
                paciente.setIdPaciente(result.getInt("id_paciente"));
                paciente.setNombre(result.getString("nombre"));
                paciente.setApellidos(result.getString("apellidos"));
                paciente.setDni(result.getString("dni"));
                Date fechaNacimiento = result.getDate("fecha_nacimiento");
                paciente.setFechaNacimiento(fechaNacimiento == null ? null : fechaNacimiento.toLocalDate());
                paciente.setTelefono(result.getString("telefono"));
                paciente.setEmail(result.getString("email"));
                paciente.setDireccion(result.getString("direccion"));
                paciente.setAlergias(result.getString("alergias"));
                paciente.setNumeroCitas(result.getInt("num_citas"));
                pacientes.add(paciente);
            }
        }
        return pacientes;
    }

    public void insertar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente "
                + "(nombre, apellidos, dni, fecha_nacimiento, telefono, email, direccion, alergias) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, paciente.getNombre());
            statement.setString(2, paciente.getApellidos());
            statement.setString(3, paciente.getDni());
            LocalDate fechaNacimiento = paciente.getFechaNacimiento();
            if (fechaNacimiento == null) {
                statement.setNull(4, Types.DATE);
            } else {
                statement.setDate(4, Date.valueOf(fechaNacimiento));
            }
            statement.setString(5, paciente.getTelefono());
            statement.setString(6, paciente.getEmail());
            statement.setString(7, paciente.getDireccion());
            statement.setString(8, paciente.getAlergias());
            statement.executeUpdate();
        }
    }

    public void eliminar(int idPaciente) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM paciente WHERE id_paciente = ?")) {
            statement.setInt(1, idPaciente);
            statement.executeUpdate();
        }
    }
}
