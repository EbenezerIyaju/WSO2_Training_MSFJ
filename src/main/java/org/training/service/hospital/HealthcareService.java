package org.training.service.hospital;

import com.google.gson.Gson;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.training.service.hospital.daos.*;
import org.training.service.hospital.utils.HealthCareUtil;

@Path("/healthcare")
public class HealthcareService {
    public HealthcareService() {
        this.fillCategories();
        HealthcareDao.doctorsList.add(new Doctor("thomas collins", "grand oak community hospital", "surgery", "9.00 a.m - 11.00 a.m", 7000.0));
        HealthcareDao.doctorsList.add(new Doctor("henry parker", "grand oak community hospital", "ent", "9.00 a.m - 11.00 a.m", 4500.0));
        HealthcareDao.doctorsList.add(new Doctor("abner jones", "grand oak community hospital", "gynaecology", "8.00 a.m - 10.00 a.m", 11000.0));
        HealthcareDao.doctorsList.add(new Doctor("abner jones", "grand oak community hospital", "ent", "8.00 a.m - 10.00 a.m", 6750.0));
        HealthcareDao.doctorsList.add(new Doctor("anne clement", "clemency medical center", "surgery", "8.00 a.m - 10.00 a.m", 12000.0));
        HealthcareDao.doctorsList.add(new Doctor("thomas kirk", "clemency medical center", "gynaecology", "9.00 a.m - 11.00 a.m", 8000.0));
        HealthcareDao.doctorsList.add(new Doctor("cailen cooper", "clemency medical center", "paediatric", "9.00 a.m - 11.00 a.m", 5500.0));
        HealthcareDao.doctorsList.add(new Doctor("seth mears", "pine valley community hospital", "surgery", "3.00 p.m - 5.00 p.m", 8000.0));
        HealthcareDao.doctorsList.add(new Doctor("emeline fulton", "pine valley community hospital", "cardiology", "8.00 a.m - 10.00 a.m", 4000.0));
        HealthcareDao.doctorsList.add(new Doctor("jared morris", "willow gardens general hospital", "cardiology", "9.00 a.m - 11.00 a.m", 10000.0));
        HealthcareDao.doctorsList.add(new Doctor("henry foster", "willow gardens general hospital", "paediatric", "8.00 a.m - 10.00 a.m", 10000.0));
    }

    @GET
    @Path("/{category}")
    @Produces({"application/json"})
    public Response getDoctorsByCategory(@PathParam("category") String category) {
        List<Doctor> stock = HealthcareDao.findDoctorByCategory(category);
        new Gson();
        if (stock != null && stock.size() > 0) {
            return Response.status(Response.Status.OK).entity(stock).type("application/json").build();
        } else {
            Status status = new Status("Could not find any entry for the requested Category");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        }
    }

    @GET
    @Path("/appointments/{appointment_id}")
    public Response getAppointment(@PathParam("appointment_id") int id) {
        Appointment appointment = HospitalService.getAppointments().get(id);
        if (appointment != null) {
            return Response.status(Response.Status.OK).entity(appointment).type("application/json").build();
        } else {
            Status status = new Status("Error.There is no appointment with appointment number " + id);
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        }
    }

    @GET
    @Path("/appointments/validity/{appointment_id}/")
    public Response getAppointmentValidityTime(@PathParam("appointment_id") int id) {
        Appointment appointment = HospitalService.getAppointments().get(id);
        long diffDays = 0L;
        if (appointment != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date date = dateFormat.parse(appointment.getAppointmentDate());
                Date toDay = new Date();
                diffDays = (date.getTime() - toDay.getTime()) / 86400000L;
            } catch (ParseException var8) {
                var8.printStackTrace();
            }

            Status status = new Status(String.valueOf(diffDays));
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        } else {
            Status status = new Status("Error.Could not Find the Requested appointment ID");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        }
    }

    @DELETE
    @Path("/appointments/{appointment_id}/")
    public Response removeAppointment(@PathParam("appointment_id") int id) {
        HospitalService.getAppointments().remove(id);
        Status status = new Status("Appointment is successfully removed");
        return Response.status(Response.Status.OK).entity(status).type("application/json").build();
    }

    @POST
    @Path("/payments")
    public Response settlePayment(PaymentSettlement paymentSettlement) {
        new Gson();
        if (paymentSettlement.getAppointmentNumber() >= 0) {
            Payment payment = HealthCareUtil.createNewPaymentEntry(paymentSettlement);
            payment.setStatus("Settled");
            HealthcareDao.payments.put(payment.getPaymentID(), payment);
            return Response.status(Response.Status.OK).entity(payment).type("application/json").build();
        } else {
            Status status = new Status("Error.Could not Find the Requested appointment ID");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        }
    }

    @GET
    @Path("/payments")
    public Response getAllPayments() {
        HashMap payments = HealthcareDao.payments;
        return Response.status(Response.Status.OK).entity(payments).type("application/json").build();
    }

    @GET
    @Path("/payments/payment/{payment_id}")
    public Response getPaymentDetails(@PathParam("payment_id") String payment_id) {
        new Gson();
        Payment payment = HealthcareDao.payments.get(payment_id);
        if (payment != null) {
            return Response.status(Response.Status.OK).entity(payment).type("application/json").build();
        } else {
            Status status = new Status("Invalid payment id provided");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        }
    }

    @POST
    @Path("/admin/newdoctor")
    public Response addNewDoctor(Doctor doctor) {
        String category = doctor.getCategory();
        if (!HealthcareDao.catergories.contains(category)) {
            HealthcareDao.catergories.add(category);
        }

        if (this.findDoctorByName(doctor.getName()) == null) {
            HealthcareDao.doctorsList.add(doctor);
            Status status = new Status("New Doctor Added Successfully");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        } else {
            Status status = new Status("Doctor Already Exist in the system");
            return Response.status(Response.Status.OK).entity(status).type("application/json").build();
        }
    }

    public void fillCategories() {
        HealthcareDao.catergories.add("surgery");
        HealthcareDao.catergories.add("cardiology");
        HealthcareDao.catergories.add("gynaecology");
        HealthcareDao.catergories.add("ent");
        HealthcareDao.catergories.add("paediatric");
    }

    private Doctor findDoctorByName(String name) {
        for(Doctor doctor : HealthcareDao.doctorsList) {
            if (doctor.getName().equals(name)) {
                return doctor;
            }
        }

        return null;
    }
}

