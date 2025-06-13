package com.example.monitoring_communication_service.entities.dtos;

public class GraphicDataToSendDTO {
    private int minut;
    private int suma;

    public GraphicDataToSendDTO(int minut, int suma){
        this.minut = minut;
        this.suma = suma;
    }

    public int getMinut() {
        return minut;
    }

    public void setMinut(int minut) {
        this.minut = minut;
    }

    public int getSuma() {
        return suma;
    }

    public void setSuma(int suma) {
        this.suma = suma;
    }
}
