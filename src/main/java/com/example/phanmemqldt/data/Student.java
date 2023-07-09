package com.example.phanmemqldt.data;

import java.util.*;

public class Student extends Person {
    String studentid;
    String classid;
    String classname;
    String fathername;
    String mothername;
    String fatherphone;
    String motherphone;
    String conduct;
    Integer absent;
    ArrayList<Subject> subjects = new ArrayList<>();
    Subject studentsubject = new Subject();

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getMothername() {
        return mothername;
    }

    public void setMothername(String mothername) {
        this.mothername = mothername;
    }

    public String getFatherphone() {
        return fatherphone;
    }

    public void setFatherphone(String fatherphone) {
        this.fatherphone = fatherphone;
    }

    public String getMotherphone() {
        return motherphone;
    }

    public void setMotherphone(String motherphone) {
        this.motherphone = motherphone;
    }

    public String getConduct() {
        return conduct;
    }

    public void setConduct(String conduct) {
        this.conduct = conduct;
    }

    public Integer getAbsent() {
        return absent;
    }

    public void setAbsent(Integer absent) {
        this.absent = absent;
    }

    public Subject getStudentsubject() {
        return studentsubject;
    }

    public void setStudentsubject(Subject studentsubject) {
        this.studentsubject = studentsubject;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }
}
