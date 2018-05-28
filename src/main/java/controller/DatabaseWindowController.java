package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import main.Main;
import model.Box;
import model.Employee;
import utils.DialogManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.regex.Pattern;

public class DatabaseWindowController {

    private Main main;
    private Employee employee;
    private int id_parameter;

    // table with employee
    @FXML
    private TableView<Employee> tree_table;
    @FXML
    private TableColumn<Employee, String> col_name;
    @FXML
    private TableColumn<Employee, String> col_selo;
    @FXML
    private TableColumn<Employee, String> col_amount_box;
    @FXML
    private TableColumn<Employee, String> col_clean_kg;
    @FXML
    private TableColumn<Employee, String> col_salary;
    @FXML
    private TableColumn<Employee, String> col_date;

    // table with box
    @FXML
    private TableView<Box> table_box_kg;
    @FXML
    private TableColumn<Box, String> col_nb_box;
    @FXML
    private TableColumn<Box, String> col_kg;

    // editable field
    @FXML
    private TextField cmd_name_emp;
    @FXML
    private TextField cmd_selo_emp;

    //search field
    @FXML
    private TextField cmd_search;

    @FXML
    private Label labelCount;

    @FXML
    private TextField cmd_box_field;


    public void setMain(Main main){
        this.main = main;
    }


    public void init(){
        preInit();
        initListeners();
        updateCountLabelEmployee();
        actionSearch();
    }

    @FXML
    private void saveBoxInDatabase (){
        if (checkValuesBoxKg()){
            return;
        }
        main.getDatabase().addBoxInDatabase(employee, cmd_box_field.getText(), id_parameter);
        cmd_box_field.setText("");
        updateTableBox();
        updateTableEmployees();

        tree_table.getSelectionModel().select(employee);
    }

    @FXML
    private void saveEmployeeInDatabase () {
        if (!checkValues()){
            return;
        }
        main.getDatabase().addEmployeeInDatabase(cmd_selo_emp.getText(), cmd_name_emp.getText(), main, id_parameter);
        updateTableEmployees();
    }

    @FXML
    private void delEmployeeInDatabase () {
        employee = tree_table.getSelectionModel().getSelectedItem();

        if (!employeeIsSelected(employee)){
            return;
        }
        if (DialogManager.showConfirmationDialog("Удаление", "Вы действительно хотите удалить рабочего?") == 1) {
            main.getDatabase().removeEmployeeInDatabase(employee);
        }
        updateTableEmployees();
    }

    @FXML
    private void delBoxInDatabase (){
        Box selectedBox = table_box_kg.getSelectionModel().getSelectedItem();

        if (boxIsSelected(selectedBox)){
            return;
        }
        if (DialogManager.showConfirmationDialog("Удаление", "Вы действительно хотите удалить ящик?") == 1){
            main.getDatabase().removeBoxInDatabase(selectedBox);
        }
        updateTableBox();
        updateTableEmployees();
    }

    @FXML
    private void setParameter () {
        main.showParameterWindow(main);
        updateTableEmployees();
    }

    @FXML
    private void editBox() {
        Box selectedBox = table_box_kg.getSelectionModel().getSelectedItem();

        if (boxIsSelected(selectedBox)){
            return;
        }
        if (checkValuesBoxKg()){
            return;
        }
        main.getDatabase().editingKilogramBoxInDatabase(selectedBox, cmd_box_field.getText());
        updateTableBox();
        updateTableEmployees();
    }

