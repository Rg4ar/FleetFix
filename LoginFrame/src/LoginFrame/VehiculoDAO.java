package Backend.FleetFix;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    public List<String[]> obtenerTodos() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT id_vehiculo, placa, modelo, estado FROM Vehiculos";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new String[]{
                    String.valueOf(rs.getInt(1)),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener vehiculos: " + e.getMessage());
        }
        return lista;
    }

    public int contarActivos() {
        String sql = "SELECT COUNT(*) FROM Vehiculos WHERE estado = 'Operativo'";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error al contar vehiculos: " + e.getMessage());
        }
        return 0;
    }
}
