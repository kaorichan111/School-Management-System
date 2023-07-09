package com.example.phanmemqldt.data;

import java.util.*;

public class Subject {
    private int subjectid;
    private String subjectname;
    private String isspecial;
    private Double finalgrade;
    private ArrayList<Gradetype> grades = new ArrayList<>();

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getIsspecial() {
        return isspecial;
    }

    public void setIsspecial(String isspecial) {
        this.isspecial = isspecial;
    }

    public Double getFinalgrade() {
        return finalgrade;
    }

    public void setFinalgrade(Double finalgrade) {
        this.finalgrade = finalgrade;
    }

    public ArrayList<Gradetype> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Gradetype> grades) {
        this.grades = grades;
    }
}
