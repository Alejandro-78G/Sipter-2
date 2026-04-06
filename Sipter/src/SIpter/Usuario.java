package SIpter;

public class Usuario {
    private String nombre;
    private String apellido;
    private Long cedula;
    private String tipoDocumento;
    private String contrasena;
    private String nombreUsuario;
    private String ciudad;
    private String rol;

    public Usuario(String nombre, String apellido, Long cedula, String tipoDocumento,
                   String contrasena, String nombreUsuario, String ciudad, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.tipoDocumento = tipoDocumento;
        this.contrasena = contrasena;
        this.nombreUsuario = nombreUsuario;
        this.ciudad = ciudad;
        this.rol = rol;
    }

    public Usuario() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public Long getCedula() { return cedula; }
    public void setCedula(Long cedula) { this.cedula = cedula; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
