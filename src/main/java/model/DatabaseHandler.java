package model;

import javafx.scene.control.ComboBox;
import main.Main;
import utils.DialogManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;

public class DatabaseHandler {

    private Connection connection = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

    public boolean checkLoginAndPass(String login, String password){

        String sql = "select * from users where username = ? and password = ?";
        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2,password);
            resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addBoxInDatabase(Employee employee, String kilo, int id_parameter){

        String sql = "INSERT INTO box(id_box, id_employee, kilogramm, id_parameter) VALUES (NEXTVAL('box_id_box_seq'), ?, ?, ?)";
        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(employee.getId_employee()));
            preparedStatement.setDouble(2, Double.parseDouble(kilo));
            preparedStatement.setInt(3, id_parameter);
            preparedStatement.execute();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addEmployeeInDatabase(String selo, String employeeName, Main main, int id_parameter){
        String sqlSelo = "INSERT INTO selo VALUES (NEXTVAL('selo_id_sela_seq'), ?)";
        String sqlAddEmployee = "INSERT INTO employee(id_employee, name_e, id_sela) VALUES (NEXTVAL('employee_id_employee_seq'), ?, ?)";

        int count = 0;

        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sqlSelo);

            for (Map.Entry<Integer, String> s : main.getCollections().getSetSeloList()) {
                if (selo.equals(s.getValue())) {
                    count++;
                }
            }
            System.out.println(count);

            if (count == 0) {
                if(DialogManager.showConfirmationDialog("Не существует!", "Села " + selo + " не существует! Хотите добавить это село в базу?") == 1) {
                    preparedStatement.setString(1, selo);
                    preparedStatement.execute();
                }
            }
            connection.close();

