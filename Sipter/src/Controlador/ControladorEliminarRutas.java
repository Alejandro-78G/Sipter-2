package Controlador;

import Modelo.ConexionSql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControladorEliminarRutas {

    @FXML private TableView<Ruta> tablaRutas;
    @FXML private TableColumn<Ruta, Integer> colId;
    @FXML private TableColumn<Ruta, String> colOrigen;
    @FXML private TableColumn<Ruta, String> colDestino;
    @FXML private TableColumn<Ruta, Double> colTarifa;
    @FXML private TableColumn<Ruta, String> colConductor;
    @FXML private TableColumn<Ruta, String> colVehiculo;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colTarifa.setCellValueFactory(new PropertyValueFactory<>("tarifa"));
        colConductor.setCellValueFactory(new PropertyValueFactory<>("conductor"));
        colVehiculo.setCellValueFactory(new PropertyValueFactory<>("vehiculo"));

        cargarRutas();
    }

    private void cargarRutas() {
        ObservableList<Ruta> lista = FXCollections.observableArrayList();
        String sql = """
                SELECT r.id_ruta, r.origen, r.destino, r.tarifa,
                       c.nombre AS conductor, v.modelo AS vehiculo
                FROM rutas r
                LEFT JOIN viajes_conductores vc ON r.id_ruta = vc.id_ruta
                LEFT JOIN conductores c ON vc.id_conductor = c.id_conductor
                LEFT JOIN vehiculos v ON c.id_vehiculo = v.id_vehiculo
                """;

        try (Connection conn = ConexionSql.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Ruta(
                        rs.getInt("id_ruta"),
                        rs.getString("origen"),
                        rs.getString("destino"),
                        rs.getDouble("tarifa"),
                        rs.getString("conductor"),
                        rs.getString("vehiculo")
                ));
            }

            tablaRutas.setItems(lista);

        } catch (SQLException e) {
            mostrarMensaje("Error al cargar rutas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void eliminarRuta() {
        Ruta rutaSeleccionada = tablaRutas.getSelectionModel().getSelectedItem();
        if (rutaSeleccionada == null) {
            mostrarMensaje("Seleccione una ruta para eliminar.");
            return;
        }

        String sql = "DELETE FROM rutas WHERE id_ruta = ?";
        try (Connection conn = ConexionSql.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rutaSeleccionada.getId());
            ps.executeUpdate();
            mostrarMensaje("Ruta eliminada correctamente.");
            cargarRutas();

        } catch (SQLException e) {
            mostrarMensaje("Error al eliminar ruta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Eliminar Ruta");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static class Ruta {
        private final Integer id;
        private final String origen;
        private final String destino;
        private final Double tarifa;
        private final String conductor;
        private final String vehiculo;

        public Ruta(int id, String origen, String destino, double tarifa, String conductor, String vehiculo) {
            this.id = id;
            this.origen = origen;
            this.destino = destino;
            this.tarifa = tarifa;
            this.conductor = conductor != null ? conductor : "Sin asignar";
            this.vehiculo = vehiculo != null ? vehiculo : "Sin asignar";
        }

        public Integer getId() { return id; }
        public String getOrigen() { return origen; }
        public String getDestino() { return destino; }
        public Double getTarifa() { return tarifa; }
        public String getConductor() { return conductor; }
        public String getVehiculo() { return vehiculo; }
    }
}

