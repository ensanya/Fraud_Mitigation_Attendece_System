package com.example.userinterfaceclientside;

public class Helper {
    String name,employeeId,branch,password;

    public Helper() {
    }

    public Helper(String name, String employeeId, String branch, String password) {
        this.name = name;
        this.employeeId = employeeId;
        this.branch = branch;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

