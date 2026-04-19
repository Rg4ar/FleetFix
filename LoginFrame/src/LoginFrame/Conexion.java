package Backend.FleetFix;

import java.sql.*;

/**
 * Clase responsable de la conexión a la base de datos SQLite.
 * También inicializa las tablas y datos por defecto si no existen.
 */
public class Conexion {

    private static final String URL = "jdbc:sqlite:fleetfix.db";

    /**
     * Retorna una conexión activa a la base de datos.
     * Cada llamada abre una nueva conexión e inicializa la BD si es necesario.
     */
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

    /**
     * Crea las tablas si no existen e inserta datos iniciales
     * (usuarios, niveles de prioridad) cuando la BD está vacía.
     */
    private static void inicializarDB(Connection con) throws SQLException {
        Statement st = con.createStatement();

        // Crear tablas si no existen
        st.execute("CREATE TABLE IF NOT EXISTS Usuarios (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre_usuario TEXT NOT NULL UNIQUE, " +
                "contrasena TEXT NOT NULL, " +
                "activo INTEGER NOT NULL DEFAULT 1)");

        st.execute("CREATE TABLE IF NOT EXISTS Niveles (" +
                "id_nivel INTEGER PRIMARY KEY, " +
                "nombre TEXT NOT NULL)");

        st.execute("CREATE TABLE IF NOT EXISTS Vehiculos (" +
                "id_vehiculo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "placa TEXT NOT NULL, " +
                "modelo TEXT NOT NULL, " +
                "estado TEXT NOT NULL DEFAULT 'Operativo')");

        st.execute("CREATE TABLE IF NOT EXISTS Tickets (" +
                "id_ticket INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "vehiculo TEXT NOT NULL, " +
                "id_nivel INTEGER NOT NULL, " +
                "titulo TEXT NOT NULL, " +
                "descripcion TEXT NOT NULL, " +
                "estado TEXT NOT NULL DEFAULT 'Abierto', " +
                "falla_critica INTEGER NOT NULL DEFAULT 0, " +
                "creado_en TEXT NOT NULL DEFAULT (datetime('now')))");

        // Insertar usuarios por defecto si la tabla está vacía
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Usuarios");
        if (rs.next() && rs.getInt(1) == 0) {
            st.execute("INSERT INTO Usuarios (nombre_usuario, contrasena, activo) VALUES ('admin', 'admin123', 1)");
            st.execute("INSERT INTO Usuarios (nombre_usuario, contrasena, activo) VALUES ('raini', 'fleetfix', 1)");
        }

        // Insertar niveles de prioridad si la tabla está vacía
        rs = st.executeQuery("SELECT COUNT(*) FROM Niveles");
        if (rs.next() && rs.getInt(1) == 0) {
            st.execute("INSERT INTO Niveles VALUES (1, 'Bajo')");
            st.execute("INSERT INTO Niveles VALUES (2, 'Medio')");
            st.execute("INSERT INTO Niveles VALUES (3, 'Alto')");
            st.execute("INSERT INTO Niveles VALUES (4, 'Critico')");
        }

        st.close();
    }
}
