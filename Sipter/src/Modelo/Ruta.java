package Modelo;

import javafx.beans.property.*;

public class Ruta {

    private final IntegerProperty id;
    private final StringProperty origen;
    private final StringProperty destino;
    private final DoubleProperty tarifa;
    private final StringProperty estado;


    public Ruta(int id, String origen, String destino, double tarifa, String estado) {
        this.id = new SimpleIntegerProperty(id);
        this.origen = new SimpleStringProperty(origen);
        this.destino = new SimpleStringProperty(destino);
        this.tarifa = new SimpleDoubleProperty(tarifa);
        this.estado = new SimpleStringProperty(estado);
    }


    public Ruta(String origen, String destino, double tarifa, String estado) {
        this.id = new SimpleIntegerProperty(0);
        this.origen = new SimpleStringProperty(origen);
        this.destino = new SimpleStringProperty(destino);
        this.tarifa = new SimpleDoubleProperty(tarifa);
        this.estado = new SimpleStringProperty(estado);
    }


    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getOrigen() {
        return origen.get();
    }

    public void setOrigen(String origen) {
        this.origen.set(origen);
    }

    public StringProperty origenProperty() {
        return origen;
    }

    public String getDestino() {
        return destino.get();
    }

    public void setDestino(String destino) {
        this.destino.set(destino);
    }

    public StringProperty destinoProperty() {
        return destino;
    }

    public double getTarifa() {
        return tarifa.get();
    }

    public void setTarifa(double tarifa) {
        this.tarifa.set(tarifa);
    }

    public DoubleProperty tarifaProperty() {
        return tarifa;
    }

    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public StringProperty estadoProperty() {
        return estado;
    }
}
