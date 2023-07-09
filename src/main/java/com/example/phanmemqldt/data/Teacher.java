package com.example.phanmemqldt.data;

import java.text.*;
import java.util.*;

public class Teacher extends Person {
    private String teacherid;
    private int workYear;
    private int salary;
    private String formatSalary;
    private String userName;
    private String Password = "123";

    private String role;
    private ArrayList<Class> homeroomclass = new ArrayList<>();
    private ArrayList<Teachersubjectclasses> teachersubjectclasses = new ArrayList<>();

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public int getWorkYear() {
        return workYear;
    }

    public void setWorkYear(int workYear) {
        this.workYear = workYear;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFormatSalary() {
        return this.formatSalary;
    }

    public void setFormatSalary() {
        // Tạo đối tượng NumberFormat
        NumberFormat numberFormat = new DecimalFormat("#,###");

        // Định dạng số
        this.formatSalary = numberFormat.format(this.salary);

    }

    public ArrayList<Class> getHomeroomclass() {
        return homeroomclass;
    }

    public ArrayList<Teachersubjectclasses> getTeachersubjectclasses() {
        return teachersubjectclasses;
    }
}
