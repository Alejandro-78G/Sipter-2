package Controlador;

import Modelo.ConexionSql;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ControladorMenuConductor {

    @FXML
    private void confirmarSalida() {
        registrarEvento("salida");
    }

    @FXML
    private void confirmarLlegada() {
        registrarEvento("llegada");
    }

    @FXML
    private void reportarInasistencia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/reportar_inasistencia.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Reportar Inasistencia");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarError("Error al abrir la ventana de inasistencia: " + e.getMessage());
        }
    }

    @FXML
    private void verPasajeros() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/ver_pasajeros.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Lista de Pasajeros");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            mostrarError("Error al abrir la ventana de pasajeros: " + e.getMessage());
        }
    }

    @FXML
    private void cerrarSesion() {
        try {
            Stage stageActual = (Stage) getWindow();
            stageActual.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/login.fxml"));
            Parent root = loader.load();
            Stage stageLogin = new Stage();
            stageLogin.setTitle("Iniciar Sesión");
            stageLogin.setScene(new Scene(root));
            stageLogin.show();
        } catch (Exception e) {
            mostrarError("Error al cerrar sesión: " + e.getMessage());
        }
    }

    private void registrarEvento(String tipo) {
        try (Connection conn = ConexionSql.getConexion()) {
            if (conn == null) {
                mostrarError("No se pudo conectar a la base de datos.");
                return;
            }
            String sql = "INSERT INTO registro_viajes (tipo_evento, fecha_hora) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tipo);
            ps.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.executeUpdate();
            mostrarMensaje("Éxito", "Evento de " + tipo + " confirmado correctamente.");
        } catch (Exception e) {
            mostrarError("Error al registrar " + tipo + ": " + e.getMessage());
        }
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private Window getWindow() {
        return javafx.stage.Window.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
    }
}

