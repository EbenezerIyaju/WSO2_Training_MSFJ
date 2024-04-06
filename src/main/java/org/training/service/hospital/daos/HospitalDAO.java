package org.training.service.hospital.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class HospitalDAO {
    private List<Doctor> doctorsList = new ArrayList<>();
    private List<String> catergories = new ArrayList<>();
    private HashMap<String, Patient> patientMap = new HashMap<>();
    private HashMap<String, PatientRecord> patientRecordMap = new HashMap<>();

    public List<Doctor> findDoctorByCategory(String category) {
        List<Doctor> list = new ArrayList<>();

        for(Doctor doctor : this.doctorsList) {
            if (category.equals(doctor.getCategory())) {
                list.add(doctor);
            }
        }

        return list;
    }

    public Doctor findDoctorByName(String name) {
        for(Doctor doctor : this.doctorsList) {
            if (doctor.getName().equals(name)) {
                return doctor;
            }
        }

        return null;
    }

    public List<Doctor> getDoctorsList() {
        return this.doctorsList;
    }

    public void setDoctorsList(List<Doctor> doctorsList) {
        this.doctorsList = doctorsList;
    }

    public List<String> getCatergories() {
        return this.catergories;
    }

    public void setCatergories(List<String> catergories) {
        this.catergories = catergories;
    }

    public HashMap<String, Patient> getPatientMap() {
        return this.patientMap;
    }

    public void setPatientMap(HashMap<String, Patient> patientMap) {
        this.patientMap = patientMap;
    }

    public HashMap<String, PatientRecord> getPatientRecordMap() {
        return this.patientRecordMap;
    }

    public void setPatientRecordMap(HashMap<String, PatientRecord> patientRecordMap) {
        this.patientRecordMap = patientRecordMap;
    }
}
