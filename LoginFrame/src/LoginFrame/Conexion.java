package Backend.FleetFix;

import java.sql.*;

public class Conexion {
    private static final String URL = "jdbc:sqlite:fleetfix.db";

    public static Connection getConexion() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("DB ubicada en: " + new java.io.File("fleetfix.db").getAbsolutePath());
            Connection con = DriverManager.getConnection(URL);
            inicializarDB(con);
            return con;
        } catch (SQLException e) {
            System.err.println("Error de conexión SQLite: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite no encontrado: " + e.getMessage());
            return null;
        }
    }

    private static void inicializarDB(Connection con) throws SQLException {
        Statement st = con.createStatement();
        st.execute("CREATE TABLE IF NOT EXISTS Usuarios (id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, nombre_usuario TEXT NOT NULL UNIQUE, contrasena TEXT NOT NULL, activo INTEGER NOT NULL DEFAULT 1)");
        st.execute("CREATE TABLE IF NOT EXISTS Niveles (id_nivel INTEGER PRIMARY KEY, nombre TEXT NOT NULL)");
        st.execute("CREATE TABLE IF NOT EXISTS Vehiculos (id_vehiculo INTEGER PRIMARY KEY AUTOINCREMENT, placa TEXT NOT NULL, modelo TEXT NOT NULL, estado TEXT NOT NULL DEFAULT 'Operativo')");
        st.execute("CREATE TABLE IF NOT EXISTS Tickets (id_ticket INTEGER PRIMARY KEY AUTOINCREMENT, vehiculo TEXT NOT NULL, id_nivel INTEGER NOT NULL, titulo TEXT NOT NULL, descripcion TEXT NOT NULL, estado TEXT NOT NULL DEFAULT 'Abierto', falla_critica INTEGER NOT NULL DEFAULT 0, creado_en TEXT NOT NULL DEFAULT (datetime('now')))");
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Usuarios");
        if (rs.next() && rs.getInt(1) == 0) {
            st.execute("INSERT INTO Usuarios (nombre_usuario, contrasena, activo) VALUES ('admin', 'admin123', 1)");
            st.execute("INSERT INTO Usuarios (nombre_usuario, contrasena, activo) VALUES ('raini', 'fleetfix', 1)");
        }
        rs = st.executeQuery("SELECT COUNT(*) FROM Niveles");
        if (rs.next() && rs.getInt(1) == 0) {
            st.execute("INSERT INTO Niveles VALUES (1, 'Bajo')");
            st.execute("INSERT INTO Niveles VALUES (2, 'Medio')");
            st.execute("INSERT INTO Niveles VALUES (3, 'Alto')");
            st.execute("INSERT INTO Niveles VALUES (4, 'Critico')");
        }
        rs = st.executeQuery("SELECT COUNT(*) FROM Vehiculos");
        if (rs.next() && rs.getInt(1) == 0) {
            
        }
        st.close();
    }
}