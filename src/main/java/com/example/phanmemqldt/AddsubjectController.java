package com.example.phanmemqldt;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

import java.sql.*;

public class AddsubjectController {

    @FXML
    private TextField Nametf;

    @FXML
    private ComboBox<String> TypegradeBox;
    @FXML
    private Button SaveBtn;


    public void saveNewSubject() {
        int numbersubject = 0;
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String query = "SELECT COUNT(*) FROM subjects";

                // Tạo đối tượng Statement
                Statement statement = connection.createStatement();

                // Thực thi câu truy vấn và lấy kết quả
                ResultSet resultSet = statement.executeQuery(query);

                // Di chuyển tới hàng đầu tiên của kết quả
                resultSet.next();

                // Lấy giá trị số lượng hàng từ cột đầu tiên (ở vị trí 1 trong SQL)
                numbersubject = resultSet.getInt(1);

                // In ra số lượng hàng
                System.out.println("Số lượng hàng: " + numbersubject);
                if (Nametf.getText().isEmpty() || TypegradeBox.getValue() == null) {
                    LoginController.showErrorMessage("Lỗi", "Vui lòng nhập đầy đủ thông tin!");
                } else {
                    String sql1 = "INSERT INTO subjects(subjectid, subjectname, special) VALUES(?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql1);
                    preparedStatement.setInt(1, numbersubject + 1);
                    preparedStatement.setString(2, Nametf.getText());
                    preparedStatement.setString(3, TypegradeBox.getValue().equals("Số") ? "false" : "true");
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Thêm học thành công!");
                        LoginController.showSuccessMessage("thành công", "Thêm môn học thành công");
                    } else {
                        System.out.println("Thêm môn thất bại!");
                    }
                    Stage stage = (Stage) SaveBtn.getScene().getWindow();
                    stage.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void addSubject() {
        TypegradeBox.getItems().addAll("Số", "Chữ");
        SaveBtn.setOnAction(event -> {
            saveNewSubject();
        });

    }


}
