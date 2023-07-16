package com.example.phanmemqldt.data;

import java.text.*;
import java.util.regex.*;

public class Person {

    protected String name;
    protected String vietnamesename;
    protected String birthday;
    protected String address;

    protected String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static String removeAccent(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutAccent = pattern.matcher(normalized).replaceAll("").toLowerCase();
        return withoutAccent.replace("đ", "d");
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVietnamesename() {
        String normalized = Normalizer.normalize(this.name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutAccent = pattern.matcher(normalized).replaceAll("").toLowerCase();
        this.vietnamesename = withoutAccent.replace("đ", "d");
    }

    public String getVietnamesename() {
        return this.vietnamesename;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Person(String name, String birthday, String address, String gender) {
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.gender = gender;
    }

    public static String normalizeName(String name) {
        // Xóa dấu cách thừa và trim ở đầu và cuối chuỗi
        String trimmedName = name.trim();
        String[] words = trimmedName.split("\\s+");

        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                // Viết hoa chữ cái đầu và viết thường các chữ cái sau
                String normalizedWord = Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
                result.append(normalizedWord).append(" "); // Thêm dấu cách sau mỗi từ
            }
        }

        // Xóa dấu cách cuối cùng và trả về kết quả
        return result.toString().trim();
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
