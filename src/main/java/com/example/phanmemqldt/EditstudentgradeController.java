package com.example.phanmemqldt;

import com.example.phanmemqldt.data.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.stage.*;

import java.sql.*;
import java.util.*;

public class EditstudentgradeController {

    @FXML
    private ComboBox<String> GradetypeBox;

    @FXML
    private TableView<Grade> TableGrade;

    @FXML
    private TableColumn<Grade, String> gradeid_col;
    @FXML
    private Button Savebtn;


    public ObservableList<Grade> getStudentGradeValue(Student selectedstudent, String semesterid, String subjectname) {
        ObservableList<Grade> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT  G.gradeid ,S.studentid,SS.semesterid,S.studentname,Sub.subjectid, Sub.subjectname, Sub.special, GT.gradetypename, classes.classid,classes.classname,G.gradevalue\n" +
                        "FROM grades G\n" +
                        "JOIN gradetypes GT ON G.gradetypeid = GT.gradetypeid\n" +
                        "JOIN students S ON G.studentid = S.studentid\n" +
                        "JOIN subjects Sub ON G.subjectid = Sub.subjectid\n" +
                        "JOIN classes ON S.classid = classes.classid\n" +
                        "JOIN semesters SS ON SS.semesterid = G.semesterid\n" +
                        "WHERE S.studentid = " + selectedstudent.getStudentid() + " AND Sub.subjectname = " + "'" + subjectname + "' AND GT.gradetypename = " + "'" + GradetypeBox.getValue() + "' AND SS.semesterid = " + semesterid + " order by S.studentid;";
                System.out.println(sql);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    Grade g = new Grade();
                    g.setGradeid(rs.getString(1));
                    if (rs.getString(7).equals("false"))
                        g.setValues(Double.parseDouble(rs.getString(11)));
                    else
                        g.setSpecialvalue(rs.getString(11));
                    filteredList.add(g);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filteredList;
    }

    public void updateTableGrade(Student selectedstudent, String semesterid, String subjectname) {
        if (GradetypeBox.getValue() != null) {
            TableGrade.setVisible(true);
            TableGrade.getColumns().subList(1, TableGrade.getColumns().size()).clear();
            TableGrade.getItems().clear();
            gradeid_col.setCellValueFactory(new PropertyValueFactory<>("gradeid"));
            if (selectedstudent.getStudentsubject().getIsspecial().equals("false")) {
                TableColumn<Grade, Double> gradeColumn = new TableColumn<>("Kết quả");
                gradeColumn.setCellValueFactory(new PropertyValueFactory<>("values"));
                TableGrade.getColumns().add(gradeColumn);

            } else {
                TableColumn<Grade, String> gradeColumn = new TableColumn<>("Kết quả");
                gradeColumn.setCellValueFactory(new PropertyValueFactory<>("specialvalue"));
                TableGrade.getColumns().add(gradeColumn);
            }
            TableGrade.setItems(getStudentGradeValue(selectedstudent, semesterid, subjectname));
            for (TableColumn<?, ?> column : TableGrade.getColumns()) {
                column.prefWidthProperty().bind(TableGrade.widthProperty().multiply(0.5)); // Đặt chiều rộng cột là 25% chiều rộng của bảng
            }
        }
    }

    public void editGrade(Grade grade, Student selectedstudent, String semesterid, String subjectname, String editft, String gradetypename) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editstudentgradechild.fxml"));
        Stage stage = new Stage();
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sửa điểm");
            stage.initModality(Modality.APPLICATION_MODAL);
            EditstudentgradechildController editstudentgradechildController = fxmlLoader.getController();
            editstudentgradechildController.edit(grade, selectedstudent, semesterid, subjectname, editft, gradetypename);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTableGrade(selectedstudent, semesterid, subjectname);
    }

    public void deleteGrade(Grade grade, Student selectedstudent, String semesterid, String subjectname, String editft, String gradetypename) {
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "DELETE FROM grades WHERE gradeid = " + grade.getGradeid();
                Statement statement = connection.createStatement();
                int rowsInserted = statement.executeUpdate(sql);
                if (rowsInserted > 0) {
                    System.out.println("Xóa điểm thành công");
                    LoginController.showSuccessMessage("Thành công", "Xóa điểm thành công");
                } else {
                    System.out.println("Xóa điểm thất bại!");
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateTableGrade(selectedstudent, semesterid, subjectname);

    }

    public void edit(Student selectedstudent, String semesterid, String subjectname) {
        TableGrade.setVisible(false);
        Subject sj = selectedstudent.getStudentsubject();
        ArrayList<Gradetype> gradetypes = sj.getGrades();
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (Gradetype gt : gradetypes) {
            filteredList.add(gt.getGradetypename());
        }
        GradetypeBox.setItems(filteredList);
        GradetypeBox.setOnAction(event -> {
            updateTableGrade(selectedstudent, semesterid, subjectname);
        });
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Chỉnh sửa");
        MenuItem addMenuItem = new MenuItem("Nhập thêm");
        MenuItem deleteMenuItem = new MenuItem("Xóa");
        contextMenu.getItems().addAll(editMenuItem, addMenuItem, deleteMenuItem);

        TableGrade.setContextMenu(contextMenu);
        editMenuItem.setOnAction(event -> {
            Grade grade = TableGrade.getSelectionModel().getSelectedItem();
            if (grade != null) {
                String editft = "edit";
                editGrade(grade, selectedstudent, semesterid, subjectname, editft, GradetypeBox.getValue());
            }
        });

        addMenuItem.setOnAction(event -> {
            Grade grade = TableGrade.getSelectionModel().getSelectedItem();
            String isspecial = "";
            Connection connection = database.connectDb();
            if (connection != null) {
                try {
                    String sql = "SELECT special FROM subjects WHERE subjectname = '" + subjectname + "'";
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    if (rs.next()) {
                        isspecial = rs.getString(1);
                    }
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isspecial.equals("true")) {
                if (TableGrade.getItems().isEmpty() && grade == null) {
                    String editft = "add";
                    editGrade(grade, selectedstudent, semesterid, subjectname, editft, GradetypeBox.getValue());
                }
            } else {
                String editft = "add";
                editGrade(grade, selectedstudent, semesterid, subjectname, editft, GradetypeBox.getValue());
            }


        });

        deleteMenuItem.setOnAction(event -> {
            Grade grade = TableGrade.getSelectionModel().getSelectedItem();
            if (grade != null) {
                String editft = "delete";
                deleteGrade(grade, selectedstudent, semesterid, subjectname, editft, GradetypeBox.getValue());
            }
        });

        Savebtn.setOnAction(event -> {
            Stage stage = (Stage) Savebtn.getScene().getWindow();
            LoginController.showSuccessMessage("Thành công", "Lưu điểm thành công");
            stage.close();
        });

    }

}
