package Controlador;

public abstract class ControladorBase {
    protected String usuarioActivo;
    protected String rolActivo;

    public void setUsuarioActivo(String usuario, String rol) {
        this.usuarioActivo = usuario;
        this.rolActivo = rol;
    }
}
