package com.example.phanmemqldt;

import com.example.phanmemqldt.data.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

import java.sql.*;

public class EditsubjectController {

    @FXML
    private TextField Subjectnametf;
    @FXML
    private Button SaveBtn;

    public void Save(Subject selectedsubject) {
        if (Subjectnametf.getText().isEmpty()) {
            LoginController.showErrorMessage("Lỗi", "Vui lòng nhập đủ thông tin");
        } else {
            Connection connection = database.connectDb();
            if (connection != null) {
                try {
                    String update = "UPDATE subjects SET subjectname = ? WHERE subjectid = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(update);
                    preparedStatement.setString(1, Subjectnametf.getText());
                    preparedStatement.setInt(2, selectedsubject.getSubjectid());

                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Sửa môn học thành công!");
                        LoginController.showSuccessMessage("thành công", "Sửa môn học thành công");
                    } else {
                        System.out.println("Sửa môn thất bại!");
                    }
                    Stage stage = (Stage) SaveBtn.getScene().getWindow();
                    stage.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void edit(Subject selectedsubject) {
        Subjectnametf.setText(selectedsubject.getSubjectname());
        SaveBtn.setOnAction(event -> {
            Save(selectedsubject);
        });
    }
}
