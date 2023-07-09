package com.example.phanmemqldt;

import com.example.phanmemqldt.data.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

import java.sql.*;
import java.util.*;

public class EditstudentgradechildController {

    @FXML
    private ComboBox<Double> NonespecialBox;

    @FXML
    private TextField Specialtf;

    @FXML
    private Button SaveBtn;

    public int getGradetypeId(String gradetypename) {
        int id = 0;
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT gradetypeid FROM gradetypes WHERE gradetypename = '" + gradetypename + "'";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public int getSubjectid(String subjectname) {
        int id = 0;
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT subjectid FROM subjects WHERE subjectname = '" + subjectname + "'";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) {
                    id = rs.getInt(1);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public void setNonespecialBox() {
        double startValue = 0.0;
        double endValue = 10.0;
        double step = 0.5;

        for (double value = startValue; value <= endValue; value += step) {
            NonespecialBox.getItems().add(value);
        }
    }

    public void saveNewGrade(Grade grade, Student selectedstudent, String semesterid, String subjectname, String editft, String gradetypename, String isspecial) {
        Connection connection = database.connectDb();
        if (editft.equals("edit")) {
            if (connection != null) {
                try {
                    String value = "";
                    if (isspecial.equals("false"))
                        value = String.valueOf(NonespecialBox.getValue());
                    else
                        value = Specialtf.getText();
                    String sql = "UPDATE grades SET gradevalue = " + "'" + value + "' WHERE gradeid = " + grade.getGradeid();
                    Statement statement = connection.createStatement();
                    int rowsInserted = statement.executeUpdate(sql);
                    if (rowsInserted > 0) {
                        System.out.println("sửa điểm thành công");
                        LoginController.showSuccessMessage("Thành công", "sửa điểm thành công");
                    } else {
                        System.out.println("Sửa điểm thất bại!");
                    }
                    Stage stage = (Stage) SaveBtn.getScene().getWindow();
                    stage.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (editft.equals("add")) {
            if (connection != null) {
                try {
                    Random random = new Random();
                    int gradeid = random.nextInt(100000000);
                    String gradeidd = String.format("%07d", gradeid);
                    String checkQuery = "SELECT gradeid FROM grades WHERE gradeid = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                    checkStatement.setString(1, gradeidd);
                    ResultSet resultSet = checkStatement.executeQuery();
                    // Nếu gradeid đã tồn tại, sinh lại một số và kiểm tra lại
                    while (resultSet.next()) {
                        gradeid = random.nextInt(100000000);
                        gradeidd = String.format("%07d", gradeid);
                        checkStatement.setString(1, gradeidd);
                        resultSet = checkStatement.executeQuery();
                    }
                    String value = "";
                    String sql = "";
                    if (isspecial.equals("false")) {
                        value = String.valueOf(NonespecialBox.getValue());
                        sql = "INSERT INTO grades(gradeid, studentid, subjectid, semesterid, gradetypeid, gradevalue) VALUES(?, ?, ?, ?, ?, ?)";
                    } else {
                        value = Specialtf.getText();
                        sql = "INSERT INTO grades(gradeid, studentid, subjectid, semesterid, gradetypeid, gradevalue) VALUES(?, ?, ?, ?, ?, ?)";
                    }
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, gradeidd);
                    preparedStatement.setString(2, selectedstudent.getStudentid());
                    preparedStatement.setInt(3, getSubjectid(subjectname));
                    preparedStatement.setString(4, semesterid);
                    preparedStatement.setInt(5, getGradetypeId(gradetypename));
                    preparedStatement.setString(6, value);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Thêm điểm thành công");
                        LoginController.showSuccessMessage("Thành công", "Thêm điểm thành công");
                    } else {
                        System.out.println("Thêm điểm thất bại!");
                    }
                    Stage stage = (Stage) SaveBtn.getScene().getWindow();
                    stage.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void chooseGradetype(Grade grade, Student selectedstudent, String semesterid, String subjectname, String editft, String gradetypename) {
        Connection connection = database.connectDb();
        if (editft.equals("edit")) {
            if (connection != null) {
                try {
                    String sql = "SELECT  G.gradeid ,S.studentid,SS.semesterid,S.studentname,Sub.subjectid, Sub.subjectname, Sub.special, GT.gradetypename, classes.classid,classes.classname,G.gradevalue\n" +
                            "FROM grades G\n" +
                            "JOIN gradetypes GT ON G.gradetypeid = GT.gradetypeid\n" +
                            "JOIN students S ON G.studentid = S.studentid\n" +
                            "JOIN subjects Sub ON G.subjectid = Sub.subjectid\n" +
                            "JOIN classes ON S.classid = classes.classid\n" +
                            "JOIN semesters SS ON SS.semesterid = G.semesterid\n" +
                            "WHERE S.studentid = " + selectedstudent.getStudentid() + " AND Sub.subjectname = " + "'" + subjectname + "' AND GT.gradetypename = " + "'" + gradetypename + "' AND SS.semesterid = " + semesterid + " AND G.gradeid = " + grade.getGradeid() + " order by S.studentid;";
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    if (rs.next()) {
                        if (rs.getString(7).equals("false")) {
                            String isspecial = rs.getString(7);
                            System.out.println(isspecial);
                            setNonespecialBox();
                            NonespecialBox.setVisible(true);
                            NonespecialBox.setValue(Double.parseDouble(rs.getString(11)));
                            Specialtf.setVisible(false);
                            SaveBtn.setOnAction(event -> {
                                if (NonespecialBox.getValue() != null) {
                                    try {
                                        saveNewGrade(grade, selectedstudent, semesterid, subjectname, editft, gradetypename, isspecial);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else {
                            String isspecial = rs.getString(7);
                            System.out.println(isspecial);
                            NonespecialBox.setVisible(false);
                            Specialtf.setVisible(true);
                            Specialtf.setText(rs.getString(11));
                            SaveBtn.setOnAction(event -> {
                                if (!Specialtf.getText().isEmpty()) {
                                    try {
                                        saveNewGrade(grade, selectedstudent, semesterid, subjectname, editft, gradetypename, isspecial);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    }
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (editft.equals("add")) {
            if (connection != null) {
                try {
                    String sql = "SELECT special FROM subjects WHERE subjectname = '" + subjectname + "'";
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    if (rs.next()) {
                        if (rs.getString(1).equals("false")) {
                            String isspecial = rs.getString(1);
                            System.out.println(isspecial);
                            setNonespecialBox();
                            NonespecialBox.setVisible(true);
                            NonespecialBox.setValue(0.0);
                            Specialtf.setVisible(false);
                            SaveBtn.setOnAction(event -> {
                                if (NonespecialBox.getValue() != null) {
                                    try {
                                        saveNewGrade(grade, selectedstudent, semesterid, subjectname, editft, gradetypename, isspecial);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else {
                            String isspecial = rs.getString(1);
                            System.out.println(isspecial);
                            NonespecialBox.setVisible(false);
                            Specialtf.setVisible(true);
                            Specialtf.setText("Đạt");
                            SaveBtn.setOnAction(event -> {
                                if (!Specialtf.getText().isEmpty()) {
                                    try {
                                        saveNewGrade(grade, selectedstudent, semesterid, subjectname, editft, gradetypename, isspecial);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    }
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void edit(Grade grade, Student selectedstudent, String semesterid, String subjectname, String editft, String gradetypename) {
        if (editft.equals("edit")) {
            chooseGradetype(grade, selectedstudent, semesterid, subjectname, editft, gradetypename);
        } else if (editft.equals("add")) {
            chooseGradetype(grade, selectedstudent, semesterid, subjectname, editft, gradetypename);
        }
    }

}
