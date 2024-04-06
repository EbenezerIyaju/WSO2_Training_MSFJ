package org.training.service.hospital.utils;
import java.util.Calendar;
import org.training.service.hospital.daos.HealthcareDao;
import org.training.service.hospital.daos.Payment;
import org.training.service.hospital.daos.PaymentSettlement;

public class HealthCareUtil {
    public static Payment createNewPaymentEntry(PaymentSettlement paymentSettlement) {
        Payment payment = new Payment();
        String dob = paymentSettlement.getPatient().getDob();
        int discount = checkForDiscounts(dob);
        String doctor = paymentSettlement.getDoctor().getName();
        payment.setActualFee(HealthcareDao.findDoctorByName(doctor).getFee());
        payment.setDiscount(discount);
        double discounted = HealthcareDao.findDoctorByName(doctor).getFee() / 100.0 * (double)(100 - discount);
        payment.setDiscounted(discounted);
        payment.setPatient(paymentSettlement.getPatient().getName());
        return payment;
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
}
