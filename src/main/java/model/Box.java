package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Box {

    private final StringProperty id_box;
    private final StringProperty kilogram;
    private final StringProperty count_box;

    public Box(String id_box, String kilogram, String count_box) {
        this.id_box = new SimpleStringProperty(id_box);
        this.kilogram = new SimpleStringProperty(kilogram);
        this.count_box = new SimpleStringProperty(count_box);
    }


    public StringProperty kilogramProperty() {
        return kilogram;
    }

    public StringProperty count_boxProperty() {
        return count_box;
    }

    public String getId_box() {
        return id_box.get();
    }

}
