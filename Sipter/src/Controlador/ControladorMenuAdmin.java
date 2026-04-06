package Controlador;

import Modelo.ConexionSql;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ControladorMenuAdmin {

    @FXML
    private void generarReporteViajes(ActionEvent event) {
        generarReporteExcel(
                "Reporte_Viajes.xlsx",
                "SELECT v.id AS id_viaje, c.nombre AS conductor, d.ciudad AS destino, " +
                        "v.fecha_salida, v.fecha_llegada, COUNT(p.id) AS pasajeros " +
                        "FROM viajes_conductores v " +
                        "JOIN conductores c ON v.id_conductor = c.id " +
                        "LEFT JOIN destinos d ON d.id = v.id_destino " +
                        "LEFT JOIN viaje_pasajeros p ON v.id = p.id_viaje " +
                        "GROUP BY v.id, c.nombre, d.ciudad, v.fecha_salida, v.fecha_llegada",
                new String[]{"ID Viaje", "Conductor", "Destino", "Fecha Salida", "Fecha Llegada", "Pasajeros"}
        );
    }

    @FXML
    private void generarReporteConductores(ActionEvent event) {
        generarReporteExcel(
                "Reporte_Conductores.xlsx",
                "SELECT c.id AS id_conductor, c.nombre, c.licencia, " +
                        "COUNT(v.id) AS cantidad_viajes, c.activo " +
                        "FROM conductores c " +
                        "LEFT JOIN viajes_conductores v ON c.id = v.id_conductor " +
                        "GROUP BY c.id, c.nombre, c.licencia, c.activo",
                new String[]{"ID Conductor", "Nombre", "Licencia", "Cantidad Viajes", "Activo"}
        );
    }

    @FXML
    private void inhabilitarUsuarios(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Inhabilitar Usuario");
        dialog.setHeaderText("Desactivar usuario");
        dialog.setContentText("Ingrese el nombre de usuario a inhabilitar:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombre -> {
            try (Connection conn = ConexionSql.getConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE usuarios SET activo = FALSE WHERE nombre_usuario = ?")) {
                ps.setString(1, nombre);
                int filas = ps.executeUpdate();
                mostrarAlerta("Resultado",
                        filas > 0 ? "Usuario inhabilitado correctamente." :
                                "No se encontró el usuario especificado.");
            } catch (SQLException e) {
                mostrarAlerta("Error SQL", e.getMessage());
            }
        });
    }

    @FXML
    private void inhabilitarConductores(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Inhabilitar Conductor");
        dialog.setHeaderText("Desactivar conductor");
        dialog.setContentText("Ingrese el ID numérico del conductor a inhabilitar:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(id -> {
            if (!id.matches("\\d+")) {
                mostrarAlerta("Error", "El ID debe ser un número entero.");
                return;
            }

            try (Connection conn = ConexionSql.getConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE conductores SET activo = FALSE WHERE id = ?")) {
                ps.setInt(1, Integer.parseInt(id));
                int filas = ps.executeUpdate();
                mostrarAlerta("Resultado",
                        filas > 0 ? "Conductor inhabilitado correctamente." :
                                "No se encontró el conductor especificado.");
            } catch (SQLException e) {
                mostrarAlerta("Error SQL", e.getMessage());
            }
        });
    }

    @FXML
    private void actualizarTarifas(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Actualizar Tarifas");
        dialog.setHeaderText("Modificar tarifa de una ruta");
        dialog.setContentText("Ingrese el ID de la ruta a actualizar:");
        Optional<String> idRuta = dialog.showAndWait();
        idRuta.ifPresent(id -> {
            TextInputDialog dialog2 = new TextInputDialog();
            dialog2.setTitle("Nueva Tarifa");
            dialog2.setHeaderText("Ingrese la nueva tarifa:");
            Optional<String> nuevaTarifa = dialog2.showAndWait();
            nuevaTarifa.ifPresent(tarifa -> {
                try (Connection conn = ConexionSql.getConexion();
                     PreparedStatement ps = conn.prepareStatement(
                             "UPDATE rutas SET tarifa = ? WHERE id = ?")) {
                    ps.setDouble(1, Double.parseDouble(tarifa));
                    ps.setInt(2, Integer.parseInt(id));
                    int filas = ps.executeUpdate();
                    mostrarAlerta("Resultado",
                            filas > 0 ? "Tarifa actualizada correctamente." :
                                    "No se encontró la ruta especificada.");
                } catch (SQLException e) {
                    mostrarAlerta("Error SQL", e.getMessage());
                }
            });
        });
    }

    @FXML
    private void eliminarRutas(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Eliminar Ruta");
        dialog.setHeaderText("Eliminar ruta del sistema");
        dialog.setContentText("Ingrese el ID de la ruta a eliminar:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(id -> {
            try (Connection conn = ConexionSql.getConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "DELETE FROM rutas WHERE id = ?")) {
                ps.setInt(1, Integer.parseInt(id));
                int filas = ps.executeUpdate();
                mostrarAlerta("Resultado",
                        filas > 0 ? "Ruta eliminada correctamente." :
                                "No se encontró la ruta especificada.");
            } catch (SQLException e) {
                mostrarAlerta("Error SQL", e.getMessage());
            }
        });
    }

    @FXML
    private void gestionarRutas(ActionEvent event) {
        abrirVentana("Gestión de Rutas", "/vista/GestionarRutas.fxml");
    }

    @FXML
    private void gestionarVehiculos(ActionEvent event) {
        abrirVentana("Gestión de Vehículos", "/vista/GestionarVehiculos.fxml");
    }

    @FXML
    private void gestionarDestinos(ActionEvent event) {
        abrirVentana("Gestión de Destinos", "/vista/GestionarDestinos.fxml");
    }

    @FXML
    private void gestionarEncuentros(ActionEvent event) {
        abrirVentana("Gestión de Lugares de Encuentro", "/vista/GestionarEncuentros.fxml");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        mostrarAlerta("Sesión cerrada", "El administrador ha cerrado sesión.");
        System.exit(0);
    }

    private void abrirVentana(String titulo, String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana: " + e.getMessage());
        }
    }

    private void generarReporteExcel(String nombreArchivo, String query, String[] columnas) {
        try (Connection conn = ConexionSql.getConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery();
             Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte");
            Row header = sheet.createRow(0);
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
            }
            int fila = 1;
            while (rs.next()) {
                Row row = sheet.createRow(fila++);
                for (int i = 0; i < columnas.length; i++) {
                    row.createCell(i).setCellValue(rs.getString(i + 1));
                }
            }
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }
            File carpeta = new File("C:\\Reportes");
            if (!carpeta.exists()) carpeta.mkdirs();
            File archivo = new File(carpeta, nombreArchivo);
            try (FileOutputStream fileOut = new FileOutputStream(archivo)) {
                workbook.write(fileOut);
            }
            mostrarAlerta("Reporte generado", "El archivo se ha guardado en:\n" + archivo.getAbsolutePath());
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "No se pudo generar el reporte: " + e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
