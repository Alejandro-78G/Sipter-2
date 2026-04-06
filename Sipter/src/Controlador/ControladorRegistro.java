package Controlador;

import SIpter.Usuario;
import SIpter.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControladorRegistro {

    @FXML
    private TextField txtNombre, txtApellido, txtCedula, txtTipoDocumento, txtUsuario, txtCiudad, txtRol;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private void registrarUsuario() {
        try {

            if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || txtCedula.getText().isEmpty() ||
                    txtTipoDocumento.getText().isEmpty() || txtContrasena.getText().isEmpty() ||
                    txtUsuario.getText().isEmpty() || txtCiudad.getText().isEmpty() || txtRol.getText().isEmpty()) {
                mostrarAlerta("Error", "Por favor completa todos los campos.");
                return;
            }

            long cedula = Long.parseLong(txtCedula.getText());

            if (UsuarioDAO.usuarioExiste(cedula, txtUsuario.getText())) {
                mostrarAlerta("Advertencia", "El usuario o la cédula ya están registrados.");
                return;
            }

            Usuario usuario = new Usuario(
                    txtNombre.getText(),
                    txtApellido.getText(),
                    cedula,
                    txtTipoDocumento.getText(),
                    txtContrasena.getText(),
                    txtUsuario.getText(),
                    txtCiudad.getText(),
                    txtRol.getText()
            );


            boolean exito = UsuarioDAO.registrarUsuario(usuario);
            if (exito) {
                mostrarAlerta("Éxito", "Usuario registrado correctamente.");
                limpiarCampos();
            } else {
                mostrarAlerta("Error", "No se pudo registrar el usuario. Revisa la conexión o la base de datos.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "La cédula debe ser un número válido.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un problema al registrar el usuario.");
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtCedula.clear();
        txtTipoDocumento.clear();
        txtContrasena.clear();
        txtUsuario.clear();
        txtCiudad.clear();
        txtRol.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
