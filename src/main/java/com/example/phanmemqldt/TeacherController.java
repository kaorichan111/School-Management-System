package com.example.phanmemqldt;

import com.example.phanmemqldt.data.*;
import com.example.phanmemqldt.data.Class;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.sql.*;
import java.util.*;

public class TeacherController {

    @FXML
    private Button AccountBtn;

    @FXML
    private Button HomeroomclassBtn;

    @FXML
    private Button LineChartBtn;

    @FXML
    private AnchorPane Slider;

    @FXML
    private Button SubjectClassBtn;

    @FXML
    private VBox VBoxAdmin;
    @FXML
    private ComboBox<String> ClassnameBox;
    @FXML
    private ComboBox<String> SubjectnameBox;

    private Teacher teacher = new Teacher();
    @FXML
    private TableView<Student> TableGrade;

    @FXML
    private TableColumn<Student, String> Studentidgrade_col;

    @FXML
    private TableColumn<Student, String> Studentnamegrade_col;
    @FXML
    private ComboBox<String> SemesterBox;

    //lớp chủ nhiệm
    @FXML
    private ComboBox<String> NamehomeroomclassBox;
    @FXML
    private ComboBox<String> SubjectgradehomeroomclassBox;
    @FXML
    private TableView<Student> Tablegradestudenthomeroom;

    @FXML
    private TableColumn<Student, String> Idstudentgradehomeroom_col;
    @FXML
    private TableColumn<Student, String> Namestudentgradehomeroom_col;
    @FXML
    private ComboBox<String> SemestergradehomeroomclassBox;
    @FXML
    private TableView<Student> Tablestudenthomeroominfor;

