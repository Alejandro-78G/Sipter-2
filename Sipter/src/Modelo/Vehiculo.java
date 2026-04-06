package Modelo;

import javafx.beans.property.*;

public class Vehiculo {
    private IntegerProperty id;
    private StringProperty placa;
    private StringProperty marca;
    private StringProperty modelo;
    private IntegerProperty capacidad;
    private StringProperty estado;

    public Vehiculo(int id, String placa, String marca, String modelo, int capacidad, String estado) {
        this.id = new SimpleIntegerProperty(id);
        this.placa = new SimpleStringProperty(placa);
        this.marca = new SimpleStringProperty(marca);
        this.modelo = new SimpleStringProperty(modelo);
        this.capacidad = new SimpleIntegerProperty(capacidad);
        this.estado = new SimpleStringProperty(estado);
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public String getPlaca() { return placa.get(); }
    public StringProperty placaProperty() { return placa; }

    public String getMarca() { return marca.get(); }
    public StringProperty marcaProperty() { return marca; }

    public String getModelo() { return modelo.get(); }
    public StringProperty modeloProperty() { return modelo; }

    public int getCapacidad() { return capacidad.get(); }
    public IntegerProperty capacidadProperty() { return capacidad; }

    public String getEstado() { return estado.get(); }
    public StringProperty estadoProperty() { return estado; }
}
