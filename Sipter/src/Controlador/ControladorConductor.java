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

public class ControladorConductor {

    @FXML private TableView<Conductor> tablaConductores;
    @FXML private TableColumn<Conductor, Integer> colId;
    @FXML private TableColumn<Conductor, String> colNombre;
    @FXML private TableColumn<Conductor, String> colLicencia;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colLicencia.setCellValueFactory(new PropertyValueFactory<>("licencia"));
        cargarConductores();
    }

    @FXML
    public void cargarConductores() {
        ObservableList<Conductor> lista = FXCollections.observableArrayList();
        String sql = "SELECT * FROM conductores";

        try (Connection conn = ConexionSql.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Conductor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("licencia")
                ));
            }

            tablaConductores.setItems(lista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Conductor {
        private final int id;
        private final String nombre;
        private final String licencia;

        public Conductor(int id, String nombre, String licencia) {
            this.id = id;
            this.nombre = nombre;
            this.licencia = licencia;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getLicencia() { return licencia; }
    }
}
