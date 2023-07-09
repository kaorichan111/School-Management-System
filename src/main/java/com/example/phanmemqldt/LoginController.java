package com.example.phanmemqldt;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.sql.*;
import java.text.*;
import java.util.*;

public class LoginController {

    @FXML
    private AnchorPane leftloginform;

    @FXML
    private Button CloseBtn;

    @FXML
    private Button LoginBtn;

    @FXML
    private TextField UserNameTf;

    @FXML
    private PasswordField PasswordTf;

    public void close() {
        System.exit(0);
    }

    public static void showErrorMessage(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void showSuccessMessage(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setNewScene(String role) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(role + ".fxml"));
        Stage stage = new Stage();
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Hệ thống quản lý trường thcs");
            if (role.equals("admin")) {
                AdminController adminController = fxmlLoader.getController();
                adminController.initTable();

            } else {
                TeacherController teacherController = fxmlLoader.getController();
                teacherController.initTable(UserNameTf.getText());
            }
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void formatNumber(TextField textField, NumberFormat numberFormat) {
        String text = textField.getText();
        try {
            // Xóa tất cả các ký tự không phải số
            text = text.replaceAll("[^\\d]", "");

            // Chuyển đổi chuỗi thành số
            long number = Long.parseLong(text);

            // Định dạng số
            String formattedNumber = numberFormat.format(number);

            // Cập nhật lại giá trị trong TextField
            textField.setText(formattedNumber);
        } catch (NumberFormatException e) {
            // Xử lý ngoại lệ nếu không thể chuyển đổi thành số
            textField.setText("");

        }
    }

    public static String initUserName(String fullName, String id) {
        String[] names = fullName.split(" "); // Split the full name into individual names
        String lastName = names[names.length - 1]; // Extract the last name


        Random random = new Random();
        String s = "";
        for (int i = 0; i < names.length - 1; i++) {
//            String id = String.valueOf(random.nextInt(99999) + 100000);
            String firstName = names[i];
            s += (names[i].charAt(0)); // Get the first letter of each first name

        }

        return lastName + s + id;
    }

    public void loginBtn() {
        Connection connect = database.connectDb();
        if (connect != null) {
            try {
                String sql = "SELECT username, password, role FROM teachers WHERE username = ? AND password = ?";
                PreparedStatement statement = connect.prepareStatement(sql);
                statement.setString(1, UserNameTf.getText());
                statement.setString(2, PasswordTf.getText());
                ResultSet rs = statement.executeQuery();
                UserNameTf.setText("tubl57116"); // môn bt
                PasswordTf.setText("123");
////                UserNameTf.setText("vinhhn20548"); // môn đặc biệt
//////                PasswordTf.setText("123");
//                UserNameTf.setText("admin"); // môn đặc biệt
//                PasswordTf.setText("123");

                if (UserNameTf.getText().isEmpty() || PasswordTf.getText().isEmpty()) {
                    showErrorMessage("Lỗi", "Vui lòng nhập đầy đủ thông tin.");
                } else if (rs.next()) {
                    showSuccessMessage("Thành công", "Đăng nhập thành công!");
                    System.out.println(rs.getString(3));
                    UserNameTf.getScene().getWindow().hide();
                    setNewScene(rs.getString(3));


                } else {
                    showErrorMessage("Lỗi", "Tên đăng nhập hoặc mật khẩu không đúng.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}