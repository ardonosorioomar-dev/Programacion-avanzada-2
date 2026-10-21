package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Hola_Mundo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conexion = ConnectionDB.getConnection();

        if (conexion == null) {
            System.out.println("No se puede establecer la conexión");
            return;
        }

        int opcion = 0;
        do {
            System.out.println("\n=== MENU  ===");
            System.out.println("1. Ingresar Nuevo");
            System.out.println("2. Todos los Alumnos");
            System.out.println("3. Actualizar Registro");
            System.out.println("4. Eliminar");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 1:
                    insertarAlumno(conexion, scanner);
                    break;
                case 2:
                    listarAlumnos(conexion);
                    break;
                case 3:
                    actualizarAlumno(conexion, scanner);
                    break;
                case 4:
                    eliminarAlumno(conexion, scanner);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 5);

        try {
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scanner.close();
    }

    private static void insertarAlumno(Connection conexion, Scanner scanner) {
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el correo: ");
        String correo = scanner.nextLine();

        String sql = "INSERT INTO Alumnos (nombre, correo) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, correo);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("¡Alumno Ingresado con éxito!");
            } else {
                System.out.println("No se Ingreso ningún registro.");
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
        }
    }

    private static void listarAlumnos(Connection conexion) {
        String sql = "SELECT id, nombre, correo FROM Alumnos";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n=====================================================");
            System.out.println("ID  | Nombre           | Correo");
            System.out.println("=====================================================");
            while (rs.next()) {
                System.out.printf("%-3d  | %-16s | %s%n",
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"));
            }
            System.out.println("=====================================================");

        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
        }
    }

    private static void actualizarAlumno(Connection conexion, Scanner scanner) {
        System.out.print("Ingrese el ID del alumno a actualizar: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        if (!existeAlumno(conexion, id)) {
            System.out.println("No se encontró ningún alumno con ese ID.");
            return;
        }

        System.out.println("\n¿Qué campo desea actualizar?");
        System.out.println("1. Nombre");
        System.out.println("2. Correo");
        System.out.println("3. Todos los campos");
        System.out.print("Seleccione una opción: ");

        int opcion;
        try {
            opcion = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida.");
            return;
        }

        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT nombre, correo FROM Alumnos WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return;

                String nombreActual = rs.getString("nombre");
                String correoActual = rs.getString("correo");

                switch (opcion) {
                    case 1:
                        System.out.print("Nuevo nombre (" + nombreActual + "): ");
                        String nuevoNombre = scanner.nextLine();
                        if (nuevoNombre.isEmpty()) nuevoNombre = nombreActual;
                        try (PreparedStatement psUpd = conexion.prepareStatement(
                                "UPDATE Alumnos SET nombre = ? WHERE id = ?")) {
                            psUpd.setString(1, nuevoNombre);
                            psUpd.setInt(2, id);
                            ejecutarUpdate(psUpd);
                        }
                        break;

                    case 2:
                        System.out.print("Nuevo correo (" + correoActual + "): ");
                        String nuevoCorreo = scanner.nextLine();
                        if (nuevoCorreo.isEmpty()) nuevoCorreo = correoActual;
                        try (PreparedStatement psUpd = conexion.prepareStatement(
                                "UPDATE Alumnos SET correo = ? WHERE id = ?")) {
                            psUpd.setString(1, nuevoCorreo);
                            psUpd.setInt(2, id);
                            ejecutarUpdate(psUpd);
                        }
                        break;

                    case 3:
                        System.out.print("Nuevo nombre (" + nombreActual + "): ");
                        String nNombre = scanner.nextLine();
                        if (nNombre.isEmpty()) nNombre = nombreActual;

                        System.out.print("Nuevo correo (" + correoActual + "): ");
                        String nCorreo = scanner.nextLine();
                        if (nCorreo.isEmpty()) nCorreo = correoActual;

                        try (PreparedStatement psUpd = conexion.prepareStatement(
                                "UPDATE Alumnos SET nombre = ?, correo = ? WHERE id = ?")) {
                            psUpd.setString(1, nNombre);
                            psUpd.setString(2, nCorreo);
                            psUpd.setInt(3, id);
                            ejecutarUpdate(psUpd);
                        }
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
        }
    }

    private static void ejecutarUpdate(PreparedStatement ps) throws SQLException {
        int filas = ps.executeUpdate();
        if (filas > 0) {
            System.out.println("¡Alumno actualizado con éxito! Filas afectadas: " + filas);
        } else {
            System.out.println("No se pudo actualizar el alumno.");
        }
    }

    private static boolean existeAlumno(Connection conexion, int id) {
        String sql = "SELECT 1 FROM Alumnos WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            return false;
        }
    }

    private static void eliminarAlumno(Connection conexion, Scanner scanner) {
        System.out.print("Ingrese el ID del alumno a eliminar: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        String sql = "DELETE FROM Alumnos WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("¡Alumno eliminado con éxito! Filas afectadas: " + filas);
            } else {
                System.out.println("No se encontró el alumno a eliminar.");
            }
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
        }
    }
}