package com.example.phanmemqldt;

import com.example.phanmemqldt.data.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

import java.sql.*;
import java.time.*;
import java.util.*;

public class EditstudenthomeroominforController {

    @FXML
    private TextField Addresstf;

    @FXML
    private DatePicker BirthdayBox;

    @FXML
    private ComboBox<String> ConductBox;

    @FXML
    private TextField Fathernametf;

    @FXML
    private TextField Fatherphonetf;

    @FXML
    private ComboBox<String> GenderBox;

    @FXML
    private TextField Mothernametf;

    @FXML
    private TextField Motherphonetf;

    @FXML
    private TextField Nametf;

    @FXML
    private Button SaveBtn;

    @FXML
    private TextField Absentf;

    public String getClassID(String classname) {
        String classid = "";
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT classid FROM classes WHERE classname = '" + classname + "'";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) {
                    classid = rs.getString(1);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return classid;
    }

    public void saveStudentHomeRoomInfor(Student selectedstudent, String edittf, String classname) {
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                if (Nametf.getText().isEmpty() ||
                        GenderBox.getValue() == null ||
                        BirthdayBox.getValue() == null ||
                        Addresstf.getText().isEmpty() ||
                        Fathernametf.getText().isEmpty() ||
                        Mothernametf.getText().isEmpty() ||
                        Fatherphonetf.getText().isEmpty() ||
                        Motherphonetf.getText().isEmpty() ||
                        ConductBox.getValue() == null ||
                        Absentf.getText().isEmpty()) {
                    LoginController.showErrorMessage("Lỗi", "Vui lòng nhập đủ thông tin");
                } else if (AdminController.containsDigitOrSpecialCharacter(Nametf.getText()) ||
                        AdminController.containsDigitOrSpecialCharacter(Fathernametf.getText()) ||
                        AdminController.containsDigitOrSpecialCharacter(Mothernametf.getText()) ||
                        AdminController.containsDigitOrSpecialCharacter(Fathernametf.getText())
                ) {
                    LoginController.showErrorMessage("Lỗi", "Tên không được chứa chữ số hay kí tự đặc biệt");
                } else if (AdminController.containsLetterOrSpecialCharacter(Absentf.getText())) {
                    LoginController.showErrorMessage("Lỗi", "Số buổi vắng chỉ chứa chữ số");
                } else if (AdminController.containsLetterOrSpecialCharacter(Fatherphonetf.getText()) ||
                        AdminController.containsLetterOrSpecialCharacter(Motherphonetf.getText())
                ) {
                    LoginController.showErrorMessage("Lỗi", "Số điện thoại chỉ chứa chữ số");
                } else {
                    if (edittf.equals("edit")) {
                        String sql = "UPDATE students SET studentname = ?, gender = ?, birthday = ?, address = ?, fathername = ?, mothername = ?, fatherphonenumber = ?, motherphonenumber = ?, conduct = ?, absent = ? WHERE studentid = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, Nametf.getText());
                        preparedStatement.setString(2, GenderBox.getValue().equals("Nam") ? "male" : "female");
                        preparedStatement.setString(3, BirthdayBox.getValue().toString());
                        preparedStatement.setString(4, Addresstf.getText());
                        preparedStatement.setString(5, Fathernametf.getText());
                        preparedStatement.setString(6, Mothernametf.getText());
                        preparedStatement.setString(7, Fatherphonetf.getText());
                        preparedStatement.setString(8, Motherphonetf.getText());
                        String conduct = "";
                        if (ConductBox.getValue().equals("Tốt"))
                            conduct = "T";
                        else if (ConductBox.getValue().equals("Khá"))
                            conduct = "K";
                        else
                            conduct = "TB";
                        preparedStatement.setString(9, conduct);
                        preparedStatement.setString(10, Absentf.getText());
                        preparedStatement.setString(11, selectedstudent.getStudentid());
                        int rowsInserted = preparedStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Sửa thông tin học sinh thành công!");
                            LoginController.showSuccessMessage("thành công", "Sửa thông tin học sinh thành công!");
                        } else {
                            System.out.println("Sửa thông tin học sinh thất bại!");
                        }
                        Stage stage = (Stage) SaveBtn.getScene().getWindow();
                        stage.close();
                    } else {
                        String classid = getClassID(classname);
                        Random random = new Random();
                        int generatedId = random.nextInt(99999 - 10000 + 1) + 10000;
                        String id = String.valueOf(generatedId);
                        // Kiểm tra xem id đã tồn tại trong bảng "teacher" chưa
                        String checkQuery = "SELECT studentid FROM students WHERE studentid = ?";
                        PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                        checkStatement.setString(1, id);
                        ResultSet resultSet = checkStatement.executeQuery();
                        // Nếu id đã tồn tại, sinh lại một số và kiểm tra lại
                        while (resultSet.next()) {
                            generatedId = random.nextInt(90000) + 10000;
                            id = String.valueOf(generatedId);
                            checkStatement.setString(1, id);
                            resultSet = checkStatement.executeQuery();
                        }
                        String sql = "INSERT INTO students(studentid, studentname, gender, birthday, address, classid, fathername, mothername, fatherphonenumber, motherphonenumber, conduct, absent) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, id);
                        preparedStatement.setString(2, Nametf.getText());
                        preparedStatement.setString(3, GenderBox.getValue().equals("Nam") ? "male" : "female");
                        preparedStatement.setString(4, BirthdayBox.getValue().toString());
                        preparedStatement.setString(5, Addresstf.getText());
                        preparedStatement.setString(6, classid);
                        preparedStatement.setString(7, Fathernametf.getText());
                        preparedStatement.setString(8, Mothernametf.getText());
                        preparedStatement.setString(9, Fatherphonetf.getText());
                        preparedStatement.setString(10, Motherphonetf.getText());
                        String conduct = "";
                        if (ConductBox.getValue().equals("Tốt"))
                            conduct = "T";
                        else if (ConductBox.getValue().equals("Khá"))
                            conduct = "K";
                        else
                            conduct = "TB";
                        preparedStatement.setString(11, conduct);
                        preparedStatement.setString(12, Absentf.getText());
                        int rowsInserted = preparedStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Thêm học sinh thành công!");
                            LoginController.showSuccessMessage("thành công", "Thêm thông tin học sinh thành công!");
                        } else {
                            System.out.println("Thêm thông tin học sinh thất bại!");
                        }
                        Stage stage = (Stage) SaveBtn.getScene().getWindow();
                        stage.close();
                    }
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void edit(Student selectedstudent, String edittf, String classname) {
        GenderBox.getItems().addAll("Nam", "Nữ");
        ConductBox.getItems().addAll("Tốt", "Khá", "Trung bình");
        if (edittf.equals("edit")) {
            Nametf.setText(selectedstudent.getName());
            GenderBox.setValue(selectedstudent.getGender());
            BirthdayBox.setValue(LocalDate.parse(selectedstudent.getBirthday()));
            Addresstf.setText(selectedstudent.getAddress());
            Fathernametf.setText(selectedstudent.getFathername());
            Mothernametf.setText(selectedstudent.getMothername());
            Fatherphonetf.setText(selectedstudent.getFatherphone());
            Motherphonetf.setText(selectedstudent.getMotherphone());
            ConductBox.setValue(selectedstudent.getConduct());
            Absentf.setText(String.valueOf(selectedstudent.getAbsent()));
        }
        SaveBtn.setOnAction(event -> {
            saveStudentHomeRoomInfor(selectedstudent, edittf, classname);
        });

    }

}
