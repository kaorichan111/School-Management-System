package com.example.phanmemqldt.data;

public class Grade {
    private String gradeid;
    private Double values;
    private String specialvalue;
    private String semestername;
    private String semesterid;

    public String getGradeid() {
        return gradeid;
    }

    public void setGradeid(String gradeid) {
        this.gradeid = gradeid;
    }

    public Double getValues() {
        return values;
    }

    public void setValues(Double values) {
        this.values = values;
    }

    public String getSpecialvalue() {
        return specialvalue;
    }

    public void setSpecialvalue(String specialvalue) {
        this.specialvalue = specialvalue;
    }

    public String getSemestername() {
        return semestername;
    }

    public void setSemestername(String semestername) {
        this.semestername = semestername;
    }

    public String getSemesterid() {
        return semesterid;
    }

    public void setSemesterid(String semesterid) {
        this.semesterid = semesterid;
    }
}
