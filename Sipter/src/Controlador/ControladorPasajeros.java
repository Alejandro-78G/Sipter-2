package Controlador;

import Modelo.ConexionSql;
import Modelo.Pasajero;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ControladorPasajeros {

    @FXML private TableView<Pasajero> tablaPasajeros;
    @FXML private TableColumn<Pasajero, Integer> colId;
    @FXML private TableColumn<Pasajero, String> colNombre;
    @FXML private TableColumn<Pasajero, String> colApellido;
    @FXML private TableColumn<Pasajero, String> colCedula;
    @FXML private TableColumn<Pasajero, String> colTelefono;
    @FXML private TableColumn<Pasajero, String> colDestino;

    private final ObservableList<Pasajero> listaPasajeros = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colNombre.setCellValueFactory(data -> data.getValue().nombreProperty());
        colApellido.setCellValueFactory(data -> data.getValue().apellidoProperty());
        colCedula.setCellValueFactory(data -> data.getValue().cedulaProperty());
        colTelefono.setCellValueFactory(data -> data.getValue().telefonoProperty());
        colDestino.setCellValueFactory(data -> data.getValue().destinoProperty());

        cargarPasajeros();
    }

    private void cargarPasajeros() {
        listaPasajeros.clear();
        try (Connection conn = ConexionSql.getConexion()) {
            if (conn == null) {
                mostrarAlerta("Error", "No se pudo conectar con la base de datos de viajes.");
                return;
            }

            String sql = "SELECT * FROM pasajeros";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listaPasajeros.add(new Pasajero(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getString("destino")
                ));
            }

            tablaPasajeros.setItems(listaPasajeros);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar pasajeros: " + e.getMessage());
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaPasajeros.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
