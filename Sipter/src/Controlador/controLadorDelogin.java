package Controlador;

import Modelo.ConexionSql;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class controLadorDelogin {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Label lblMensaje;

    @FXML
    private void handleLogin() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            lblMensaje.setText("Por favor ingrese usuario y contraseña.");
            return;
        }

        try (Connection conn = ConexionSql.getConexion()) {
            if (conn == null) {
                lblMensaje.setText("No se pudo establecer conexión con la base de datos.");
                return;
            }

            String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String rol = rs.getString("rol");

                Stage stageActual = (Stage) txtUsuario.getScene().getWindow();
                stageActual.close();

                String fxmlMenu;
                String titulo;

                switch (rol.toLowerCase()) {
                    case "conductor":
                        fxmlMenu = "/vista/menuConductor.fxml";
                        titulo = "Menú Conductor";
                        break;
                    case "admin":
                        fxmlMenu = "/vista/menuAdmin.fxml";
                        titulo = "Menú Administrador";
                        break;
                    case "usuario":
                        fxmlMenu = "/vista/menu.fxml";
                        titulo = "Menú Usuario";
                        break;
                    default:
                        lblMensaje.setText("Rol no reconocido. Contacte al administrador.");
                        rs.close();
                        ps.close();
                        return;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlMenu));
                Parent root = loader.load();

                Stage stageMenu = new Stage();
                stageMenu.setTitle(titulo);
                stageMenu.setScene(new Scene(root));
                stageMenu.show();

            } else {
                lblMensaje.setText("Usuario o contraseña incorrectos");
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            lblMensaje.setText("Error al consultar en la base de datos.");
            e.printStackTrace();
        } catch (Exception e) {
            lblMensaje.setText("Ocurrió un error inesperado.");
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/registro.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(txtUsuario.getScene().getWindow());
            stage.showAndWait();

            FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/vista/login.fxml"));
            Parent rootLogin = loaderLogin.load();
            Stage stageLogin = new Stage();
            stageLogin.setTitle("Iniciar Sesión");
            stageLogin.setScene(new Scene(rootLogin));
            stageLogin.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
