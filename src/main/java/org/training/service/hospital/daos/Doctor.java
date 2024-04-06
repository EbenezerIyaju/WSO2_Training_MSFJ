package org.training.service.hospital.daos;

public class Doctor {

    private String name;
    private String hospital;
    private String category;
    private String availability;
    private double fee;

    public Doctor(String name, String hospital, String category, String availability, double fee) {
        this.name = name;
        this.hospital = hospital;
        this.category = category;
        this.availability = availability;
        this.fee = fee;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHospital() {
        return this.hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAvailability() {
        return this.availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public double getFee() {
        return this.fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
