package Controlador;

import Modelo.ConexionSql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ControladorVerPasajeros {

    @FXML
    private TableView<Pasajero> tablaPasajeros;
    @FXML
    private TableColumn<Pasajero, String> colNombre;
    @FXML
    private TableColumn<Pasajero, String> colApellido;
    @FXML
    private TableColumn<Pasajero, String> colDestino;

    private ObservableList<Pasajero> listaPasajeros = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        cargarPasajeros();
    }

    private void cargarPasajeros() {
        String sql = "SELECT nombre, apellido, destino FROM pasajeros";
        try (Connection conn = ConexionSql.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            listaPasajeros.clear();
            while (rs.next()) {
                listaPasajeros.add(new Pasajero(
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("destino")
                ));
            }
            tablaPasajeros.setItems(listaPasajeros);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar los pasajeros: " + e.getMessage());
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaPasajeros.getScene().getWindow();
        stage.close();
    }

    public static class Pasajero {
        private String nombre;
        private String apellido;
        private String destino;

        public Pasajero(String nombre, String apellido, String destino) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.destino = destino;
        }

        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getDestino() { return destino; }
    }
}
