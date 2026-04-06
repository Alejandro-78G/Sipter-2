package Controlador;

import Modelo.ConexionSql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ControladorVehiculo {

    @FXML
    private TableView<Vehiculo> tablaVehiculos;
    @FXML
    private TableColumn<Vehiculo, Integer> colId;
    @FXML
    private TableColumn<Vehiculo, String> colPlaca;
    @FXML
    private TableColumn<Vehiculo, String> colModelo;
    @FXML
    private TableColumn<Vehiculo, Integer> colCapacidad;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        cargarVehiculos(); // carga automáticamente al iniciar
    }

    @FXML
    public void cargarVehiculos() {
        ObservableList<Vehiculo> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM vehiculos";

        try (Connection conn = ConexionSql.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Vehiculo(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getInt("capacidad")
                ));
            }

            tablaVehiculos.setItems(lista);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class Vehiculo {
        private final int id;
        private final String placa;
        private final String modelo;
        private final int capacidad;

        public Vehiculo(int id, String placa, String modelo, int capacidad) {
            this.id = id;
            this.placa = placa;
            this.modelo = modelo;
            this.capacidad = capacidad;
        }

        public int getId() { return id; }
        public String getPlaca() { return placa; }
        public String getModelo() { return modelo; }
        public int getCapacidad() { return capacidad; }
    }
}
