package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Parameter {
    private final StringProperty idParameter;
    private final StringProperty date;
    private final StringProperty tareWeight;
    private final StringProperty cost1Kg;

    public Parameter (String idParameter, String data, String tareWeight, String cost1kg){
        this.idParameter = new SimpleStringProperty(idParameter);
        this.date = new SimpleStringProperty(data);
        this.tareWeight = new SimpleStringProperty(tareWeight);
        this.cost1Kg = new SimpleStringProperty(cost1kg);
    }

    public String getData() {
        return date.get();
    }

    public void setData(String data) {
        this.date.set(data);
    }

    public String getTareWeight() {
        return tareWeight.get();
    }

    public String getCost1Kg() {
        return cost1Kg.get();
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "idParameter=" + idParameter +
                ", data=" + date +
                ", tareWeight=" + tareWeight +
                ", cost1Kg=" + cost1Kg +
                '}';
    }
}

