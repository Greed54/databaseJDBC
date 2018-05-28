package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CollectionsWithData {
    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    private ObservableList<Box> boxes = FXCollections.observableArrayList();
    private HashMap<Integer,String> seloList = new HashMap<>();
    private ObservableList<Parameter> parameters = FXCollections.observableArrayList();
    private ObservableList<Integer> options = FXCollections.observableArrayList();

    public void clearEmployees(){
        this.employees.clear();
    }

    public void clearSeloList(){
        this.seloList.clear();
    }

    public void clearParameters(){
        this.parameters.clear();
    }

    public void clearOptions(){
        this.options.clear();
    }

    public ObservableList<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public ObservableList<Box> getBoxes() {
        return boxes;
    }

    public void addBox(Box box) {
        this.boxes.add(box);
    }

    public void addSelo(int id_sela, String name_selo){
        this.seloList.put(id_sela, name_selo);
    }

    public Set<Map.Entry<Integer, String>> getSetSeloList(){
        return seloList.entrySet();
    }

    public ObservableList<Parameter> getParameters() {
        return parameters;
    }

    public void addParameter(Parameter parameter){
        this.parameters.add(parameter);
    }

    public void addOption(int numberOption){
        this.options.add(numberOption);
    }

    public ObservableList<Integer> getOptions() {
        return options;
    }
}
