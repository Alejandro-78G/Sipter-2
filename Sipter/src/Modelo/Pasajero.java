package Modelo;

import javafx.beans.property.*;

public class Pasajero {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty apellido;
    private final StringProperty cedula;
    private final StringProperty telefono;
    private final StringProperty destino;

    public Pasajero(int id, String nombre, String apellido, String cedula, String telefono, String destino) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.cedula = new SimpleStringProperty(cedula);
        this.telefono = new SimpleStringProperty(telefono);
        this.destino = new SimpleStringProperty(destino);
    }

    public int getId() { return id.get(); }
    public String getNombre() { return nombre.get(); }
    public String getApellido() { return apellido.get(); }
    public String getCedula() { return cedula.get(); }
    public String getTelefono() { return telefono.get(); }
    public String getDestino() { return destino.get(); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty apellidoProperty() { return apellido; }
    public StringProperty cedulaProperty() { return cedula; }
    public StringProperty telefonoProperty() { return telefono; }
    public StringProperty destinoProperty() { return destino; }
}
