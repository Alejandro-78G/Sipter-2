package Controlador;


import Modelo.ConexionSql;
import Modelo.Ruta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;

public class ControladorGestionRutas {

    @FXML
    private TableView<Ruta> tablaRutas;
    @FXML
    private TableColumn<Ruta, Integer> colId;
    @FXML
    private TableColumn<Ruta, String> colOrigen;
    @FXML
    private TableColumn<Ruta, String> colDestino;
    @FXML
    private TableColumn<Ruta, Double> colTarifa;
    @FXML
    private TableColumn<Ruta, String> colEstado;

    @FXML
    private TextField txtOrigen;
    @FXML
    private TextField txtDestino;
    @FXML
    private TextField txtTarifa;
    @FXML
    private ComboBox<String> cbEstado;

    private ObservableList<Ruta> listaRutas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(celda -> celda.getValue().idProperty().asObject());
        colOrigen.setCellValueFactory(celda -> celda.getValue().origenProperty());
        colDestino.setCellValueFactory(celda -> celda.getValue().destinoProperty());
        colTarifa.setCellValueFactory(celda -> celda.getValue().tarifaProperty().asObject());
        colEstado.setCellValueFactory(celda -> celda.getValue().estadoProperty());

        cbEstado.setItems(FXCollections.observableArrayList("Activa", "Inactiva"));
        cargarRutas(null);
    }

    @FXML
    public void cargarRutas(ActionEvent event) {
        listaRutas.clear();
        try (Connection conn = ConexionSql.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rutas")) {

            while (rs.next()) {
                listaRutas.add(new Ruta(
                        rs.getInt("id"),
                        rs.getString("origen"),
                        rs.getString("destino"),
                        rs.getDouble("tarifa"),
                        rs.getString("estado")
                ));
            }
            tablaRutas.setItems(listaRutas);

        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "No se pudieron cargar las rutas: " + e.getMessage());
        }
    }

    @FXML
    private void agregarRuta(ActionEvent event) {
        String origen = txtOrigen.getText();
        String destino = txtDestino.getText();
        String tarifaTexto = txtTarifa.getText();
        String estado = cbEstado.getValue();

        if (origen.isEmpty() || destino.isEmpty() || tarifaTexto.isEmpty() || estado == null) {
            mostrarAlerta("Campos incompletos", "Por favor, completa todos los campos.");
            return;
        }

        try {
            double tarifa = Double.parseDouble(tarifaTexto);
            try (Connection conn = ConexionSql.getConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO rutas (origen, destino, tarifa, estado) VALUES (?, ?, ?, ?)")) {
                ps.setString(1, origen);
                ps.setString(2, destino);
                ps.setDouble(3, tarifa);
                ps.setString(4, estado);
                ps.executeUpdate();
                mostrarAlerta("Ruta agregada", "La ruta fue agregada correctamente.");
                limpiarCampos();
                cargarRutas(null);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La tarifa debe ser un número válido.");
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "No se pudo agregar la ruta: " + e.getMessage());
        }
    }

    @FXML
    private void actualizarRuta(ActionEvent event) {
        Ruta rutaSeleccionada = tablaRutas.getSelectionModel().getSelectedItem();
        if (rutaSeleccionada == null) {
            mostrarAlerta("Sin selección", "Selecciona una ruta para actualizar.");
            return;
        }

        String origen = txtOrigen.getText();
        String destino = txtDestino.getText();
        String tarifaTexto = txtTarifa.getText();
        String estado = cbEstado.getValue();

        if (origen.isEmpty() || destino.isEmpty() || tarifaTexto.isEmpty() || estado == null) {
            mostrarAlerta("Campos incompletos", "Por favor, completa todos los campos.");
            return;
        }

        try {
            double tarifa = Double.parseDouble(tarifaTexto);
            try (Connection conn = ConexionSql.getConexion();
                 PreparedStatement ps = conn.prepareStatement(
                         "UPDATE rutas SET origen = ?, destino = ?, tarifa = ?, estado = ? WHERE id = ?")) {
                ps.setString(1, origen);
                ps.setString(2, destino);
                ps.setDouble(3, tarifa);
                ps.setString(4, estado);
                ps.setInt(5, rutaSeleccionada.getId());
                ps.executeUpdate();
                mostrarAlerta("Ruta actualizada", "La ruta fue actualizada correctamente.");
                limpiarCampos();
                cargarRutas(null);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La tarifa debe ser un número válido.");
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "No se pudo actualizar la ruta: " + e.getMessage());
        }
    }

    @FXML
    private void eliminarRuta(ActionEvent event) {
        Ruta rutaSeleccionada = tablaRutas.getSelectionModel().getSelectedItem();
        if (rutaSeleccionada == null) {
            mostrarAlerta("Sin selección", "Selecciona una ruta para eliminar.");
            return;
        }

        try (Connection conn = ConexionSql.getConexion();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM rutas WHERE id = ?")) {
            ps.setInt(1, rutaSeleccionada.getId());
            ps.executeUpdate();
            mostrarAlerta("Ruta eliminada", "La ruta fue eliminada correctamente.");
            cargarRutas(null);
        } catch (SQLException e) {
            mostrarAlerta("Error SQL", "No se pudo eliminar la ruta: " + e.getMessage());
        }
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) tablaRutas.getScene().getWindow();
        stage.close();
    }

    private void limpiarCampos() {
        txtOrigen.clear();
        txtDestino.clear();
        txtTarifa.clear();
        cbEstado.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
