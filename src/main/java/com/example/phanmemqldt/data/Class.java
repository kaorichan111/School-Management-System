package com.example.phanmemqldt.data;

import java.util.*;

public class Class {
    private String classid;
    private String classname;
    private String idhomeroomteacher;
    private String namehomeroomteacher;


    private ArrayList<Subjectteacher> subjectteachers = new ArrayList<>();

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

    public String getIdhomeroomteacher() {
        return idhomeroomteacher;
    }

    public void setIdhomeroomteacher(String idhomeroomteacher) {
        this.idhomeroomteacher = idhomeroomteacher;
    }

    public ArrayList<Subjectteacher> getSubjectteachers() {
        return subjectteachers;
    }

    public String getNamehomeroomteacher() {
        return namehomeroomteacher;
    }

    public void setNamehomeroomteacher(String namehomeroomteacher) {
        this.namehomeroomteacher = namehomeroomteacher;
    }
}