    public void setSemesterBox() {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT semesterid FROM semesters ";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    filteredList.add(rs.getString(1));
                }
                SemesterBox.setItems(filteredList);
                SemestergradehomeroomclassBox.setItems(filteredList);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public void getTeacherInfor(String username) {
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT * FROM teachers WHERE username = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username);

                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    teacher.setTeacherid(rs.getString(1));
                    teacher.setName(rs.getString(2));
                    teacher.setGender(rs.getString(3));
                    teacher.setBirthday(rs.getString(4));
                    teacher.setAddress(rs.getString(5));
                    teacher.setWorkYear(rs.getInt(6));
                    teacher.setPassword(rs.getString(9));
                }
                //Lấy ra các lớp gv này chủ nhiệm
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getTeacherHomeRoom() {
        Connection connection = database.connectDb();
        if (connection != null) {
            //Lấy ra các lớp gv này chủ nhiệm
            try {
                String sql = "SELECT classid, classname FROM classes WHERE idhomeroomteacher = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, teacher.getTeacherid());

                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    Class c = new Class();
                    c.setClassid(rs.getString(1));
                    c.setClassname(rs.getString(2));
                    teacher.getHomeroomclass().add(c);
                    NamehomeroomclassBox.getItems().add(c.getClassname());
                    System.out.println("Lớp chủ nhiêm : " + c.getClassname());
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void getClassTeacher() {
        //lấy ra các lớp gv này dạy
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT  distinct C.classid, C.classname\n" +
                        "FROM Subjects S\n" +
                        "JOIN TeacherSubjects TS ON S.subjectid = TS.subjectid\n" +
                        "JOIN teachers t ON t.teacherid = TS.teacherid\n" +
                        "JOIN classes C ON TS.classid = C.classid\n" +
                        "WHERE TS.teacherid = " + teacher.getTeacherid() + " order by classid";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    Teachersubjectclasses c = new Teachersubjectclasses();
                    c.setClassid(rs.getString(1));
                    c.setClassname(rs.getString(2));
                    teacher.getTeachersubjectclasses().add(c);
                    System.out.println(c.getClassname());
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getStudyClassTeacher() {
        //lấy ra các lớp gv này dạy
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                if (!teacher.getTeachersubjectclasses().isEmpty()) {
                    for (Teachersubjectclasses ts : teacher.getTeachersubjectclasses()) {
                        String sql = "SELECT * FROM (SELECT  C.classid, C.classname,t.teacherid, t.teachername, S.subjectid, S.subjectname, S.special\n" +
                                "FROM Subjects S\n" +
                                "JOIN TeacherSubjects TS ON S.subjectid = TS.subjectid\n" +
                                "JOIN teachers t ON t.teacherid = TS.teacherid\n" +
                                "JOIN classes C ON TS.classid = C.classid\n" +
                                "WHERE TS.teacherid = " + teacher.getTeacherid() + ") AS result WHERE classid = " + ts.getClassid();
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery(sql);
                        while (rs.next()) {
                            Subject s = new Subject();
                            s.setSubjectid(rs.getInt(5));
                            s.setSubjectname(rs.getString(6));
                            s.setIsspecial(rs.getString(7));
                            System.out.println("Môn giáo viên dạy ở lớp " + ts.getClassname() + " là " + s.getSubjectname());
                            ts.getTeachersubjectteach().add(s);
                        }

                    }
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateSubjectNameBox(String classname) {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        Teachersubjectclasses ts = new Teachersubjectclasses();
        for (Teachersubjectclasses ts1 : teacher.getTeachersubjectclasses()) {
            if (ts1.getClassname().equals(classname)) {
                ts = ts1;
                break;
            }
        }
        for (Subject sj : ts.getTeachersubjectteach()) {
            filteredList.add(sj.getSubjectname());
        }
        SubjectnameBox.setItems(filteredList);
    }

    public void setClassnameBox() {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (Teachersubjectclasses ts : teacher.getTeachersubjectclasses()) {
            filteredList.add(ts.getClassname());
        }
        ClassnameBox.setItems(filteredList);
    }

    public ArrayList<Gradetype> getGradeType(String isspecial) {
        ArrayList<Gradetype> filterlist = new ArrayList<>();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "";
                if (isspecial.equals("false"))
                    sql = "SELECT * FROM gradetypes WHERE gradetypevalue = 'số'";
                else
                    sql = "SELECT * FROM gradetypes WHERE gradetypevalue = 'chữ'";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    Gradetype gradetype = new Gradetype();

                    gradetype.setGradetypeid(rs.getInt(1));
                    gradetype.setGradetypename(rs.getString(2));
                    gradetype.setIndex(rs.getInt(3));
                    gradetype.setGradetypevalue(rs.getString(4));
                    filterlist.add(gradetype);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filterlist;
    }

    public void updateGradeType(Gradetype g, String studentid, int subjectid, String isspecial, ComboBox<String> mysemesterBox) {
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
                        "WHERE S.studentid = " + studentid + " AND Sub.subjectid = " + subjectid + " AND G.gradetypeid = " + g.getGradetypeid() + " AND SS.semesterid = " + mysemesterBox.getValue() + " order by S.studentid;";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    Grade grade = new Grade();
                    grade.setGradeid(rs.getString(1));
                    grade.setSemesterid(rs.getString(3));
                    if (isspecial.equals("false"))
                        grade.setValues(Double.parseDouble(rs.getString(11)));
                    else
                        grade.setSpecialvalue(rs.getString(11));
                    g.getValues().add(grade);
                    System.out.println("Loại điểm : " + g.getGradetypename() + " giá trị : " + grade.getValues());
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<Student> getStudentGradeList(Teachersubjectclasses tsc, Subject s, ComboBox<String> mysemesterbox) {
        ObservableList<Student> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT studentid, studentname FROM remakektpm.students \n" +
                        "JOIN Classes C ON students.classid = C.classid\n" +
                        "WHERE C.classname = " + "'" + tsc.getClassname() + "'";
                System.out.println(sql);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    Student st = new Student();
                    st.setStudentid(rs.getString(1));
                    st.setName(rs.getString(2));
                    st.getStudentsubject().setSubjectid(s.getSubjectid());
                    st.getStudentsubject().setSubjectname(s.getSubjectname());
                    st.getStudentsubject().setIsspecial(s.getIsspecial());
                    st.getStudentsubject().setGrades(getGradeType(s.getIsspecial()));
                    for (Gradetype g : st.getStudentsubject().getGrades()) {
                        updateGradeType(g, st.getStudentid(), s.getSubjectid(), s.getIsspecial(), mysemesterbox);
                        g.setGradestring();
                        System.out.println(g.getGradestring());
                    }
                    filteredList.add(st);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filteredList;
    }

    public void addGradeColumn(String isspecial, TableView<Student> mytableview) {
        ArrayList<Gradetype> gradetypes = getGradeType(isspecial);
        for (Gradetype gt : gradetypes) {
            TableColumn<Student, String> gradeColumn = new TableColumn<>(gt.getGradetypename());
            gradeColumn.setCellValueFactory(data -> {
                Student student = data.getValue();
                Subject sj = student.getStudentsubject();
                ArrayList<Gradetype> tmp = sj.getGrades();
                Gradetype foundGradetype = null;
                for (Gradetype gtt : tmp) {
                    if (gtt.getGradetypeid() == gt.getGradetypeid()) {
                        foundGradetype = gtt;
                        break;
                    }
                }
                if (foundGradetype != null) {
                    return new SimpleStringProperty(foundGradetype.getGradestring());
                } else {
                    return new SimpleStringProperty("");
                }
            });
            mytableview.getColumns().add(gradeColumn);
        }
    }

    public void editGrade(Student selectedstudent) {
        if (selectedstudent != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editstudentgrade.fxml"));
            Stage stage = new Stage();
            try {
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Sửa thông tin điểm");
                stage.initModality(Modality.APPLICATION_MODAL);
                EditstudentgradeController editstudentgradeController = fxmlLoader.getController();
                editstudentgradeController.edit(selectedstudent, SemesterBox.getValue(), SubjectnameBox.getValue());
                stage.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
            updateTableGrade();

        }

    }

    public void updateTableGrade() {
        String subjectname = SubjectnameBox.getValue();
        String classname = ClassnameBox.getValue();
        Teachersubjectclasses tsc = new Teachersubjectclasses();
        Subject s = new Subject();
        Studentidgrade_col.setCellValueFactory(new PropertyValueFactory<>("studentid"));
        Studentnamegrade_col.setCellValueFactory(new PropertyValueFactory<>("name"));

        if (subjectname != null && classname != null && SemesterBox.getValue() != null) {
            for (Teachersubjectclasses sc : teacher.getTeachersubjectclasses()) {
                if (sc.getClassname().equals(classname)) {
                    tsc = sc;
                    break;
                }
            }
            for (Subject sj : tsc.getTeachersubjectteach()) {
                if (sj.getSubjectname().equals(subjectname)) {
                    s = sj;
                    break;
                }
            }
            TableGrade.getColumns().subList(2, TableGrade.getColumns().size()).clear();
            addGradeColumn(s.getIsspecial(), TableGrade);
            TableGrade.setItems(getStudentGradeList(tsc, s, SemesterBox));
            TableGrade.setFixedCellSize(30);
            TableGrade.setPrefHeight(TableGrade.getItems().size() * TableGrade.getFixedCellSize() + TableView.USE_COMPUTED_SIZE);

// Đặt chiều dài theo nội dung của cột
            for (TableColumn<?, ?> column : TableGrade.getColumns()) {
                column.prefWidthProperty().bind(TableGrade.widthProperty().multiply(0.25)); // Đặt chiều rộng cột là 25% chiều rộng của bảng
            }
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editMenuItem = new MenuItem("Chỉnh sửa");
            contextMenu.getItems().addAll(editMenuItem);
            TableGrade.setContextMenu(contextMenu);
            editMenuItem.setOnAction(event -> {
                Student selectedstudent = TableGrade.getSelectionModel().getSelectedItem();
                editGrade(selectedstudent);
            });
        } else {
            TableGrade.getItems().clear();
        }

    }

    public void setSubjectgradehomeroomclassBox() {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT subjectname FROM subjects ";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    filteredList.add(rs.getString(1));
                }
                SubjectgradehomeroomclassBox.setItems(filteredList);
                //Lấy ra các lớp gv này chủ nhiệm
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Teachersubjectclasses getTeacherSubjectClassesFromClassname(String classname) {
        Connection connection = database.connectDb();
        Teachersubjectclasses tsc = new Teachersubjectclasses();
        if (connection != null) {
            try {
                String sql = "SELECT * FROM classes WHERE classname = '" + classname + "'";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) {
                    tsc.setClassid(rs.getString(1));
                    tsc.setClassname(rs.getString(2));
                }
                //Lấy ra các lớp gv này chủ nhiệm
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tsc;
    }

    public Subject getSubjecFromSubjectName(String subjectname) {
        Connection connection = database.connectDb();
        Subject s = new Subject();
        if (connection != null) {
            try {
                String sql = "SELECT * FROM subjects WHERE subjectname = '" + subjectname + "'";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) {
                    s.setSubjectid(rs.getInt(1));
                    s.setSubjectname(rs.getString(2));
                    s.setIsspecial(rs.getString(3));
                }
                //Lấy ra các lớp gv này chủ nhiệm
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    public void updateStudentGradeHomeRoom() {
        String classname = NamehomeroomclassBox.getValue();
        String subjectname = SubjectgradehomeroomclassBox.getValue();
        Idstudentgradehomeroom_col.setCellValueFactory(new PropertyValueFactory<>("studentid"));
        Namestudentgradehomeroom_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (classname != null && subjectname != null && SemestergradehomeroomclassBox.getValue() != null) {
            Teachersubjectclasses tsc = getTeacherSubjectClassesFromClassname(classname);
            Subject s = getSubjecFromSubjectName(subjectname);
            Tablegradestudenthomeroom.getColumns().subList(2, Tablegradestudenthomeroom.getColumns().size()).clear();
            addGradeColumn(s.getIsspecial(), Tablegradestudenthomeroom);
            Tablegradestudenthomeroom.setItems(getStudentGradeList(tsc, s, SemestergradehomeroomclassBox));
            Tablegradestudenthomeroom.setFixedCellSize(30);
            Tablegradestudenthomeroom.setPrefHeight(Tablegradestudenthomeroom.getItems().size() * Tablegradestudenthomeroom.getFixedCellSize() + TableView.USE_COMPUTED_SIZE);

// Đặt chiều dài theo nội dung của cột
            for (TableColumn<?, ?> column : Tablegradestudenthomeroom.getColumns()) {
                column.prefWidthProperty().bind(Tablegradestudenthomeroom.widthProperty().multiply(0.25)); // Đặt chiều rộng cột là 25% chiều rộng của bảng
            }
        } else {
            Tablegradestudenthomeroom.getItems().clear();
        }


    }

    public void autoFixSize(TableView<?> myTableView) {
        myTableView.setFixedCellSize(30);
        myTableView.setPrefHeight(myTableView.getItems().size() * myTableView.getFixedCellSize() + TableView.USE_COMPUTED_SIZE);

// Đặt chiều dài theo nội dung của cột
        for (TableColumn<?, ?> column : myTableView.getColumns()) {
            column.prefWidthProperty().bind(myTableView.widthProperty().multiply(0.25)); // Đặt chiều rộng cột là 25% chiều rộng của bảng
        }
    }

    public ObservableList<Student> getStudentHomeRoomList(String classname) {
        ObservableList<Student> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT students.* FROM remakektpm.students \n" +
                        "JOIN Classes C ON students.classid = C.classid\n" +
                        "WHERE C.classname = " + "'" + classname + "'";
                System.out.println(sql);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    Student st = new Student();
                    st.setStudentid(rs.getString(1));
                    st.setName(rs.getString(2));
                    st.setGender(rs.getString(3).equals("male") ? "Nam" : "Nữ");
                    st.setBirthday(rs.getString(4));
                    st.setAddress(rs.getString(5));
                    st.setFathername(rs.getString(7));
                    st.setMothername(rs.getString(8));
                    st.setFatherphone(rs.getString(9));
                    st.setMotherphone(rs.getString(10));
                    String conduct = "";
                    if (rs.getString(11).equals("T"))
                        conduct = "Tốt";
                    else if (rs.getString(11).equals("K"))
                        conduct = "Khá";
                    else
                        conduct = "Trung bình";
                    st.setConduct(conduct);
                    st.setAbsent(rs.getInt(12));
                    filteredList.add(st);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return filteredList;
    }

    public void addColumnTableInforStudentHomeRoom(TableView<Student> mytableview) {
        TableColumn<Student, String> studentid = new TableColumn<>("ID");
        TableColumn<Student, String> studentname = new TableColumn<>("Tên");
        TableColumn<Student, String> gender = new TableColumn<>("Giới tính");
        TableColumn<Student, String> birthday = new TableColumn<>("Ngày sinh");
        TableColumn<Student, String> address = new TableColumn<>("Địa chỉ");
        TableColumn<Student, String> fathername = new TableColumn<>("Tên bố");
        TableColumn<Student, String> mothername = new TableColumn<>("Tên mẹ");
        TableColumn<Student, String> fatherphone = new TableColumn<>("SDT bố");
        TableColumn<Student, String> motherphone = new TableColumn<>("SDT mẹ");
        TableColumn<Student, String> conduct = new TableColumn<>("Hạnh kiểm");
        TableColumn<Student, Integer> absent = new TableColumn<>("Số buổi vắng");

        studentid.setCellValueFactory(new PropertyValueFactory<>("studentid"));
        studentname.setCellValueFactory(new PropertyValueFactory<>("name"));
        gender.setCellValueFactory(new PropertyValueFactory<>("studentid"));
        birthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        fathername.setCellValueFactory(new PropertyValueFactory<>("fathername"));
        mothername.setCellValueFactory(new PropertyValueFactory<>("mothername"));
        fatherphone.setCellValueFactory(new PropertyValueFactory<>("fatherphone"));
        motherphone.setCellValueFactory(new PropertyValueFactory<>("motherphone"));
        conduct.setCellValueFactory(new PropertyValueFactory<>("conduct"));
        absent.setCellValueFactory(new PropertyValueFactory<>("absent"));
        mytableview.getColumns().addAll(studentid, studentname, gender, birthday, address, fathername, mothername, fatherphone, motherphone, conduct, absent);

    }

    public void updateTableInforStudentHomeRoom() {
        if (!NamehomeroomclassBox.getValue().isEmpty()) {
            Tablestudenthomeroominfor.getColumns().clear();
            addColumnTableInforStudentHomeRoom(Tablestudenthomeroominfor);
            Tablestudenthomeroominfor.setItems(getStudentHomeRoomList(NamehomeroomclassBox.getValue()));
            autoFixSize(Tablestudenthomeroominfor);
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editMenuItem = new MenuItem("Chỉnh sửa");
            MenuItem addMenuItem = new MenuItem("Nhập thêm");
            MenuItem deleteMenuItem = new MenuItem("Xóa");
            contextMenu.getItems().addAll(editMenuItem, addMenuItem, deleteMenuItem);

            Tablestudenthomeroominfor.setContextMenu(contextMenu);
            editMenuItem.setOnAction(event -> {
               Student selectedstudent = Tablestudenthomeroominfor.getSelectionModel().getSelectedItem();

            });

            addMenuItem.setOnAction(event -> {

            });

            deleteMenuItem.setOnAction(event -> {

                }
            );


        }
    }

    public void initTable(String username) {
        getTeacherInfor(username);
        getTeacherHomeRoom();
        getClassTeacher();
        getStudyClassTeacher();
        if (!teacher.getTeachersubjectclasses().isEmpty()) {
            setSemesterBox();
            setClassnameBox();
            ClassnameBox.setOnAction(event -> {
                updateSubjectNameBox(ClassnameBox.getValue());
                updateTableGrade();
                updateTableInforStudentHomeRoom();
            });
            SubjectnameBox.setOnAction(event -> {
                updateTableGrade();
            });
            SemesterBox.setOnAction(event -> {
                updateTableGrade();
            });
        }
        if (!teacher.getHomeroomclass().isEmpty()) {
            setSubjectgradehomeroomclassBox();
            SubjectgradehomeroomclassBox.setOnAction(event -> {
                updateStudentGradeHomeRoom();
            });

            NamehomeroomclassBox.setOnAction(event -> {
                updateTableInforStudentHomeRoom();
                updateStudentGradeHomeRoom();
            });

            SemestergradehomeroomclassBox.setOnAction(event -> {
                updateStudentGradeHomeRoom();
            });

        }


    }

}
