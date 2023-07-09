package com.example.phanmemqldt;

import com.example.phanmemqldt.data.*;
import com.example.phanmemqldt.data.Class;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.stage.*;

import java.sql.*;


public class EdithomeroomteacherController {

    @FXML
    private TableView<Teacher> Tableteacher;

    @FXML
    private TableColumn<Teacher, String> teacherid_col;

    @FXML
    private TableColumn<Teacher, String> teachername_col;

    public void saveNewHomeRoomTeacher(Class c, String classid, Teacher newteacher) {
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                if (c != null) {
                    String sql = "UPDATE  classes SET idhomeroomteacher = ? WHERE classid = ? AND idhomeroomteacher = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newteacher.getTeacherid());
                    preparedStatement.setString(2, classid);
                    preparedStatement.setString(3, c.getIdhomeroomteacher());

                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Phân công giáo viên chủ nhiệm thành công!");
                        LoginController.showSuccessMessage("thành công", "Phân công giáo viên chủ nhiệm thành công!");
                    } else {
                        System.out.println("Phân giáo viên chủ nhiệmthất bại!");
                    }
                } else {
                    String sql = "UPDATE  classes SET idhomeroomteacher = ? WHERE classid = ? ";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newteacher.getTeacherid());
                    preparedStatement.setString(2, classid);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Phân công giáo viên chủ nhiệm thành công!");
                        LoginController.showSuccessMessage("thành công", "Phân công giáo viên chủ nhiệm thành công!");
                    } else {
                        System.out.println("Phân giáo viên chủ nhiệmthất bại!");
                    }
                }
                Stage stage = (Stage) Tableteacher.getScene().getWindow();
                stage.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void edit(Class c, String classid) {
        Connection connection = database.connectDb();
        ObservableList<Teacher> filteredList = FXCollections.observableArrayList();
        teacherid_col.setCellValueFactory(new PropertyValueFactory<>("teacherid"));
        teachername_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (connection != null) {
            try {
                String sql = "";
                if (c == null)
                    sql = "SELECT teacherid, teachername FROM teachers WHERE teacherid NOT LIKE 1";
                else
                    sql = "SELECT teacherid, teachername FROM teachers WHERE teacherid NOT LIKE " + c.getIdhomeroomteacher() + " AND teacherid NOT LIKE 1";
                Statement statement = connection.createStatement();

                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    Teacher t = new Teacher();
                    t.setTeacherid(rs.getString(1));
                    t.setName(rs.getString(2));
                    filteredList.add(t);
                }
                Tableteacher.setItems(filteredList);
                ContextMenu contextMenu = new ContextMenu();
                MenuItem editMenuItem = new MenuItem("Phân công");
                contextMenu.getItems().addAll(editMenuItem);
                Tableteacher.setContextMenu(contextMenu);
                editMenuItem.setOnAction(event -> {
                    Teacher t = Tableteacher.getSelectionModel().getSelectedItem();
                    if (t != null)
                        saveNewHomeRoomTeacher(c, classid, t);
                });
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
