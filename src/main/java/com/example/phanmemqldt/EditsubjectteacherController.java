package com.example.phanmemqldt;

import com.example.phanmemqldt.data.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.stage.*;

import java.sql.*;

public class EditsubjectteacherController {

    @FXML
    private TableColumn<Subjectteacher, String> subjectname_col;

    @FXML
    private TableColumn<Subjectteacher, String> teacherid_col;

    @FXML
    private TableColumn<Subjectteacher, String> teachername_col;
    @FXML
    private TableView<Subjectteacher> Tablesubjectteacher;

    public void saveNewTeacher(Subjectteacher sjt, String classid, Subjectteacher newsubjectteacher) {
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                if (sjt.getTeacherid() != null) {
                    String sql = "UPDATE  teachersubjects SET teacherid = ?, subjectid = ?, classid = ? WHERE teacherid = ? AND subjectid = ? AND classid = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newsubjectteacher.getTeacherid());
                    preparedStatement.setInt(2, newsubjectteacher.getSubjectid());
                    preparedStatement.setString(3, classid);
                    preparedStatement.setString(4, sjt.getTeacherid());
                    preparedStatement.setInt(5, sjt.getSubjectid());
                    preparedStatement.setString(6, classid);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Phân công giáo viên thành công!");
                        LoginController.showSuccessMessage("thành công", "Phân công giáo viên thành công");
                    } else {
                        System.out.println("Phân giáo viên thất bại!");
                    }
                } else {
                    String sql = "INSERT INTO  teachersubjects(teacherid, subjectid, classid) VALUES(?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newsubjectteacher.getTeacherid());
                    preparedStatement.setInt(2, newsubjectteacher.getSubjectid());
                    preparedStatement.setString(3, classid);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Phân công giáo viên thành công!");
                        LoginController.showSuccessMessage("thành công", "Phân công giáo viên thành công");
                    } else {
                        System.out.println("Phân giáo viên thất bại!");
                    }
                }
                connection.close();
                Stage stage = (Stage) Tablesubjectteacher.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void edit(Subjectteacher sjt, String classid) {
        subjectname_col.setCellValueFactory(new PropertyValueFactory<>("subjectname"));
        teacherid_col.setCellValueFactory(new PropertyValueFactory<>("teacherid"));
        teachername_col.setCellValueFactory(new PropertyValueFactory<>("teachername"));
        ObservableList<Subjectteacher> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                System.out.println(sjt.getTeacherid());
                if (sjt.getTeacherid() != null) {
                    String sql1 = sjt.getTeacherid();
                    String sql = "SELECT DISTINCT  T.teacherid, T.teachername\n" +
                            "FROM teachers T\n" +
                            "JOIN teachersubjects TS ON T.teacherid = TS.teacherid\n" +
                            "WHERE TS.subjectid = " + sjt.getSubjectid() + " AND TS.teacherid NOT LIKE " + sql1;
                    Statement statement = connection.createStatement();

                    ResultSet rs = statement.executeQuery(sql);
                    while (rs.next()) {
                        Subjectteacher sjtt = new Subjectteacher();
                        sjtt.setTeacherid(rs.getString(1));
                        sjtt.setTeachername(rs.getString(2));
                        sjtt.setSubjectid(sjt.getSubjectid());
                        sjtt.setSubjectname(sjt.getSubjectname());
                        filteredList.add(sjtt);
                    }
                } else {
                    String sql = "SELECT DISTINCT  T.teacherid, T.teachername\n" +
                            "FROM teachers T\n" +
                            "JOIN teachersubjects TS ON T.teacherid = TS.teacherid\n" +
                            "WHERE TS.subjectid = " + sjt.getSubjectid();
                    Statement statement = connection.createStatement();

                    ResultSet rs = statement.executeQuery(sql);
                    while (rs.next()) {
                        Subjectteacher sjtt = new Subjectteacher();
                        sjtt.setTeacherid(rs.getString(1));
                        sjtt.setTeachername(rs.getString(2));
                        sjtt.setSubjectid(sjt.getSubjectid());
                        sjtt.setSubjectname(sjt.getSubjectname());
                        filteredList.add(sjtt);
                    }
                }
                Tablesubjectteacher.setItems(filteredList);
                ContextMenu contextMenu = new ContextMenu();
                MenuItem editMenuItem = new MenuItem("Phân công");
                contextMenu.getItems().addAll(editMenuItem);
                Tablesubjectteacher.setContextMenu(contextMenu);
                editMenuItem.setOnAction(event -> {
                    Subjectteacher newsubjecteacher = Tablesubjectteacher.getSelectionModel().getSelectedItem();
                    if (newsubjecteacher != null)
                        saveNewTeacher(sjt, classid, newsubjecteacher);
                });

                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
