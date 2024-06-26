package org.training.service.hospital;

import java.util.HashMap;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.training.service.hospital.daos.AppointmentRequest;
import org.training.service.hospital.daos.Doctor;

@Path("/willowogardens/categories")
public class WillowGardensHospitalService extends HospitalService {
    public WillowGardensHospitalService() {
        this.doctorsList.add(new Doctor("jared morris", "willow gardens general hospital", "cardiology", "9.00 a.m - 11.00 a.m", 10000.0));
        this.doctorsList.add(new Doctor("henry foster", "willow gardens general hospital", "paediatric", "8.00 a.m - 10.00 a.m", 10000.0));
    }

    @POST
    @Path("/{category}/reserve")
    @Override
    public Response reserveAppointment(AppointmentRequest appointmentRequest, @PathParam("category") String category) {
        return super.reserveAppointment(appointmentRequest, category);
    }

    @GET
    @Path("/appointments/{appointment_id}/fee")
    @Override
    public Response checkChannellingFee(@PathParam("appointment_id") int id) {
        return super.checkChannellingFee(id);
    }

    @POST
    @Path("/patient/updaterecord")
    @Override
    public Response updatePatientRecord(HashMap<String, Object> patientDetails) {
        return super.updatePatientRecord(patientDetails);
    }

    @GET
    @Path("/patient/{SSN}/getrecord")
    @Override
    public Response getPatientRecord(@PathParam("SSN") String SSN) {
        return super.getPatientRecord(SSN);
    }

    @GET
    @Path("/patient/appointment/{appointment_id}/discount")
    @Override
    public Response isEligibleForDiscount(@PathParam("appointment_id") int id) {
        return super.isEligibleForDiscount(id);
    }

    @POST
    @Path("/admin/doctor/newdoctor")
    @Override
    public Response addNewDoctor(Doctor doctor) {
        return super.addNewDoctor(doctor);
    }
}

