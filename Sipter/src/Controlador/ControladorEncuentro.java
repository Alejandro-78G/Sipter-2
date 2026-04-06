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
import java.sql.Time;

public class ControladorEncuentro {

    @FXML
    private TableView<Encuentro> tablaEncuentros;
    @FXML
    private TableColumn<Encuentro, String> colLugar;
    @FXML
    private TableColumn<Encuentro, Time> colHora;

    private ObservableList<Encuentro> listaEncuentros = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colLugar.setCellValueFactory(new PropertyValueFactory<>("lugar"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        cargarEncuentros();
    }

    private void cargarEncuentros() {
        String sql = "SELECT lugar, hora FROM encuentros";
        try (Connection conn = ConexionSql.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listaEncuentros.add(new Encuentro(
                        rs.getString("lugar"),
                        rs.getTime("hora")
                ));
            }

            tablaEncuentros.setItems(listaEncuentros);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al cargar los lugares de encuentro.");
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaEncuentros.getScene().getWindow();
        stage.close();
    }

    public static class Encuentro {
        private String lugar;
        private Time hora;

        public Encuentro(String lugar, Time hora) {
            this.lugar = lugar;
            this.hora = hora;
        }

        public String getLugar() { return lugar; }
        public Time getHora() { return hora; }
    }
}
