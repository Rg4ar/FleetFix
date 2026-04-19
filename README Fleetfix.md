# FleetFix

Sistema de gestión de flota vehicular desarrollado en Java. Permite registrar y dar seguimiento a tickets de mantenimiento y fallas de vehículos de una empresa.

---

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 11+ | Lenguaje principal |
| Java Swing | Interfaz gráfica de escritorio |
| JDBC | Conexión a base de datos |
| SQLite | Base de datos local embebida |
| sqlite-jdbc 3.36.0.3 | Driver JDBC para SQLite |
| Apache NetBeans 17+ | IDE de desarrollo |

---

## Requisitos previos

- JDK 11 o superior
- Apache NetBeans IDE 17+
- Archivo `sqlite-jdbc-3.36.0.3.jar` — descargar en:
  https://github.com/xerial/sqlite-jdbc/releases/tag/3.36.0.3

---

## Instalación y ejecución

1. Clonar o descomprimir el proyecto en tu máquina.

2. Abrir NetBeans → `File` → `Open Project` → seleccionar la carpeta del proyecto.

3. Agregar el driver SQLite:
   - Click derecho en el proyecto → `Properties` → `Libraries`
   - `Add JAR/Folder` → seleccionar `sqlite-jdbc-3.36.0.3.jar`

4. Configurar la clase principal:
   - Click derecho en el proyecto → `Properties` → `Run`
   - Main Class: `Frontend.FleetFix.LoginFrame`

5. Ejecutar con **F6**.

> La base de datos `fleetfix.db` se crea automáticamente en la carpeta del proyecto al primer arranque. No es necesario configurar nada manualmente.

---

## Credenciales de prueba

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | admin123 | Administrador |
| raini | fleetfix | Usuario |

---

## Estructura del proyecto

```
src/
├── Backend/
│   └── FleetFix/
│       ├── Conexion.java       # Conexión SQLite + creación de tablas
│       ├── UsuarioDAO.java     # Autenticación de usuarios
│       ├── TicketDAO.java      # CRUD de tickets
│       └── VehiculoDAO.java    # Consulta de vehículos
└── Frontend/
    └── FleetFix/
        ├── LoginFrame.java          # Pantalla de login
        ├── DashboardFrame.java      # Ventana principal con sidebar
        ├── NavButton.java           # Componente de navegación
        ├── Tema.java                # Colores y fuentes globales
        ├── PaginaDashboard.java     # Vista de métricas
        ├── PaginaTickets.java       # Lista de tickets
        ├── PaginaNuevoTicket.java   # Formulario de nuevo ticket
        └── PaginaVehiculos.java     # Vista de flota
```

---

## Base de datos

SQLite — archivo local `fleetfix.db` generado automáticamente.

### Tablas

**Usuarios**
| Columna | Tipo | Descripción |
|---|---|---|
| id_usuario | INTEGER PK | Identificador único |
| nombre_usuario | TEXT | Nombre de login |
| contrasena | TEXT | Contraseña |
| activo | INTEGER | 1 = activo, 0 = inactivo |

**Niveles**
| Columna | Tipo | Descripción |
|---|---|---|
| id_nivel | INTEGER PK | 1=Bajo, 2=Medio, 3=Alto, 4=Critico |
| nombre | TEXT | Nombre del nivel |

**Vehiculos**
| Columna | Tipo | Descripción |
|---|---|---|
| id_vehiculo | INTEGER PK | Identificador único |
| placa | TEXT | Número de placa |
| modelo | TEXT | Marca y modelo |
| estado | TEXT | Operativo / Revision |

**Tickets**
| Columna | Tipo | Descripción |
|---|---|---|
| id_ticket | INTEGER PK | Identificador único |
| vehiculo | TEXT | Tipo de vehículo (SUV, Pickup, etc.) |
| id_nivel | INTEGER FK | Nivel de prioridad |
| titulo | TEXT | Título de la falla |
| descripcion | TEXT | Descripción detallada |
| estado | TEXT | Abierto / En progreso / Resuelto / Cerrado |
| falla_critica | INTEGER | 1 si nivel = 4 (Critico) |
| creado_en | TEXT | Fecha y hora de creación |

---

## Funcionalidades

- Login con autenticación contra base de datos
- Dashboard con métricas en tiempo real
- Registro de tickets con tipo de vehículo, nivel de prioridad, título y descripción
- Cambio de estado de tickets (solo administrador)
- Vista de flota con íconos por tipo de vehículo

---

## Niveles de acceso

| Funcionalidad | Usuario | Administrador |
|---|---|---|
| Ver dashboard | ✓ | ✓ |
| Ver tickets | ✓ | ✓ |
| Crear tickets | ✓ | ✓ |
| Cambiar estado de ticket | ✗ | ✓ |
| Ver vehículos | ✓ | ✓ |

---

## Notas

- El script `fleetfix_database.sql` contiene la estructura completa de la base de datos con datos iniciales, útil como referencia o para recrear la BD manualmente.
- Para resetear la base de datos, borra el archivo `fleetfix.db` de la carpeta del proyecto y vuelve a ejecutar el programa.
