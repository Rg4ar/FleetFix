package Backend.FleetFix;

import java.sql.*;

/**
 * DAO de usuarios. Maneja operaciones relacionadas con autenticación.
 */
public class UsuarioDAO {

    /**
     * Valida las credenciales del usuario contra la base de datos.
     * Solo retorna true si el usuario existe, la contraseña es correcta y está activo.
     */
    public boolean validarLogin(String username, String password) {
        String sql = "SELECT * FROM Usuarios WHERE nombre_usuario = ? AND contrasena = ? AND activo = 1";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true si existe al menos un resultado
        } catch (SQLException e) {
            System.err.println("Error al validar login: " + e.getMessage());
            return false;
        }
    }
}
