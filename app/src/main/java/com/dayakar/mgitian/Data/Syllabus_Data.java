package com.dayakar.mgitian.Data;

public class Syllabus_Data {
    private String id,sub_code,subject,unit1,unit2,unit3,unit4,unit5,reference;

    public Syllabus_Data() {
    }

    public Syllabus_Data(String id, String sub_code, String subject, String unit1, String unit2, String unit3, String unit4, String unit5, String reference) {
        this.id = id;
        this.sub_code = sub_code;
        this.subject = subject;
        this.unit1 = unit1;
        this.unit2 = unit2;
        this.unit3 = unit3;
        this.unit4 = unit4;
        this.unit5 = unit5;
        this.reference = reference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUnit1() {
        return unit1;
    }

    public void setUnit1(String unit1) {
        this.unit1 = unit1;
    }

    public String getUnit2() {
        return unit2;
    }

    public void setUnit2(String unit2) {
        this.unit2 = unit2;
    }

    public String getUnit3() {
        return unit3;
    }

    public void setUnit3(String unit3) {
        this.unit3 = unit3;
    }

    public String getUnit4() {
        return unit4;
    }

    public void setUnit4(String unit4) {
        this.unit4 = unit4;
    }

    public String getUnit5() {
        return unit5;
    }

    public void setUnit5(String unit5) {
        this.unit5 = unit5;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
