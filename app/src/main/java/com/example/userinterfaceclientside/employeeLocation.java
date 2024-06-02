package com.example.userinterfaceclientside;

public class employeeLocation {
    private String employeeId;
    private double latitude;
    private double longitude;

    public employeeLocation() {
        // Default constructor required for Firebase
    }

    public employeeLocation(String employeeId, double latitude, double longitude) {
        this.employeeId = employeeId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

