package com.example.phanmemqldt.data;

import java.util.*;

public class Gradetype {
    private int gradetypeid;
    private String gradetypename;
    private int index;
    private String gradetypevalue;

    private ArrayList<Grade> values = new ArrayList<>();

    private String gradestring;
    private Double sumGrade = 0.0;

    public void setSumGrade() {
        if (this.gradetypevalue.equals("số")) {
            for (Grade g : this.values) {
                sumGrade += g.getValues();
            }
            if (!this.values.isEmpty())
                sumGrade /= this.values.size();
        }
    }

    public Double getSumGrade() {
        return sumGrade;
    }

    public void setGradestring() {
        if (!this.getValues().isEmpty()) {
            ArrayList<Double> tmp = new ArrayList<>();
            for (Grade g : this.getValues()) {
                if (this.getGradetypevalue().equals("số")) {
                    tmp.add(g.getValues());
                } else {
                    this.gradestring = g.getSpecialvalue();
                    return;
                }
            }
            String s2 = tmp.toString();
            s2 = s2.substring(1, s2.length() - 1).replaceAll(", ", " ");
            this.gradestring = s2;
        }
    }

    public String getGradestring() {
        return gradestring;
    }

    public String getGradetypevalue() {
        return gradetypevalue;
    }

    public void setGradetypevalue(String gradetypevalue) {
        this.gradetypevalue = gradetypevalue;
    }

    public int getGradetypeid() {
        return gradetypeid;
    }

    public void setGradetypeid(int gradetypeid) {
        this.gradetypeid = gradetypeid;
    }

    public String getGradetypename() {
        return gradetypename;
    }

    public void setGradetypename(String gradetypename) {
        this.gradetypename = gradetypename;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<Grade> getValues() {
        return values;
    }

    public void setValues(ArrayList<Grade> values) {
        this.values = values;
    }
}
