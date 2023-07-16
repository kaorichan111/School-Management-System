package com.example.phanmemqldt;

import com.example.phanmemqldt.data.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.util.*;
import javafx.util.converter.*;

import java.sql.*;
import java.text.*;
import java.time.*;

public class EditteacherController {

    @FXML
    private TextField Addresstf;

    @FXML
    private DatePicker BirthdayBox;

    @FXML
    private ComboBox<String> GenderBox;

    @FXML
    private TextField Nametf;

    @FXML
    private TextField Salarytf;

    @FXML
    private TextField Workyeartf;
    @FXML
    private Button Savebtn;

    public void saveinfor(Teacher selectedteacher) {
        if (Nametf.getText().isEmpty() ||
                GenderBox.getValue() == null ||
                BirthdayBox.getValue() == null ||
                Addresstf.getText().isEmpty() ||
                Workyeartf.getText().isEmpty() ||
                Salarytf.getText().isEmpty()
        ) {
            LoginController.showErrorMessage("Lỗi", "Vui lòng nhập đầy đủ thông tin");
        } else if (AdminController.containsDigitOrSpecialCharacter(Nametf.getText())) {
            LoginController.showErrorMessage("Lỗi", "Tên không dược chứa chữ số hay kí tự đặc biệt");
            System.out.println("Tên không dược chứa chữ số hay kí hiệu đặc biệt");
        } else if (AdminController.containsLetterOrSpecialCharacter(Workyeartf.getText())) {
            LoginController.showErrorMessage("Lỗi", "Năm làm việc không dc chứa chữ số hay kí tự đặc biệt");
            System.out.println("Năm làm việc không dc chứa chữ số hay kí tự đặc biệt");
        } else {
            String goodname = Person.normalizeName(Nametf.getText());
            selectedteacher.setName(goodname);
            selectedteacher.setVietnamesename();
            Connection connection = database.connectDb();
            if (connection != null) {
                try {
                    String update = "UPDATE teachers SET teachername = ?, gender = ?, birthday = ?, address = ?, workyear = ?, salary = ?, username = ?, role = ? WHERE teacherid = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(update);
                    preparedStatement.setString(1, goodname);
                    preparedStatement.setString(2, GenderBox.getValue());
                    preparedStatement.setString(3, BirthdayBox.getValue().toString());
                    preparedStatement.setString(4, Addresstf.getText());
                    preparedStatement.setInt(5, Integer.parseInt(Workyeartf.getText()));
                    String strippedNumberString = Salarytf.getText().replace(",", "");
                    preparedStatement.setString(6, strippedNumberString);
                    preparedStatement.setString(7, LoginController.initUserName(selectedteacher.getVietnamesename(), selectedteacher.getTeacherid()));
                    preparedStatement.setString(8, "teacher");
                    preparedStatement.setString(9, selectedteacher.getTeacherid());
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Sửa giáo viên thành công!");
                        LoginController.showSuccessMessage("thành công", "Sửa giáo viên thành công");
                    } else {
                        System.out.println("Sửa giáo viên thất bại!");
                    }
                    Stage stage = (Stage) Savebtn.getScene().getWindow();
                    stage.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void edit(Teacher selectedteacher) {
        Nametf.setText(selectedteacher.getName());
        GenderBox.getItems().addAll("Nam", "Nữ");
        GenderBox.setValue(selectedteacher.getGender());
        BirthdayBox.setValue(LocalDate.parse(selectedteacher.getBirthday()));
        Addresstf.setText(selectedteacher.getAddress());
        Workyeartf.setText(String.valueOf(selectedteacher.getWorkYear()));
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        // Thiết lập định dạng hiển thị số nguyên lớn cho TextField
        StringConverter<Number> converter = new NumberStringConverter(numberFormat);
        Salarytf.setTextFormatter(new TextFormatter<>(converter));
        Salarytf.setOnKeyTyped(event -> {
            LoginController.formatNumber(Salarytf, numberFormat);
        });
        Salarytf.setText(selectedteacher.getFormatSalary());

        Savebtn.setOnAction(event -> {
            saveinfor(selectedteacher);
        });

    }

}
