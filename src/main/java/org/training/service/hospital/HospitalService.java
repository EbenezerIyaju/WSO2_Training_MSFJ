package org.training.service.hospital;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.training.service.hospital.daos.Appointment;
import org.training.service.hospital.daos.AppointmentRequest;
import org.training.service.hospital.daos.ChannelingFeeDao;
import org.training.service.hospital.daos.Doctor;
import org.training.service.hospital.daos.HospitalDAO;
import org.training.service.hospital.daos.Patient;
import org.training.service.hospital.daos.PatientRecord;
import org.training.service.hospital.daos.Status;
import org.training.service.hospital.utils.HospitalUtil;
public class HospitalService {
    private static Map<Integer, Appointment> appointments = new HashMap<>();
    private HospitalDAO hospitalDAO = new HospitalDAO();
    List<String> catergories = this.hospitalDAO.getCatergories();
    List<Doctor> doctorsList = this.hospitalDAO.getDoctorsList();

    public HospitalService() {
        this.catergories.add("surgery");
        this.catergories.add("cardiology");
        this.catergories.add("gynaecology");
        this.catergories.add("ent");
        this.catergories.add("paediatric");
    }

    public Response reserveAppointment(AppointmentRequest appointmentRequest, @PathParam("category") String category) {
        if (this.hospitalDAO.getCatergories().contains(category)) {
            Appointment appointment = HospitalUtil.makeNewAppointment(appointmentRequest, this.hospitalDAO);
            if (appointment == null) {
                Status status = new Status("Doctor " + appointmentRequest.getDoctor() + " is not available in " + appointmentRequest.getHospital());
                return Response.status(Response.Status.OK).entity(status).type("application/json").build();
            } else {
                appointments.put(appointment.getAppointmentNumber(), appointment);
                this.hospitalDAO.getPatientMap().put(appointmentRequest.getPatient().getSsn(), appointmentRequest.getPatient());
                if (!this.hospitalDAO.getPatientRecordMap().containsKey(appointmentRequest.getPatient().getSsn())) {
                    PatientRecord patientRecord = new PatientRecord(appointmentRequest.getPatient());
                    this.hospitalDAO.getPatientRecordMap().put(appointmentRequest.getPatient().getSsn(), patientRecord);
                }

                return Response.status(Response.Status.OK).entity(appointment).type("application/json").build();
            }
        } else {
            Status status = new Status("Invalid Category");
            return Response.ok(status, "application/json").build();
        }
    }

    public Response checkChannellingFee(@PathParam("appointment_id") int id) {
        ChannelingFeeDao channelingFee = new ChannelingFeeDao();
        if (appointments.containsKey(id)) {
            Patient patient = appointments.get(id).getPatient();
            Doctor doctor = appointments.get(id).getDoctor();
            channelingFee.setActualFee(Double.toString(doctor.getFee()));
            channelingFee.setDoctorName(doctor.getName().toLowerCase());
            channelingFee.setPatientName(patient.getName().toLowerCase());
            return Response.status(Response.Status.OK).entity(channelingFee).type("application/json").build();
        } else {
            Status status = new Status("Error.Could not Find the Requested appointment ID");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        }
    }

    public Response updatePatientRecord(HashMap<String, Object> patientDetails) {
        String SSN = (String)((Map)patientDetails.get("patient")).get("ssn");
        List symptoms = (List)patientDetails.get("symptoms");
        List treatments = (List)patientDetails.get("treatments");
        if (this.hospitalDAO.getPatientMap().get(SSN) != null) {
            Patient patient = this.hospitalDAO.getPatientMap().get(SSN);
            PatientRecord patientRecord = this.hospitalDAO.getPatientRecordMap().get(SSN);
            if (patient != null) {
                patientRecord.updateSymptoms(symptoms);
                patientRecord.updateTreatments(treatments);
                Status status = new Status("Record Update Success");
                return Response.status(Response.Status.OK).entity(status).type("application/json").build();
            } else {
                Status status = new Status("Could not find valid Patient Record");
                return Response.status(Response.Status.OK).entity(status).type("application/json").build();
            }
        } else {
            Status status = new Status("Could not find valid Patient Entry");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        }
    }

    public Response getPatientRecord(@PathParam("SSN") String SSN) {
        PatientRecord patientRecord = this.hospitalDAO.getPatientRecordMap().get(SSN);
        if (patientRecord == null) {
            Status status = new Status("Could not find valid Patient Entry");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        } else {
            return Response.status(Response.Status.OK).entity(patientRecord).type("application/json").build();
        }
    }

    public Response isEligibleForDiscount(@PathParam("appointment_id") int id) {
        Appointment appointment = appointments.get(id);
        if (appointment == null) {
            Status status = new Status("Invalid appointment ID");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        } else {
            boolean eligible = HospitalUtil.checDiscountEligibility(appointment.getPatient().getDob());
            Status status = new Status(String.valueOf(eligible));
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        }
    }

    public Response addNewDoctor(Doctor doctor) {
        String category = doctor.getCategory();
        if (!this.catergories.contains(category)) {
            this.catergories.add(category);
        }

        if (this.hospitalDAO.findDoctorByName(doctor.getName()) == null) {
            this.doctorsList.add(doctor);
            Status status = new Status("New Doctor Added Successfully");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        } else {
            Status status = new Status("Doctor Already Exist in the system");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        }
    }

    public static Map<Integer, Appointment> getAppointments() {
        return appointments;
    }

    public static void setAppointments(Map<Integer, Appointment> appointments) {
        HospitalService.appointments = appointments;
    }
}
