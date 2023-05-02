package com.example.epark.Model;

public class Person {

    public Person(){}

    String name, phone_no, email_Id, password, selected_mode, UpiId;

    public Person(String name, String phone_no, String email_Id, String password, String selected_mode) {
        this.name = name;
        this.phone_no = phone_no;
        this.email_Id = email_Id;
        this.password = password;
        this.selected_mode = selected_mode;
    }

    public Person(String upiId) {
        UpiId = upiId;
    }

    public String getUpiId() {
        return UpiId;
    }

    public void setUpiId(String upiId) {
        UpiId = upiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail_Id() {
        return email_Id;
    }

    public void setEmail_Id(String email_Id) {
        this.email_Id = email_Id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelected_mode() {
        return selected_mode;
    }

    public void setSelected_mode(String selected_mode) {
        this.selected_mode = selected_mode;
    }
}
