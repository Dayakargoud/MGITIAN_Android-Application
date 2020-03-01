package com.dayakar.mgitian.Data;
import androidx.annotation.Keep;
@Keep
public class Student {
    private String branch,sub_branch,year,sem;
    public Student() {
    }
    public Student(String branch, String sub_branch, String year, String sem) {
        this.branch = branch;
        this.sub_branch = sub_branch;
        this.year = year;
        this.sem = sem;
    }
    public String getBranch() {
        return branch;
    }
    public void setBranch(String branch) {
        this.branch = branch;
    }
    public String getSub_branch() {
        return sub_branch;
    }
    public void setSub_branch(String sub_branch) {
        this.sub_branch = sub_branch;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getSem() {
        return sem;
    }


    public void setSem(String sem) {
        this.sem = sem;
    }
}