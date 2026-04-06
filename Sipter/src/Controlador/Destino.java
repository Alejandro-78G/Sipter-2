package Controlador;

public class Destino {
    private String ciudad;
    private String direccion;
    private double distancia;

    public Destino(String ciudad, String direccion, double distancia) {
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.distancia = distancia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getDistancia() {
        return distancia;
    }
}
