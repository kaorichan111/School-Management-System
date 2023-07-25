package com.example.phanmemqldt;

import com.example.phanmemqldt.data.*;
import com.example.phanmemqldt.data.Class;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import org.apache.poi.ss.formula.functions.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class TeacherController {

    @FXML
    private Button AccountBtn;
    @FXML
    private Button Excelbtn;

    @FXML
    private Button HomeroomclassBtn;

    @FXML
    private Button LineChartBtn;

    @FXML
    private AnchorPane Slider;
    @FXML
    private AnchorPane Subjectclass_form;
    @FXML
    private AnchorPane Homeroomclass_form;
    @FXML
    private AnchorPane Analysis_form;
    @FXML
    private AnchorPane Account_form;


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

    //thống kê điểm
    @FXML
    private LineChart<?, ?> Linechartgrade;
    @FXML
    private ComboBox<String> classname_ana;
    @FXML
    private ComboBox<String> gradetype_ana;

    @FXML
    private ComboBox<String> semesterbox_ana;

    @FXML
    private ComboBox<String> subjectname_ana;
    @FXML
    private CategoryAxis xaxis;

    @FXML
    private NumberAxis yaxis;

    //Account

    @FXML
    private Button ChangepassBtn;
    @FXML
    private Label Newpass_lb;

    @FXML
    private PasswordField Newpasstf;
    @FXML
    private Label Oldpasslb;
    @FXML
    private PasswordField Oldpasstf;
    @FXML
    private Button SavenewpassBtn;

    @FXML
    private TextField Usernametf_acc;

    //logour
    @FXML
    private Button Logoutbtn;
    private double x, y, x1, y1;


    public void clear() {
        ClassnameBox.setValue(null);
        SubjectnameBox.setItems(null);
        SemesterBox.setValue(null);
        TableGrade.getColumns().subList(2, TableGrade.getColumns().size()).clear();
        TableGrade.getItems().clear();

        NamehomeroomclassBox.setValue(null);
        Tablestudenthomeroominfor.getColumns().clear();
        SubjectgradehomeroomclassBox.setValue(null);
        SemestergradehomeroomclassBox.setValue(null);
        Tablegradestudenthomeroom.getColumns().subList(2, TableGrade.getColumns().size()).clear();
        TableGrade.getItems().clear();

        classname_ana.setValue(null);
        subjectname_ana.setItems(null);
        semesterbox_ana.setValue(null);
        gradetype_ana.setValue(null);
        Linechartgrade.getData().clear();

        Oldpasslb.setVisible(false);
        Oldpasstf.setVisible(false);
        Newpasstf.setVisible(false);
        Newpass_lb.setVisible(false);
        SavenewpassBtn.setVisible(false);
        Oldpasstf.clear();
        Newpasstf.clear();


    }

    public void swithForm(ActionEvent event) {
        if (event.getSource() == SubjectClassBtn) {
            clear();
            Subjectclass_form.setVisible(true);
            Homeroomclass_form.setVisible(false);
            Analysis_form.setVisible(false);
            Account_form.setVisible(false);
        } else if (event.getSource() == HomeroomclassBtn) {
            clear();
            Subjectclass_form.setVisible(false);
            Homeroomclass_form.setVisible(true);
            Analysis_form.setVisible(false);
            Account_form.setVisible(false);
        } else if (event.getSource() == LineChartBtn) {
            clear();
            Subjectclass_form.setVisible(false);
            Homeroomclass_form.setVisible(false);
            Analysis_form.setVisible(true);
            Account_form.setVisible(false);
        } else if (event.getSource() == AccountBtn) {
            clear();
            Subjectclass_form.setVisible(false);
            Homeroomclass_form.setVisible(false);
            Analysis_form.setVisible(false);
            Account_form.setVisible(true);
        }

    }

    public void importDataFromExcel(File file, ComboBox<String> classname) {
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                Teachersubjectclasses ts = getTeacherSubjectClassesFromClassname(classname.getValue());
                FileInputStream fileInputStream = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fileInputStream);
                Sheet sheet = workbook.getSheetAt(0);

                Iterator<Row> rowIterator = sheet.iterator();
                rowIterator.next(); // Skip the header row
                DataFormatter dataFormatter = new DataFormatter();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    for (int i = 0; i < 1; i++) {
                        Cell idCell = row.getCell(i);

                        System.out.println(idCell.getCellType() + " " + idCell.getNumericCellValue());
                    }

//                    System.out.println("hết 1 hàng");
                    String studentid = String.valueOf((int) row.getCell(0).getNumericCellValue());
                    String studentname = row.getCell(1).getStringCellValue();
                    String gender = row.getCell(2).getStringCellValue();
                    LocalDate birthday = row.getCell(3).getLocalDateTimeCellValue().toLocalDate();
                    String address = row.getCell(4).getStringCellValue();
                    String fathername = row.getCell(5).getStringCellValue();
                    String mothername = row.getCell(6).getStringCellValue();
                    String fatherphone = String.valueOf((long) row.getCell(7).getNumericCellValue());
                    String motherphone = String.valueOf((long) row.getCell(8).getNumericCellValue());
                    String conduct = row.getCell(9).getStringCellValue();
                    int absent = (int) row.getCell(10).getNumericCellValue();

                    // Get other cell values as needed

                    String sql = "INSERT INTO students(studentid, studentname, gender, birthday, address, classid, fathername, mothername, fatherphonenumber, motherphonenumber, conduct, absent) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, studentid);
                    preparedStatement.setString(2, studentname);
                    preparedStatement.setString(3, gender);
                    preparedStatement.setString(4, birthday.toString());
                    preparedStatement.setString(5, address);
                    preparedStatement.setString(6, ts.getClassid());
                    preparedStatement.setString(7, fathername);
                    preparedStatement.setString(8, mothername);
                    preparedStatement.setString(9, fatherphone);
                    preparedStatement.setString(10, motherphone);
                    preparedStatement.setString(11, conduct);
                    preparedStatement.setInt(12, absent);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Thêm học sinh thành công!");
                        LoginController.showSuccessMessage("thành công", "Thêm thông tin học sinh thành công!");
                    } else {
                        System.out.println("Thêm thông tin học sinh thất bại!");
                    }
                    // Set other parameters for the statement if needed
                    preparedStatement.close();
                }

                workbook.close();
                fileInputStream.close();
                LoginController.showSuccessMessage("thành công", "Thêm thông tin từ file excel thành công");
                updateTableInforStudentHomeRoom();
                updateStudentGradeHomeRoom();

                System.out.println("Data imported successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void importFromExcel(ActionEvent event, ComboBox<String> classname) {
        FileChooser fileChooser = new FileChooser();
        Stage primaryStage = (Stage) Excelbtn.getScene().getWindow();

        // Đặt tiêu đề cho hộp thoại chọn file
        fileChooser.setTitle("Chọn File");

        // Hiển thị hộp thoại chọn file và lấy file đã chọn
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        // Kiểm tra xem người dùng đã chọn file hay chưa
        if (selectedFile != null) {
            if (classname.getValue() != null) {
                importDataFromExcel(selectedFile, classname);
            }
        } else {
            System.out.println("Không có file nào được chọn");
        }
    }


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
                semesterbox_ana.setItems(filteredList);
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
                    teacher.setUserName(rs.getString(8));
                }
                //Lấy ra các lớp gv này chủ nhiệm
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getTeacherHomeRoom() {
        ObservableList<String> filterlist = FXCollections.observableArrayList();
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
                    filterlist.add(c.getClassname());
                    System.out.println("Lớp chủ nhiêm : " + c.getClassname());
                }
                NamehomeroomclassBox.setItems(filterlist);
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
        subjectname_ana.setItems(filteredList);
    }

    public void setClassnameBox() {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (Teachersubjectclasses ts : teacher.getTeachersubjectclasses()) {
            filteredList.add(ts.getClassname());
        }
        ClassnameBox.setItems(filteredList);
        classname_ana.setItems(filteredList);
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
                    //System.out.println("Loại điểm : " + g.getGradetypename() + " giá trị : " + grade.getValues());
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getStudentGradeForEachGradeType(Student st, Subject s, ComboBox<String> mysemesterbox) {
        for (Gradetype g : st.getStudentsubject().getGrades()) {
            updateGradeType(g, st.getStudentid(), s.getSubjectid(), s.getIsspecial(), mysemesterbox);
            g.setGradestring();
            g.setSumGrade();
            // System.out.println("Tổng điểm " + g.getSumGrade());
            //System.out.println(g.getGradestring());
        }
        st.getStudentsubject().setFinalgrade();

    }


    public ObservableList<Student> getStudentGradeList(Teachersubjectclasses tsc, Subject s, ComboBox<String> mysemesterbox) {
        ObservableList<Student> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT studentid, studentname FROM students \n" +
                        "JOIN Classes C ON students.classid = C.classid\n" +
                        "WHERE C.classname = " + "'" + tsc.getClassname() + "'";
                //System.out.println(sql);
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                List<Student> students = new ArrayList<>();
                // Tạo danh sách các Callable để thực hiện truy vấn SQL và tính toán các điểm
                //List<Callable<Student>> tasks = new ArrayList<>();
                while (rs.next()) {
                    Student st = new Student();
                    st.setStudentid(rs.getString(1));
                    st.setName(rs.getString(2));
                    st.getStudentsubject().setSubjectid(s.getSubjectid());
                    st.getStudentsubject().setSubjectname(s.getSubjectname());
                    st.getStudentsubject().setIsspecial(s.getIsspecial());
                    st.getStudentsubject().setGrades(getGradeType(s.getIsspecial()));
                    students.add(st);


//                    for (Gradetype g : st.getStudentsubject().getGrades()) {
//                        updateGradeType(g, st.getStudentid(), s.getSubjectid(), s.getIsspecial(), mysemesterbox);
//                        g.setGradestring();
//                        g.setSumGrade();
//                        // System.out.println("Tổng điểm " + g.getSumGrade());
//                        //System.out.println(g.getGradestring());
//                    }
//                    System.out.println("Điểm tổng kết :" + st.getStudentsubject().getFinalgrade());
//                    filteredList.add(st);
                }
                rs.close();
                connection.close();
                // Tạo ExecutorService với số luồng tối đa bằng số học sinh
                ExecutorService executorService = Executors.newFixedThreadPool(students.size());

                // Tạo danh sách các Callable để tính toán điểm của từng học sinh
                List<Callable<Student>> tasks = new ArrayList<>();
                for (Student st : students) {
                    tasks.add(() -> {
                        getStudentGradeForEachGradeType(st, s, mysemesterbox);
                        return st;
                    });
                }

                // Thực thi các Callable bằng ExecutorService
                List<Future<Student>> futures = executorService.invokeAll(tasks);

                // Đóng ExecutorService sau khi hoàn thành
                executorService.shutdown();

                // Lấy kết quả từ các Future và cập nhật vào danh sách filteredList
                for (Future<Student> future : futures) {
                    Student st = future.get();
                    // Cập nhật thông tin điểm vào filteredList
                    filteredList.add(st);
                }
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
        if (isspecial.equals("false")) {
            TableColumn<Student, String> finalgradeColumn = new TableColumn<>("Điểm tổng kết");
            finalgradeColumn.setCellValueFactory(data -> {
                Student student = data.getValue();
                Subject sj = student.getStudentsubject();
                Double finalgrade = sj.getFinalgrade();
                return new SimpleStringProperty(String.valueOf(finalgrade));

            });
            mytableview.getColumns().add(finalgradeColumn);
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
        Studentnamegrade_col.prefWidthProperty().bind(TableGrade.widthProperty().multiply(0.25));

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
            TableGrade.setItems(getStudentGradeList(tsc, s, SemesterBox));
            addGradeColumn(s.getIsspecial(), TableGrade);
            TableGrade.setFixedCellSize(30);
            TableGrade.setPrefHeight(TableGrade.getItems().size() * TableGrade.getFixedCellSize() + TableView.USE_COMPUTED_SIZE);
//
//// Đặt chiều dài theo nội dung của cột
//            for (TableColumn<?, ?> column : TableGrade.getColumns()) {
//                column.prefWidthProperty().bind(TableGrade.widthProperty().multiply(0.25)); // Đặt chiều rộng cột là 25% chiều rộng của bảng
//            }
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
//            for (TableColumn<?, ?> column : Tablegradestudenthomeroom.getColumns()) {
//                column.prefWidthProperty().bind(Tablegradestudenthomeroom.widthProperty().multiply(0.25)); // Đặt chiều rộng cột là 25% chiều rộng của bảng
//            }
        } else {
            Tablegradestudenthomeroom.getItems().clear();
        }


    }

    public void autoFixSize(TableView<?> myTableView) {
        myTableView.setFixedCellSize(30);
        myTableView.setPrefHeight(myTableView.getItems().size() * myTableView.getFixedCellSize() + TableView.USE_COMPUTED_SIZE);

// Đặt chiều dài theo nội dung của cột
//        for (TableColumn<?, ?> column : myTableView.getColumns()) {
//            column.prefWidthProperty().bind(myTableView.widthProperty().multiply(0.25)); // Đặt chiều rộng cột là 25% chiều rộng của bảng
//        }
    }

    public ObservableList<Student> getStudentHomeRoomList(String classname) {
        ObservableList<Student> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT students.* FROM students \n" +
                        "JOIN Classes C ON students.classid = C.classid\n" +
                        "WHERE C.classname = " + "'" + classname + "'";
                //System.out.println(sql);
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
        gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
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

    public void editStudentHomeRoomInfor(Student selectedstudent, String edit, String classname) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editstudenthomeroominfor.fxml"));
        Stage stage = new Stage();
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sửa thông tin học sinh");
            stage.initModality(Modality.APPLICATION_MODAL);
            EditstudenthomeroominforController editstudenthomeroominforController = fxmlLoader.getController();
            editstudenthomeroominforController.edit(selectedstudent, edit, classname);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTableGrade();
        updateTableInforStudentHomeRoom();
        updateStudentGradeHomeRoom();
    }

    public void deleteStudentHomeRoom(Student selectedstudent) {

        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "DELETE FROM students WHERE studentid = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, selectedstudent.getStudentid());
                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Xóa học sinh thành công!");
                    LoginController.showSuccessMessage("thành công", "Xóa học sinh thành công");
                } else {
                    System.out.println("Xóa học sinh thất bại!");
                }
                updateTableGrade();
                updateTableInforStudentHomeRoom();
                updateStudentGradeHomeRoom();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void updateTableInforStudentHomeRoom() {
        if (NamehomeroomclassBox.getValue() != null) {
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
                if (selectedstudent != null) {
                    String edit = "edit";
                    editStudentHomeRoomInfor(selectedstudent, edit, NamehomeroomclassBox.getValue());
                }

            });

            addMenuItem.setOnAction(event -> {
                Student selectedstudent = Tablestudenthomeroominfor.getSelectionModel().getSelectedItem();
                String edit = "add";
                editStudentHomeRoomInfor(selectedstudent, edit, NamehomeroomclassBox.getValue());

            });

            deleteMenuItem.setOnAction(event -> {
                Student selectedstudent = Tablestudenthomeroominfor.getSelectionModel().getSelectedItem();
                if (selectedstudent != null)
                    deleteStudentHomeRoom(selectedstudent);
            });


        }
    }

    public void setGradetypename() {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT gradetypename FROM gradetypes WHERE gradetypevalue = 'số'";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    filteredList.add(rs.getString(1));
                }
                gradetype_ana.setItems(filteredList);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Double> getGradeArray(String classname, String subjectname, String semesterid, String gradetypename) {
        ArrayList<Double> filteredList = new ArrayList<Double>();
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
                        "WHERE  Sub.subjectname = '" + subjectname + "' AND GT.gradetypename = '" + gradetypename + "' AND SS.semesterid =" + semesterid + " AND classes.classname = '" + classname + "'  order by S.studentid ;";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    //System.out.println(rs.getString(11));
                    filteredList.add(rs.getDouble(11));
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filteredList;
    }

    private int countOccurrences(ArrayList<Double> numbers, double x) {
        int count = 0;
        for (double number : numbers) {
            if (number == x) {
                count++;
            }
        }
        //System.out.println("Số lượng điểm " + x + " là :" + count);
        return count;
    }


    public void setLineChart(LineChart<Number, Number> mylinechart, ArrayList<Double> grades) {
        // Tạo các trục


        // Tạo đồ thị LineChart
        //mylinechart = new LineChart<>(xAxis, yAxis);
        mylinechart.setTitle("Biểu đồ điểm");

        // Tạo dữ liệu cho đồ thị
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Số lượng");

        for (double x = 0; x <= 10; x += 0.5) {
            int count = countOccurrences(grades, x);
            series.getData().add(new XYChart.Data<>(x, count));
        }

        // Thêm dữ liệu vào đồ thị
        mylinechart.getData().add(series);
    }

    public ObservableList<String> getXaxis() {
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (double x = 0; x <= 10; x += 0.5) {
            filteredList.add(String.valueOf(x));
        }
        return filteredList;
    }

    public void updateLineChart() {
        String classname = classname_ana.getValue();
        Subject sj = getSubjecFromSubjectName(subjectname_ana.getValue());
        String semesterid = semesterbox_ana.getValue();
        String gradetypename = gradetype_ana.getValue();
        if (classname != null && sj != null && semesterid != null && gradetypename != null && subjectname_ana.getValue() != null) {
            if (sj.getIsspecial().equals("false")) {
                Linechartgrade.getData().clear();
                ArrayList<Double> grades = getGradeArray(classname, sj.getSubjectname(), semesterid, gradetypename);
//                NumberAxis xAxis = new NumberAxis(0, 20, 1);
//                NumberAxis yAxis = new NumberAxis(0, 1000, 50);
//                xAxis.setLabel("Điểm");
////                yAxis.setLabel("Số lượng");
//                Linechartgrade = new LineChart<>(xAxis, yAxis);
                Linechartgrade.setTitle("Biểu đồ điểm");
                xaxis.setCategories(getXaxis());
                yaxis.hashCode();
                // Tạo dữ liệu cho đồ thị
                XYChart.Series series = new XYChart.Series<>();
                //  series.setName("Số lượng");
                //   series.getData().add(new XYChart.Data("5", 123));
                // series.getData().add(new XYChart.Data("10", 345));
                // Linechartgrade.getData().add(series);
                for (double x = 0; x <= 10; x += 0.5) {
                    int count = countOccurrences(grades, x);
//                    if (count != 0)
                    series.getData().add(new XYChart.Data<>(String.valueOf(x), count));
                }

                // Thêm dữ liệu vào đồ thị
                Linechartgrade.getData().add(series);
            } else
                Linechartgrade.getData().clear();
        } else
            Linechartgrade.getData().clear();

    }

    public void showSavePassWord() {
        Oldpasslb.setVisible(true);
        Oldpasstf.setVisible(true);
        Newpasstf.setVisible(true);
        Newpass_lb.setVisible(true);
        SavenewpassBtn.setVisible(true);
    }

    public void saveNewPassWord(Teacher teacher) {

        Connection connection = database.connectDb();
        if (Oldpasstf.getText().isEmpty() || Newpasstf.getText().isEmpty())
            LoginController.showErrorMessage("Lỗi", "Vui lòng nhập đầy đủ thông tin");
        else {
            if (connection != null) {
                try {
                    String sql = "SELECT * FROM teachers WHERE username = '" + teacher.getUserName() + "' AND PASSWORD = '" + Oldpasstf.getText() + "'";
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);
                    if (rs.next()) {
                        String sql1 = "UPDATE teachers SET password = '" + Newpasstf.getText() + "' WHERE username = '" + teacher.getUserName() + "'";
                        Statement statement1 = connection.createStatement();
                        int rowsInserted = statement1.executeUpdate(sql1);
                        if (rowsInserted > 0) {
                            System.out.println("Đổi mật khẩu thành công!");
                            LoginController.showSuccessMessage("thành công", "Đổi mật khẩu thành công!");
                            Oldpasslb.setVisible(false);
                            Oldpasstf.setVisible(false);
                            Newpasstf.setVisible(false);
                            Newpass_lb.setVisible(false);
                            SavenewpassBtn.setVisible(false);
                            Oldpasstf.clear();
                            Newpasstf.clear();
                        } else {
                            System.out.println("Đổi mật khẩu thất bại!");
                        }

                    } else
                        LoginController.showErrorMessage("Lỗi", "Sai mật khẩu cũ");

                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void logout(Button Logoutbtn) {
        LoginController.showSuccessMessage("Thành công", "Đăng xuất thành công!");
        Stage stage = (Stage) Logoutbtn.getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        try {
            Parent root = fxmlLoader.load();
            Stage stage1 = new Stage();
            Scene scene = new Scene(root);
            root.setOnMousePressed((MouseEvent event) -> {
                x = event.getSceneX();
                y = event.getSceneY();
                x1 = event.getScreenX();
                y1 = event.getScreenY();
                System.out.println(x + " " + y + " " + x1 + " " + y1);
            });

            root.setOnMouseDragged((MouseEvent event) -> {
                stage1.setX(event.getScreenX() - x);
                stage1.setY(event.getScreenY() - y);
            });

//
//        else
//            System.out.println("fail to connect database");
            stage1.initStyle(StageStyle.TRANSPARENT);
            stage1.setTitle("Hệ thống quản lý trường thcs");
            stage1.setScene(scene);
            stage1.show();
        } catch (Exception e) {
            e.printStackTrace();
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
            setGradetypename();
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
            classname_ana.setOnAction(event -> {
                updateSubjectNameBox(classname_ana.getValue());
                updateLineChart();
            });
            subjectname_ana.setOnAction(event -> {
                updateLineChart();
            });
            semesterbox_ana.setOnAction(event -> {
                updateSubjectNameBox(classname_ana.getValue());
                updateLineChart();
            });
            gradetype_ana.setOnAction(event -> {
                updateLineChart();
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

            Excelbtn.setOnAction(event -> {
                importFromExcel(event, NamehomeroomclassBox);
            });
        }

        Usernametf_acc.setText(teacher.getUserName());
        ChangepassBtn.setOnAction(event -> {
            showSavePassWord();
        });

        SavenewpassBtn.setOnAction(event -> {
            saveNewPassWord(teacher);
        });

        Logoutbtn.setOnAction(event -> {
            logout(Logoutbtn);
        });
    }

}
