package Controlador;

import Modelo.ConexionSql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ControladorSeleccionViaje {

    private int idUsuario;

    @FXML
    private ComboBox<String> cbLugarEncuentro;

    @FXML
    private ComboBox<String> cbDestino;

    @FXML
    private TableView<Destino> tablaDestinos;

    @FXML
    private TableColumn<Destino, String> colCiudad;

    @FXML
    private TableColumn<Destino, String> colDireccion;

    @FXML
    private TableColumn<Destino, Double> colDistancia;

    @FXML
    private Button btnSeleccionar;

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    @FXML
    public void initialize() {
        cbLugarEncuentro.setItems(FXCollections.observableArrayList("Parque Central", "Terminal", "Universidad"));
        cbDestino.setItems(FXCollections.observableArrayList("Bogotá", "Medellín", "Cali"));

        colCiudad.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCiudad()));
        colDireccion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDireccion()));
        colDistancia.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDistancia()));

        ObservableList<Destino> listaDestinos = FXCollections.observableArrayList(
                new Destino("Bogotá", "Cra 10 #45", 15.2),
                new Destino("Medellín", "Av Poblado 123", 25.7),
                new Destino("Cali", "Cl 70 #32", 18.4)
        );

        tablaDestinos.setItems(listaDestinos);
    }

    @FXML
    private void seleccionarViaje() {
        String lugar = cbLugarEncuentro.getValue();
        Destino destinoSeleccionado = tablaDestinos.getSelectionModel().getSelectedItem();

        if (lugar == null || destinoSeleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un lugar de encuentro y un destino.");
            return;
        }

        try (Connection conexion = ConexionSql.getConexion()) {
            String sql = "INSERT INTO viaje_pasajeros (id_usuario, ciudad_destino, direccion_destino, distancia, lugar_encuentro) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setString(2, destinoSeleccionado.getCiudad());
            ps.setString(3, destinoSeleccionado.getDireccion());
            ps.setDouble(4, destinoSeleccionado.getDistancia());
            ps.setString(5, lugar);
            ps.executeUpdate();

            mostrarAlerta("Éxito", "La selección se ha guardado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo guardar la selección en la base de datos.");
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

