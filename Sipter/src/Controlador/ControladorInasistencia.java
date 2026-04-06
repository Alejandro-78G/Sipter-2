package Controlador;

import Modelo.ConexionSql;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ControladorInasistencia {

    @FXML
    private TextArea txtMotivo;

    @FXML
    private void reportarInasistencia() {
        String motivo = txtMotivo.getText().trim();

        if (motivo.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar un motivo de inasistencia.", Alert.AlertType.ERROR);
            return;
        }

        String sql = "INSERT INTO inasistencias (motivo, fecha_hora) VALUES (?, ?)";
        try (Connection conn = ConexionSql.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, motivo);
            ps.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.executeUpdate();

            mostrarAlerta("Éxito", "Inasistencia reportada correctamente.", Alert.AlertType.INFORMATION);
            cerrarVentana();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo registrar la inasistencia: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtMotivo.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
