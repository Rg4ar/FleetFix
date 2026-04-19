package Backend.FleetFix;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 * DAO de tickets. Maneja todas las operaciones CRUD sobre la tabla Tickets.
 */
public class TicketDAO {

    /**
     * Inserta un nuevo ticket en la base de datos.
     * Si el nivel es 4 (Crítico), marca automáticamente falla_critica = 1.
     */
    public boolean crearTicket(String vehiculo, int idNivel, String titulo, String descripcion) {
        boolean esCritica = (idNivel == 4);
        String sql = "INSERT INTO Tickets (vehiculo, id_nivel, titulo, descripcion, estado, falla_critica, creado_en) " +
                     "VALUES (?, ?, ?, ?, 'Abierto', ?, datetime('now'))";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehiculo);
            ps.setInt(2, idNivel);
            ps.setString(3, titulo);
            ps.setString(4, descripcion);
            ps.setInt(5, esCritica ? 1 : 0);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar ticket: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza el estado de un ticket existente.
     * Estados válidos: Abierto, En progreso, Resuelto, Cerrado.
     */
    public boolean cambiarEstado(int idTicket, String nuevoEstado) {
        String sql = "UPDATE Tickets SET estado = ? WHERE id_ticket = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idTicket);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retorna todos los tickets en un modelo de tabla listo para JTable.
     * Columnas: ID, Titulo, Vehiculo, Nivel, Estado, Critico, Fecha.
     */
    public DefaultTableModel obtenerTodos() {
        String[] columnas = {"ID", "Titulo", "Vehiculo", "Nivel", "Estado", "Critico", "Fecha"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        String sql = "SELECT t.id_ticket, t.titulo, t.vehiculo, n.nombre, " +
                     "t.estado, t.falla_critica, t.creado_en " +
                     "FROM Tickets t " +
                     "JOIN Niveles n ON t.id_nivel = n.id_nivel " +
                     "ORDER BY t.id_ticket DESC";

        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getInt(6) == 1 ? "Si" : "No", // falla_critica como texto legible
                    rs.getString(7).substring(0, 10)  // solo la fecha (sin hora)
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener tickets: " + e.getMessage());
        }
        return model;
    }

    /**
     * Cuenta los tickets que tienen un estado específico.
     */
    public int contarPorEstado(String estado) {
        String sql = "SELECT COUNT(*) FROM Tickets WHERE estado = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error al contar tickets: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Cuenta cuántos tickets están marcados como falla crítica.
     */
    public int contarCriticos() {
        String sql = "SELECT COUNT(*) FROM Tickets WHERE falla_critica = 1";
        try (Connection con = Conexion.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error al contar criticos: " + e.getMessage());
        }
        return 0;
    }
}