            for (Employee employees : main.getCollections().getEmployees()){
                if (employeeName.equals(employees.getName())){
                    DialogManager.showWarningDialog("Ошибка!", "Рабочий с таким именем существует!");
                    return;
                }
            }
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sqlAddEmployee);
            preparedStatement.setString(1, employeeName);

            for (Map.Entry<Integer, String> s : main.getCollections().getSetSeloList()) {
                if (selo.equals(s.getValue())) {
                    preparedStatement.setInt(2, s.getKey());
                    preparedStatement.execute();
                    addPreparedBox(employeeName, id_parameter);
                    break;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void removeEmployeeInDatabase(Employee employee){
        String sql = "DELETE FROM employee WHERE id_employee = ?";

        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(employee.getId_employee()));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeBoxInDatabase(Box box){
        String sql = "DELETE FROM box WHERE id_box = ?";

        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(box.getId_box()));
            preparedStatement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void editingKilogramBoxInDatabase(Box box, String newKilogramm){
        String sql = "UPDATE box SET kilogramm = ? WHERE id_box = ?";

        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, Double.parseDouble(newKilogramm));
            preparedStatement.setInt(2, Integer.parseInt(box.getId_box()));
            preparedStatement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateBoxes(Employee employee, Main main){
        String sql = "SELECT id_box, kilogramm FROM box B, employee E\n" +
                "\tWHERE E.id_employee = B.id_employee AND E.name_e IN ('" + employee.getName() +"')";

        int count_box = 1;

        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                String id_box = resultSet.getString("id_box");
                String kg = resultSet.getString("kilogramm");
                main.getCollections().addBox(new Box(id_box, kg, Integer.toString(count_box++)));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployeeInDatabase(Employee employee, String seloName, String employeeName, Main main){
        String sql = "UPDATE employee SET name_e = ?, id_sela = ? WHERE id_employee = ?";
        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (Map.Entry<Integer, String> s : main.getCollections().getSetSeloList()) {
                if (seloName.equals(s.getValue())) {
                    preparedStatement.setString(1, employeeName);
                    preparedStatement.setInt(2, s.getKey());
                    preparedStatement.setInt(3, Integer.parseInt(employee.getId_employee()));
                    preparedStatement.execute();
                    break;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Parameter getParameterOnDatabase(int id_parameter){
        String sql = "SELECT data_, tare_weight, cost_1kg FROM parameter WHERE id_parameter = ?";

        Parameter parameter = null;
        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id_parameter);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String date = resultSet.getString("data_");
                String tareWeight = resultSet.getString("tare_weight");
                String cost1Kg = resultSet.getString("cost_1kg");

                parameter = new Parameter(Integer.toString(id_parameter), date, tareWeight, cost1Kg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parameter;
    }

    public void addParameterInDatabase(LocalDate date, String weightTare, String cost1kg){
        String sql = "INSERT INTO parameter (id_parameter, data_, tare_weight, cost_1kg) VALUES (NEXTVAL('parameter_id_parameter_seq'), ?, ?, ?)";

        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setDouble(2, Double.parseDouble(weightTare));
            preparedStatement.setDouble(3, Double.parseDouble(cost1kg));
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeParameterAndUpdateDate(LocalDate date, String weightTare, String cost1kg, ComboBox<Integer> comboBox){
        String sqlUpdate = "UPDATE parameter SET data_ = ?, tare_weight = ?, cost_1kg = ? WHERE id_parameter = ?";

        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.setDouble(2, Double.parseDouble(weightTare));
            preparedStatement.setDouble(3, Double.parseDouble(cost1kg));
            preparedStatement.setInt(4, comboBox.getSelectionModel().getSelectedItem());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateParameterInTableBox(int id_parameter){
        String sql = "UPDATE box SET id_parameter = ?";
        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id_parameter);
            preparedStatement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateParameterWithZeroKilo(int id_parameter){
        String sql = "UPDATE box SET id_parameter = ?, kilogramm = ?";
        try {
            connection = DatabaseConnector.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id_parameter);
            preparedStatement.setDouble(2, 0);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializeParameters(Main main){
        String sql = "SELECT id_parameter, data_, tare_weight, cost_1kg FROM parameter";

        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                String id_parameter = resultSet.getString("id_parameter");
                String date = resultSet.getString("data_");
                String tareWeight = resultSet.getString("tare_weight");
                String cost1Kg = resultSet.getString("cost_1kg");

                main.getCollections().addOption(Integer.parseInt(id_parameter));
                main.getCollections().addParameter(new Parameter(id_parameter, date, tareWeight, cost1Kg));

                main.getCollections().getOptions().sort(Comparator.naturalOrder());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void initializeEmployees(Main main){
        String sql = "SELECT DISTINCT E.id_employee, name_e, name_selo, amount_box, clean_kg, salary, data_ " +
                        "FROM selo S, employee E, box B, parameter P " +
                            "WHERE S.id_sela = E.id_sela AND" +
                                "  E.id_employee = B.id_employee AND B.id_parameter = P.id_parameter";



        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                String id_employee = resultSet.getString("id_employee");
                String name = resultSet.getString("name_e");
                String name_selo = resultSet.getString("name_selo");
                String amount_box = resultSet.getString("amount_box");
                String clean_kg = resultSet.getString("clean_kg");
                String salary = resultSet.getString("salary") + " ₴";
                String date = resultSet.getString("data_");
                main.getCollections().addEmployee(new Employee(id_employee, name, name_selo, amount_box, clean_kg, salary, date));
            }

            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void initializeSelo(Main main){
        String sqlSelo = "SELECT id_sela, name_selo FROM selo";
        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlSelo);

            while (resultSet.next()){
                int id_sela = resultSet.getInt("id_sela");
                String name_selo = resultSet.getString("name_selo");
                main.getCollections().addSelo(id_sela, name_selo);
            }

            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void addPreparedBox(String employeeName, int id_parameter){
        String sqlAddBoxWithEmployee = "INSERT INTO box(id_box, id_employee, kilogramm, id_parameter) VALUES (NEXTVAL('box_id_box_seq'), ?, ?, ?)";
        String sqlIdEmployee = "SELECT id_employee FROM employee WHERE name_e LIKE('"+ employeeName +"')";
        String id_employee = "";

        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlIdEmployee);

            while (resultSet.next()){
                id_employee = resultSet.getString("id_employee");
            }

            preparedStatement = connection.prepareStatement(sqlAddBoxWithEmployee);

            preparedStatement.setInt(1, Integer.parseInt(id_employee));
            preparedStatement.setInt(2, 1);
            preparedStatement.setInt(3, id_parameter);
            preparedStatement.execute();

            statement.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

}
