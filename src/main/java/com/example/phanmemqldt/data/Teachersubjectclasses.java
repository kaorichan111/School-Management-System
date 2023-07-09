package com.example.phanmemqldt.data;

import java.util.*;

public class Teachersubjectclasses {
    private String classid;
    private String classname;
    private ArrayList<Subject> teachersubjectteach = new ArrayList<>(); //đề xem giáo viên dạy bao nhiêu môn trong lớp này

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

    public ArrayList<Subject> getTeachersubjectteach() {
        return teachersubjectteach;
    }
}
