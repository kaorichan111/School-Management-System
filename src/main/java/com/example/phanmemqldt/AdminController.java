package com.example.phanmemqldt;

import com.example.phanmemqldt.data.*;
import com.example.phanmemqldt.data.Class;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.*;
import javafx.util.converter.*;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class AdminController {


    @FXML
    private Button AccountBtn;

    @FXML
    private Button ClassmanageBtn;

    @FXML
    private Button LogoutBtn;

    @FXML
    private AnchorPane Slider;

    @FXML
    private Button SubjectmanageBtn;

    @FXML
    private Button TeacherInforBtn;

    @FXML
    private VBox VBoxAdmin;

    @FXML
    private TextField SearchBtn1;

    @FXML
    private TableView<Teacher> TableTeacher;

    @FXML
    private TableColumn<Teacher, String> addressteacher_col;

    @FXML
    private TableColumn<Teacher, String> birthdayteacher_col;

    @FXML
    private TableColumn<Teacher, String> genderteacher_col;

    @FXML
    private TableColumn<Teacher, String> salary_col;

    @FXML
    private TableColumn<Teacher, String> teacherid_col;

    @FXML
    private TableColumn<Teacher, String> teachername_col;

    @FXML
    private TableColumn<Teacher, Integer> workyear_col;

    //them gv
    @FXML
    private Button AddTeacherBtn;

    @FXML
    private TextField Addresstf;
    @FXML
    private DatePicker BirthDayBox;

    @FXML
    private Button ClearBtn;
    @FXML
    private ComboBox<String> GenderBox;
    @FXML
    private TextField Nametf;

    @FXML
    private TextField Salarytf;
    @FXML
    private TextField WorkYearBox;

    //Thêm môn học
    @FXML
    private TableView<Subject> Tablesubject;

    @FXML
    private TableColumn<Subject, String> Subjectname_col;
    @FXML
    private TableColumn<Subject, Integer> Subjectid_col;
    @FXML
    private Button AddsubjectBtn;
    //Quản lý lớp học
    @FXML
    private TableView<Subjectteacher> Tablesubjectclass_mnc;
    @FXML
    private ComboBox<String> Classname_mnc_box;
    @FXML
    private TableColumn<Subjectteacher, Integer> Subjectid_mnc_col;

    @FXML
    private TableColumn<Subjectteacher, String> Subjectname_mnc_col;
    @FXML
    private TableColumn<Subjectteacher, String> teacherid_mnc_col;
    @FXML
    private TableColumn<Subjectteacher, String> teachername_mnc_col;
    @FXML
    private TableColumn<Class, String> Idhomeroomteacher_mnc;

    @FXML
    private TableColumn<Class, String> Namehomeroomteacher_mnc;
    @FXML
    private TableView<Class> Tablehomeroomteacher_mnc;

    public void getClassName() {
        Connection connection = database.connectDb();
        ObservableList<String> filteredList = FXCollections.observableArrayList();


        if (connection != null) {
            try {
                String sql = "SELECT * FROM classes";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    filteredList.add(rs.getString(2));
                }
                Classname_mnc_box.setItems(filteredList);
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<Subjectteacher> getSubjectClassList(String classname) {
        Connection connection = database.connectDb();
        ObservableList<Subjectteacher> filteredList = FXCollections.observableArrayList();
        System.out.println(classname);
        if (connection != null) {
            try {
                String sql = "SELECT * FROM (" +
                        "    SELECT Sub.subjectid, Sub.subjectname, T.teacherid, T.teachername " +
                        "    FROM subjects Sub " +
                        "    JOIN teachersubjects TS ON Sub.subjectid = TS.subjectid " +
                        "    JOIN teachers T ON TS.teacherid = T.teacherid " +
                        "    JOIN classes C ON TS.classid = C.classid " +
                        "    WHERE C.classname = " + "'" + classname + "'" +
                        "    UNION ALL " +
                        "    SELECT Sub.subjectid, Sub.subjectname, NULL AS teacherid, NULL AS teachername " +
                        "    FROM subjects Sub " +
                        "    WHERE Sub.subjectid NOT IN ( " +
                        "        SELECT TS.subjectid " +
                        "        FROM teachersubjects TS " +
                        "        JOIN classes C ON TS.classid = C.classid " +
                        "        WHERE C.classname = " + "'" + classname + "' " +
                        "    ) " +
                        ") AS result " +
                        "ORDER BY subjectid";
                Statement statement = connection.createStatement();

                ResultSet rs = statement.executeQuery(sql);

                while (rs.next()) {
                    Subjectteacher sjt = new Subjectteacher();
                    sjt.setSubjectid(rs.getInt(1));
                    sjt.setSubjectname(rs.getString(2));
                    sjt.setTeacherid(rs.getString(3));
                    sjt.setTeachername(rs.getString(4));
                    filteredList.add(sjt);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filteredList;
    }

    public ObservableList<Class> getHomeRoomClass(String classname) {
        Connection connection = database.connectDb();
        ObservableList<Class> filteredList = FXCollections.observableArrayList();
        System.out.println(classname);
        if (connection != null) {
            try {
                String sql = "select T.teacherid, T.teachername \n" +
                        "from teachers T \n" +
                        "JOIN classes C ON C.idhomeroomteacher = T.teacherid\n" +
                        "WHERE C.classname = " + "'" + classname + "'";
                Statement statement = connection.createStatement();

                ResultSet rs = statement.executeQuery(sql);

                if (rs.next()) {
                    Class c = new Class();
                    c.setClassname(classname);
                    c.setIdhomeroomteacher(rs.getString(1));
                    c.setNamehomeroomteacher(rs.getString(2));
                    filteredList.add(c);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filteredList;
    }

    public void updateTableSubjectClassList() {
        Subjectid_mnc_col.setCellValueFactory(new PropertyValueFactory<>("subjectid"));
        Subjectname_mnc_col.setCellValueFactory(new PropertyValueFactory<>("subjectname"));
        teacherid_mnc_col.setCellValueFactory(new PropertyValueFactory<>("teacherid"));
        teachername_mnc_col.setCellValueFactory(new PropertyValueFactory<>("teachername"));
        if (Classname_mnc_box.getValue() != null) {
            Tablesubjectclass_mnc.setItems(getSubjectClassList(Classname_mnc_box.getValue()));
        }
    }

    public void updateTableHomeRoomTeacher() {
        Idhomeroomteacher_mnc.setCellValueFactory(new PropertyValueFactory<>("idhomeroomteacher"));
        Namehomeroomteacher_mnc.setCellValueFactory(new PropertyValueFactory<>("namehomeroomteacher"));
        if (Classname_mnc_box.getValue() != null) {
            Tablehomeroomteacher_mnc.setItems(getHomeRoomClass(Classname_mnc_box.getValue()));
        }
    }

    public void deletesubjectteacher(Subjectteacher sjt, String classname) {
        Connection connection = database.connectDb();
        String classid = "";
        if (connection != null) {
            try {
                String sql = "SELECT classid FROM classes WHERE classname = " + "'" + classname + "'";
                Statement statement = connection.createStatement();

                ResultSet rs = statement.executeQuery(sql);
                if (rs.next())
                    classid = rs.getString(1);
                String sql1 = "DELETE FROM teachersubjects WHERE teacherid = ? AND subjectid = ? AND classid = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql1);
                preparedStatement.setString(1, sjt.getTeacherid());
                preparedStatement.setInt(2, sjt.getSubjectid());
                preparedStatement.setString(3, classid);
                int rowdeleted = preparedStatement.executeUpdate();
                if (rowdeleted > 0) {
                    System.out.println("Xóa phân công thành công!");
                    LoginController.showSuccessMessage("thành công", "Xóa phân công thành công!");
                } else {
                    System.out.println("Xóa phân công thất bại!");
                }
                updateTableSubjectClassList();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void editsubjectteacher(Subjectteacher sjt, String classname) {
        Connection connection = database.connectDb();
        String classid = "";
        if (connection != null) {
            try {
                String sql = "SELECT classid FROM classes WHERE classname = " + "'" + classname + "'";
                Statement statement = connection.createStatement();

                ResultSet rs = statement.executeQuery(sql);
                if (rs.next())
                    classid = rs.getString(1);

                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sjt != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editsubjectteacher.fxml"));
            Stage stage = new Stage();
            try {
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Phân công");
                stage.initModality(Modality.APPLICATION_MODAL);
                EditsubjectteacherController editsubjectteacherController = fxmlLoader.getController();
                editsubjectteacherController.edit(sjt, classid);
                stage.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
            updateTableSubjectClassList();

        }
    }

    public void deleteHomeRoomTeacher(Class c, String classname) {
        Connection connection = database.connectDb();
        String classid = "";
        if (connection != null) {
            try {
                String sql = "SELECT classid FROM classes WHERE classname = " + "'" + classname + "'";
                Statement statement = connection.createStatement();

                ResultSet rs = statement.executeQuery(sql);
                if (rs.next())
                    classid = rs.getString(1);
                if (c != null) {
                    String sql1 = "UPDATE classes SET idhomeroomteacher = null WHERE idhomeroomteacher = ? AND classid = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql1);
                    preparedStatement.setString(1, c.getIdhomeroomteacher());
                    preparedStatement.setString(2, classid);
                    int rowdeleted = preparedStatement.executeUpdate();
                    if (rowdeleted > 0) {
                        System.out.println("Xóa gvcn thành công!");
                        LoginController.showSuccessMessage("thành công", "Xóa gvcn thành công!");
                    } else {
                        System.out.println("Xóa gvcn thất bại!");
                    }
                    updateTableHomeRoomTeacher();
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void editHomeRoomTeacher(Class c, String classname) {
        Connection connection = database.connectDb();
        String classid = "";
        if (connection != null) {
            try {
                String sql = "SELECT classid FROM classes WHERE classname = " + "'" + classname + "'";
                Statement statement = connection.createStatement();

                ResultSet rs = statement.executeQuery(sql);
                if (rs.next())
                    classid = rs.getString(1);

                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("edithomeroomteacher.fxml"));
        Stage stage = new Stage();
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Phân công gvcn");
            stage.initModality(Modality.APPLICATION_MODAL);
            EdithomeroomteacherController edithomeroomteacherController = fxmlLoader.getController();
            edithomeroomteacherController.edit(c, classid);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTableSubjectClassList();
        updateTableHomeRoomTeacher();


    }

    public void updateTableSubjectClass() {
        getClassName();


        Classname_mnc_box.setOnAction(event -> {
            updateTableSubjectClassList();
            updateTableHomeRoomTeacher();
        });
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Chỉnh sửa");
        MenuItem deleteMenuItem = new MenuItem("Xóa");
        contextMenu.getItems().addAll(editMenuItem, deleteMenuItem);
        Tablesubjectclass_mnc.setContextMenu(contextMenu);

        ContextMenu contextMenu1 = new ContextMenu();
        MenuItem editMenuItem1 = new MenuItem("Chỉnh sửa");
        MenuItem deleteMenuItem1 = new MenuItem("Xóa");
        contextMenu1.getItems().addAll(editMenuItem1, deleteMenuItem1);
        Tablehomeroomteacher_mnc.setContextMenu(contextMenu1);

        deleteMenuItem.setOnAction(event -> {
            Subjectteacher sjt = Tablesubjectclass_mnc.getSelectionModel().getSelectedItem();
            deletesubjectteacher(sjt, Classname_mnc_box.getValue());
        });
        editMenuItem.setOnAction(event -> {
            Subjectteacher sjt = Tablesubjectclass_mnc.getSelectionModel().getSelectedItem();
            editsubjectteacher(sjt, Classname_mnc_box.getValue());
        });

        deleteMenuItem1.setOnAction(event -> {
            Class c = Tablehomeroomteacher_mnc.getSelectionModel().getSelectedItem();
            deleteHomeRoomTeacher(c, Classname_mnc_box.getValue());
        });

        editMenuItem1.setOnAction(event -> {
            Class c = Tablehomeroomteacher_mnc.getSelectionModel().getSelectedItem();
            editHomeRoomTeacher(c, Classname_mnc_box.getValue());
        });


    }

    public ObservableList<Subject> getSubjectList() {
        ObservableList<Subject> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {
                String sql = "SELECT * FROM subjects";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    Subject s = new Subject();
                    s.setSubjectid(rs.getInt(1));
                    s.setSubjectname(rs.getString(2));
                    s.setIsspecial(rs.getString(3));
                    filteredList.add(s);
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filteredList;
    }

    public void editSuject(Subject selectedsubject) {
        if (selectedsubject != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editsubject.fxml"));
            Stage stage = new Stage();
            try {
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Sửa thông tin môn học");
                stage.initModality(Modality.APPLICATION_MODAL);
                EditsubjectController editsubjectController = fxmlLoader.getController();
                editsubjectController.edit(selectedsubject);
                stage.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
            updateTableSubject();
        }

    }

    public void addSubject() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addsubject.fxml"));
        Stage stage = new Stage();
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sửa thông tin môn học");
            stage.initModality(Modality.APPLICATION_MODAL);
            AddsubjectController addsubjectController = fxmlLoader.getController();
            addsubjectController.addSubject();
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTableSubject();
    }

    public void updateTableSubject() {
        Subjectid_col.setCellValueFactory(new PropertyValueFactory<>("subjectid"));
        Subjectname_col.setCellValueFactory(new PropertyValueFactory<>("subjectname"));
        Tablesubject.setItems(getSubjectList());
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Chỉnh sửa");
        contextMenu.getItems().addAll(editMenuItem);
        Tablesubject.setContextMenu(contextMenu);
        editMenuItem.setOnAction(event -> {
            Subject selectedsubject = Tablesubject.getSelectionModel().getSelectedItem();
            editSuject(selectedsubject);
        });
        AddsubjectBtn.setOnAction(event -> {
            addSubject();
        });
    }

    public void deleteteacher(Teacher selectedteacher) {
        if (selectedteacher != null) {
            Connection connection = database.connectDb();
            if (connection != null) {
                try {
                    String sql = "DELETE FROM teachers WHERE teacherid = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, selectedteacher.getTeacherid());
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Xóa giáo viên thành công!");
                        LoginController.showSuccessMessage("thành công", "Xóa giáo viên thành công");
                    } else {
                        System.out.println("Xóa giáo viên thất bại!");
                    }
                    updateTableTeacher();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addTeacher() {
        if (Nametf.getText().isEmpty() ||
                GenderBox.getValue() == null ||
                BirthDayBox.getValue() == null ||
                Addresstf.getText().isEmpty() ||
                WorkYearBox.getText().isEmpty() ||
                Salarytf.getText().isEmpty()
        ) {
            LoginController.showErrorMessage("Lỗi", "Vui lòng nhập đầy đủ thông tin");
        } else if (containsDigitOrSpecialCharacter(Nametf.getText())) {
            LoginController.showErrorMessage("Lỗi", "Tên không dược chứa chữ số hay kí tự đặc biệt");
            System.out.println("Tên không dược chứa chữ số hay kí hiệu đặc biệt");
        } else if (containsLetterOrSpecialCharacter(WorkYearBox.getText())) {
            LoginController.showErrorMessage("Lỗi", "Năm làm việc không dc chứa chữ số hay kí tự đặc biệt");
            System.out.println("Năm làm việc không dc chứa chữ số hay kí tự đặc biệt");
        } else {
            Connection connection = database.connectDb();
            if (connection != null) {
                try {
                    Teacher t = new Teacher();
                    t.setName(Nametf.getText());
                    t.setVietnamesename();
                    Random random = new Random();
                    int generatedId = random.nextInt(99999 - 10000 + 1) + 10000;
                    String id = String.valueOf(generatedId);
                    // Kiểm tra xem id đã tồn tại trong bảng "teacher" chưa
                    String checkQuery = "SELECT teacherid FROM teachers WHERE teacherid = ?";
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
                    String insertQuery = "INSERT INTO teachers (teacherid, teachername, gender, birthday, address, workyear, salary, username, password, role) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, id);
                    insertStatement.setString(2, Nametf.getText());
                    insertStatement.setString(3, GenderBox.getValue().equals("Nam") ? "male" : "female");
                    insertStatement.setString(4, BirthDayBox.getValue().toString());
                    insertStatement.setString(5, Addresstf.getText());
                    insertStatement.setInt(6, Integer.parseInt(WorkYearBox.getText()));
                    String strippedNumberString = Salarytf.getText().replace(",", "");
                    insertStatement.setInt(7, Integer.parseInt(strippedNumberString));
                    insertStatement.setString(8, LoginController.initUserName(t.getVietnamesename(), id));
                    insertStatement.setString(9, "123");
                    insertStatement.setString(10, "teacher");

                    // Thực thi câu truy vấn
                    int rowsInserted = insertStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Thêm giáo viên thành công!");
                        LoginController.showSuccessMessage("thành công", "thêm giáo viên thành công");
                    } else {
                        System.out.println("Thêm giáo viên thất bại!");
                    }
                    updateTableTeacher();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void editTeacher(Teacher selectedteacher) {
        if (selectedteacher != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editteacher.fxml"));
            Stage stage = new Stage();
            try {
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Sửa thông tin giáo viên");
                stage.initModality(Modality.APPLICATION_MODAL);
                EditteacherController editteacherController = fxmlLoader.getController();
                editteacherController.edit(selectedteacher);
                stage.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
            updateTableTeacher();

        }
    }


    public static char getLastWordFirstChar(String fullName) {
        String[] words = fullName.split(" ");
        String lastWord = words[words.length - 1];
        return lastWord.charAt(0);
    }

    public static String getLastNameFirstInitial(String fullName) {
        String[] names = fullName.split(" "); // Split the full name into individual names
        String lastName = names[names.length - 1]; // Extract the last name


        Random random = new Random();
        String s = "";
        for (int i = 0; i < names.length - 1; i++) {
//            String id = String.valueOf(random.nextInt(99999) + 100000);
            String firstName = names[i];
            s += (names[i].charAt(0)); // Get the first letter of each first name

        }

        return lastName + s;
    }

    public static boolean containsLetter(String input) {
        return input.matches(".*[a-zA-Z]+.*");
    }

    public static String initClassName(String s) {
        int classId = Integer.parseInt(s);
        int base = classId / 1000; // Get the base number (e.g., 6 for 6001)
        int suffix = classId % 1000; // Get the suffix number (e.g., 1 for 6001)

        char suffixChar = (char) ('A' + suffix - 1); // Convert suffix number to character

        return String.valueOf(base) + suffixChar;
    }

//    public void switchForm(ActionEvent event) {
//
//    }


    public static boolean checkContainsNumber(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsLetterOrSpecialCharacter(String input) {
        Pattern pattern = Pattern.compile("[a-zA-Z\\p{Punct}]");
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public static boolean containsDigitOrSpecialCharacter(String input) {
        Pattern pattern = Pattern.compile("[\\d\\p{Punct}]");
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public void clear() {
        Nametf.clear();
        BirthDayBox.setValue(null);
        GenderBox.setValue(null);
        Addresstf.clear();
        WorkYearBox.clear();
        Salarytf.clear();
    }

    public ObservableList<Teacher> getTeacherList() {
        ObservableList<Teacher> filteredList = FXCollections.observableArrayList();
        Connection connection = database.connectDb();
        if (connection != null) {
            try {

                String sql = "SELECT * FROM teachers WHERE teacherid NOT LIKE '1'";
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
                while (rs.next()) {
                    Teacher t = new Teacher();
                    t.setTeacherid(rs.getString(1));
                    t.setName(rs.getString(2));
                    t.setGender(rs.getString(3).equals("male") ? "Nam" : "Nữ");
                    t.setBirthday(rs.getString(4));
                    t.setAddress(rs.getString(5));
                    t.setWorkYear(rs.getInt(6));
                    t.setSalary(rs.getInt(7));
                    t.setFormatSalary();
                    t.setUserName(rs.getString(8));
                    t.setPassword(rs.getString(9));
                    t.setVietnamesename();
                    filteredList.add(t);
                }
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filteredList;

    }

    public void updateTableTeacher() {
        teacherid_col.setCellValueFactory(new PropertyValueFactory<>("teacherid"));
        teachername_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderteacher_col.setCellValueFactory(new PropertyValueFactory<>("gender"));
        birthdayteacher_col.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        addressteacher_col.setCellValueFactory(new PropertyValueFactory<>("address"));
        workyear_col.setCellValueFactory(new PropertyValueFactory<>("workYear"));
        salary_col.setCellValueFactory(new PropertyValueFactory<>("formatSalary"));

        TableTeacher.setItems(getTeacherList());

        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        // Thiết lập định dạng hiển thị số nguyên lớn cho TextField
        StringConverter<Number> converter = new NumberStringConverter(numberFormat);
        Salarytf.setTextFormatter(new TextFormatter<>(converter));
        Salarytf.setOnKeyTyped(event -> {
            LoginController.formatNumber(Salarytf, numberFormat);
        });
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Chỉnh sửa");
        MenuItem deleteMenuItem = new MenuItem("Xóa");
        contextMenu.getItems().addAll(editMenuItem, deleteMenuItem);
        TableTeacher.setContextMenu(contextMenu);
        editMenuItem.setOnAction(event -> {
            Teacher selectedteacher = TableTeacher.getSelectionModel().getSelectedItem();
            editTeacher(selectedteacher);
        });
        deleteMenuItem.setOnAction(event -> {
            Teacher selectedteacher = TableTeacher.getSelectionModel().getSelectedItem();
            deleteteacher(selectedteacher);
        });
    }

    public void initTable() {
        GenderBox.getItems().addAll("Nam", "Nữ");
        GenderBox.setValue("Nam");
        TableColumn<Teacher, Integer> indexColumn = new TableColumn<>("STT");
        indexColumn.setCellValueFactory(cellData -> {
            // Lấy chỉ số dòng của cell hiện tại
            int rowIndex = TableTeacher.getItems().indexOf(cellData.getValue());
            return new SimpleObjectProperty<>(rowIndex + 1);
        });

        // Thêm cột số thứ tự vào TableView
        TableTeacher.getColumns().add(0, indexColumn);
        updateTableTeacher();
        updateTableSubject();
        updateTableSubjectClass();
    }


}


