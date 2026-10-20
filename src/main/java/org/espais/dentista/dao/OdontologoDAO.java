package org.espais.dentista.dao;

import org.espais.dentista.model.Odontologo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDAO {
    public List<Odontologo> listar() throws SQLException {
        List<Odontologo> odontologos = new ArrayList<>();
        String sql = "SELECT id_odontologo, nombre, especialidad "
                + "FROM odontologos ORDER BY nombre";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Odontologo odontologo = new Odontologo();
                odontologo.setIdOdontologo(result.getInt("id_odontologo"));
                odontologo.setNombre(result.getString("nombre"));
                odontologo.setEspecialidad(result.getString("especialidad"));
                odontologos.add(odontologo);
            }
        }
        return odontologos;
    }
}
