package com.dayakar.mgitian.Data;

public class Facutly_Data_Class  {

    private String name,designation,id,qualification,cabin;

    public Facutly_Data_Class() {
    }

    public Facutly_Data_Class(String name, String designation, String id, String qualification, String cabin) {
        this.name = name;
        this.designation = designation;
        this.id = id;
        this.qualification = qualification;
        this.cabin = cabin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }
}
