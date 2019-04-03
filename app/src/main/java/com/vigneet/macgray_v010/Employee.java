package com.vigneet.macgray_v010;

/**
 * Created by Vigneet on 20-04-2016.
 */
public class Employee {
    String id,first_name,last_name,empId;

    public Employee(String id, String first_name, String last_name,String empId) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.empId = empId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
}
