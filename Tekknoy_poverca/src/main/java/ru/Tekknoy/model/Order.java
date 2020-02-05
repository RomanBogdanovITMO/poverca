package ru.Tekknoy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Document(collection = "Order")
public class Order {
    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";
    @Id
    private long id;

    private String numberOrder;

    private String poverca;

    private String startPoverc;

    private String statusPoverce;

    private String dataShipment;

    private String daysForShipment;

    private String dataOfArrival;

    private String dateOfDelivery;

    private String colorShipment = "#FFFFFF";
    private String colorPoverce = "#FFFFFF";

    public Order() {
    }

    public Order(long id, String numberOrder, String poverca, String startPoverc, String statusPoverce,
                 String dataOfArrival, String dateOfDelivery, String daysForShipment, String dataShipment) {
        this.id = id;
        this.numberOrder = numberOrder;
        this.poverca = poverca;
        this.startPoverc = startPoverc;
        this.statusPoverce = statusPoverce;
        this.dataShipment = dataShipment;
        this.daysForShipment = daysForShipment;
        this.dataOfArrival = dataOfArrival;
        this.dateOfDelivery = dateOfDelivery;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(String numberOrder) {
        this.numberOrder = numberOrder;
    }

    public String getPoverca() {
        return poverca;
    }

    public void setPoverca(String poverca) {
        this.poverca = poverca;
    }

    public String getStartPoverc() {
        return startPoverc;
    }

    public void setStartPoverc(String startPoverc) {
        this.startPoverc = startPoverc;
    }

    public String getStatusPoverce() {
        return statusPoverce;
    }

    public void setStatusPoverce(String statusPoverce) {
        this.statusPoverce = statusPoverce;
    }

    public String getDataShipment() {
        return dataShipment;
    }

    public void setDataShipment(String dataShipment) {
        this.dataShipment = dataShipment;
    }

    public String getDaysForShipment() {
        return daysForShipment;
    }

    public void setDaysForShipment(String daysForShipment) {
        this.daysForShipment = daysForShipment;
    }

    public String getDataOfArrival() {
        return dataOfArrival;
    }

    public void setDataOfArrival(String dataOfArrival) {
        this.dataOfArrival = dataOfArrival;
    }

    public String getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(String dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public String getColorShipment() {
        return colorShipment;
    }

    public void setColorShipment(String colorShipment) {
        this.colorShipment = colorShipment;
    }

    public String getColorPoverce() {
        return colorPoverce;
    }

    public void setColorPoverce(String colorPoverce) {
        this.colorPoverce = colorPoverce;
    }


    @Override
    public String toString() {
        return "" + id + "," + numberOrder + "," + poverca + "," +
                startPoverc + "," + statusPoverce + "," + dataOfArrival + "," +
                dateOfDelivery + "," + daysForShipment + "," + dataShipment;

    }
}
