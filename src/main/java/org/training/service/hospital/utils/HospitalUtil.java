package org.training.service.hospital.utils;

import java.util.Calendar;
import org.training.service.hospital.daos.Appointment;
import org.training.service.hospital.daos.AppointmentRequest;
import org.training.service.hospital.daos.Doctor;
import org.training.service.hospital.daos.HospitalDAO;
public class HospitalUtil {
    public static int appointmentNumber = 1;

    public static Appointment makeNewAppointment(AppointmentRequest appointmentRequest, HospitalDAO hospitalDAO) {
        Appointment newAppointment = new Appointment();
        Doctor doctor = hospitalDAO.findDoctorByName(appointmentRequest.getDoctor());
        if (doctor != null && doctor.getHospital().equalsIgnoreCase(appointmentRequest.getHospital())) {
            newAppointment.setAppointmentNumber(appointmentNumber++);
            newAppointment.setDoctor(doctor);
            newAppointment.setPatient(appointmentRequest.getPatient());
            newAppointment.setFee(doctor.getFee());
            newAppointment.setConfirmed(false);
            return newAppointment;
        } else {
            return null;
        }
    }

    public static int checkForDiscounts(String dob) {
        int yob = Integer.parseInt(dob.split("-")[0]);
        int currentYear = Calendar.getInstance().get(1);
        int age = currentYear - yob;
        if (age < 12) {
            return 15;
        } else {
            return age > 55 ? 20 : 0;
        }
    }

    public static boolean checDiscountEligibility(String dob) {
        int yob = Integer.parseInt(dob.split("-")[0]);
        int currentYear = Calendar.getInstance().get(1);
        int age = currentYear - yob;
        return age < 12 || age > 55;
    }
}
