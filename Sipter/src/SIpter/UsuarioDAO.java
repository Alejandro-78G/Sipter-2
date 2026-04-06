package SIpter;

import Modelo.ConexionSql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public static boolean registrarUsuario(Usuario usuario) {
        Connection conn = ConexionSql.getConexion();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String verificar = "SELECT * FROM usuarios WHERE documento = ? OR nombre_usuario = ?";
            ps = conn.prepareStatement(verificar);
            ps.setLong(1, usuario.getCedula());
            ps.setString(2, usuario.getNombreUsuario());
            rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("El usuario ya está registrado en la base de datos.");
                return false;
            }

            String sql = "INSERT INTO usuarios (nombre, apellido, documento, tipo_documento, contrasena, nombre_usuario, ciudad, rol) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setLong(3, usuario.getCedula());
            ps.setString(4, usuario.getTipoDocumento());
            ps.setString(5, usuario.getContrasena());
            ps.setString(6, usuario.getNombreUsuario());
            ps.setString(7, usuario.getCiudad());
            ps.setString(8, usuario.getRol());

            ps.executeUpdate();
            System.out.println("Usuario registrado correctamente.");
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
            return false;

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean usuarioExiste(Long cedula, String nombreUsuario) {
        String sql = "SELECT * FROM usuarios WHERE documento = ? OR nombre_usuario = ?";
        try (Connection conn = ConexionSql.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, cedula);
            stmt.setString(2, nombreUsuario);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("Error al verificar existencia del usuario: " + e.getMessage());
            return false;
        }
    }

}
