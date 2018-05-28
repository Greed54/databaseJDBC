package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee {

    private final StringProperty id_employee;
    private final StringProperty name;
    private final StringProperty selo;
    private final StringProperty amount_box;
    private final StringProperty clean_kg;
    private final StringProperty salary;
    private final StringProperty date;


    Employee(String id_employee, String name, String selo, String amount_box, String clean_kg, String salary, String date) {
        this.id_employee = new SimpleStringProperty(id_employee);
        this.name = new SimpleStringProperty(name);
        this.selo = new SimpleStringProperty(selo);
        this.amount_box = new SimpleStringProperty(amount_box);
        this.clean_kg = new SimpleStringProperty(clean_kg);
        this.salary = new SimpleStringProperty(salary);
        this.date = new SimpleStringProperty(date);
    }

    public String getName(){
        return name.get();
    }

    public void setName(String name){
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getSelo() {
        return selo.get();
    }

    public StringProperty seloProperty() {
        return selo;
    }

    public StringProperty amount_boxProperty() {
        return amount_box;
    }

    public StringProperty clean_kgProperty() {
        return clean_kg;
    }

    public String getSalary() {
        return salary.get();
    }

    public StringProperty salaryProperty() {
        return salary;
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getId_employee() {
        return id_employee.get();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id_employee=" + id_employee +
                ", name=" + name +
                ", selo=" + selo +
                ", amount_box=" + amount_box +
                ", clean_kg=" + clean_kg +
                ", salary=" + salary +
                ", date=" + date +
                '}';
    }
}

