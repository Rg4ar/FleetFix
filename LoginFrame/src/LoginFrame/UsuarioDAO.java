package Backend.FleetFix;

import java.sql.*;

public class UsuarioDAO {
    public boolean validarLogin(String username, String password) {
        String sql = "SELECT * FROM Usuarios WHERE nombre_usuario = ? AND contrasena = ? AND activo = 1";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al validar login: " + e.getMessage());
            return false;
        }
    }
}
