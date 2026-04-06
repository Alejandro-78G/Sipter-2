package Controlador;

import Modelo.ConexionSql;
import Modelo.Vehiculo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class ControladorGestionarVehiculos {

    @FXML private TextField txtPlaca, txtMarca, txtModelo, txtCapacidad;
    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private TableColumn<Vehiculo, Integer> colId;
    @FXML private TableColumn<Vehiculo, String> colPlaca, colMarca, colModelo, colEstado;
    @FXML private TableColumn<Vehiculo, Integer> colCapacidad;

    private ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        colPlaca.setCellValueFactory(c -> c.getValue().placaProperty());
        colMarca.setCellValueFactory(c -> c.getValue().marcaProperty());
        colModelo.setCellValueFactory(c -> c.getValue().modeloProperty());
        colCapacidad.setCellValueFactory(c -> c.getValue().capacidadProperty().asObject());
        colEstado.setCellValueFactory(c -> c.getValue().estadoProperty());
        cargarVehiculos();
    }

    @FXML
    void cargarVehiculos() {
        listaVehiculos.clear();
        try (Connection conn = ConexionSql.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM vehiculos")) {

            while (rs.next()) {
                listaVehiculos.add(new Vehiculo(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getInt("capacidad"),
                        rs.getString("estado")
                ));
            }
            tablaVehiculos.setItems(listaVehiculos);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void agregarVehiculo() {
        try (Connection conn = ConexionSql.getConexion()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO vehiculos (placa, marca, modelo, capacidad, estado) VALUES (?, ?, ?, ?, 'Activo')");
            ps.setString(1, txtPlaca.getText());
            ps.setString(2, txtMarca.getText());
            ps.setString(3, txtModelo.getText());
            ps.setInt(4, Integer.parseInt(txtCapacidad.getText()));
            ps.executeUpdate();
            cargarVehiculos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void eliminarVehiculo() {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try (Connection conn = ConexionSql.getConexion()) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM vehiculos WHERE id = ?");
                ps.setInt(1, seleccionado.getId());
                ps.executeUpdate();
                cargarVehiculos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void actualizarVehiculo() {
        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try (Connection conn = ConexionSql.getConexion()) {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE vehiculos SET placa=?, marca=?, modelo=?, capacidad=? WHERE id=?");
                ps.setString(1, txtPlaca.getText());
                ps.setString(2, txtMarca.getText());
                ps.setString(3, txtModelo.getText());
                ps.setInt(4, Integer.parseInt(txtCapacidad.getText()));
                ps.setInt(5, seleccionado.getId());
                ps.executeUpdate();
                cargarVehiculos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