    @FXML
    private void saveFile() {
        int i = 1;
        double sumSal = 0;
        String dots = ".......................................................................................";
        String marg = "                                            ";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("saveSalary");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(main.getPrimaryStage());
        if (file != null){
            Document document = new Document(PageSize.A4);
            try {
                BaseFont bf = BaseFont.createFont("C:\\\\Windows\\Fonts\\ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font font = new Font(bf);
                Font font1 = new Font(bf);
                font1.setSize(16);
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.setMargins(70, 38, 76, 76);
                document.open();

                for (Map.Entry<Integer, String> s : main.getCollections().getSetSeloList()) {
                    document.add(new Chapter(new Paragraph(marg + s.getValue(), font1), i));
                    document.add(new Paragraph("\n"));
                    for (Employee e : main.getCollections().getEmployees()) {
                        if (s.getValue().equals(e.getSelo())) {
                            sumSal += Double.parseDouble(e.getSalary().replace('₴', ' '));
                            document.add(new Paragraph(e.getName() + dots + e.getSalary(), font));
                        }
                    }
                    i++;
                    document.add(new Paragraph("\n"));
                    document.add(new Paragraph("                                                                      "+marg + "Всего: " + Math.ceil(sumSal) + " ₴", font));
                }
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void editEmployee(){
        main.showEditEmployeeWindow(tree_table.getSelectionModel().getSelectedItem(), main);
        updateTableEmployees();
    }

    /**
     * Инициализация таблицы.
     */
    private void preInit(){
        main.getDatabase().initializeEmployees(main);

        col_name.setCellValueFactory(cell->cell.getValue().nameProperty());
        col_selo.setCellValueFactory(cell->cell.getValue().seloProperty());
        col_amount_box.setCellValueFactory(cell->cell.getValue().amount_boxProperty());
        col_clean_kg.setCellValueFactory(cell->cell.getValue().clean_kgProperty());
        col_salary.setCellValueFactory(cell->cell.getValue().salaryProperty());
        col_date.setCellValueFactory(cell->cell.getValue().dateProperty());
        tree_table.setItems(main.getCollections().getEmployees());

        main.getDatabase().initializeSelo(main);
    }

    private void initListeners(){
        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");

        main.getCollections().getEmployees().addListener((ListChangeListener<Employee>) c -> updateCountLabelEmployee());

        cmd_box_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) cmd_box_field.setText(oldValue);
        });

        tree_table.setOnMouseClicked(event -> {
            employee = tree_table.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 2){
                if (employee != null) {
                    try {
                        main.showEditEmployeeWindow(tree_table.getSelectionModel().getSelectedItem(), main);
                        updateTableEmployees();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (employee != null) {
                    updateTableBox();
                    setTextFieldEmployee(employee);
                }
            }
        });
    }

    private void updateTableEmployees(){
        main.getCollections().clearEmployees();
        main.getCollections().clearSeloList();
        preInit();
        tree_table.refresh();
    }

    private void updateTableBox(){
        table_box_kg.refresh();
        main.getCollections().getBoxes().clear();

        employee = tree_table.getSelectionModel().getSelectedItem();

        main.getDatabase().updateBoxes(employee, main);
        col_nb_box.setCellValueFactory(cell->cell.getValue().count_boxProperty());
        col_kg.setCellValueFactory(cell->cell.getValue().kilogramProperty());
        table_box_kg.setItems(main.getCollections().getBoxes());
    }

    private void updateCountLabelEmployee(){
        labelCount.setText("Кол-во записей: " + main.getCollections().getEmployees().size());
    }


    /**
     * При выбре рабочего, заполняем текстовые поля
     */
    private void setTextFieldEmployee (Employee employee){
        cmd_name_emp.setText(employee.getName());
        cmd_selo_emp.setText(employee.getSelo());
    }

    /**
     * Поиск по Имени, Дате, Селу
     */
    private void actionSearch(){
        FilteredList<Employee> filteredList = new FilteredList<>(main.getCollections().getEmployees(), p->true);

        cmd_search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteredList.setPredicate((Employee employee) -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (employee.getName().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (employee.getSelo().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (employee.getDate().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
            });
        });

        SortedList<Employee> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tree_table.comparatorProperty());
        tree_table.setItems(sortedList);
    }

    private boolean checkValues(){
        if (cmd_name_emp.getText().trim().length() == 0 || cmd_selo_emp.getText().trim().length() == 0){
            DialogManager.showErrorDialog("Ошибка!", "Одно или несколько полей пустое! Заполните поля!");
            return false;
        }
        return true;
    }

    private boolean checkValuesBoxKg(){
        if (cmd_box_field.getText().trim().length() == 0 || Double.parseDouble(cmd_box_field.getText()) < 0){
            DialogManager.showErrorDialog("Ошибка!", "Введите корректное значение!");
            return true;
        }
        return false;
    }

    private boolean employeeIsSelected(Employee selectedEmployee) {
        if (selectedEmployee == null){
            DialogManager.showErrorDialog("Ошибка!", "Выберите рабочего!");
            return false;
        }
        return true;
    }

    private boolean boxIsSelected (Box selectedBox){
        if (selectedBox == null){
            DialogManager.showErrorDialog("Ошибка!", "Выберите ящик!");
            return true;
        }
        return false;
    }

    public void setIdParameter(int id_parameter){
        this.id_parameter = id_parameter;
    }

}
