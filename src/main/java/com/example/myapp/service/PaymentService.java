package com.example.myapp.service;

import com.example.myapp.model.Booking;
import com.example.myapp.model.Payment;
import com.example.myapp.model.User;
import com.example.myapp.repository.BookingRepository;
import com.example.myapp.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    // Process Payment
    public Payment processPayment(Long bookingId, User user,
                                   Payment.PaymentMethod paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found!"));

        // Create payment
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setUser(user);
        payment.setAmount(booking.getTotalPrice());
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentStatus(Payment.PaymentStatus.SUCCESS);

        // Confirm booking after payment
        booking.setStatus(Booking.Status.CONFIRMED);
        bookingRepository.save(booking);

        Payment savedPayment = paymentRepository.save(payment);

        // Send payment confirmation email
        try {
            emailService.sendPaymentConfirmation(savedPayment);
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }

        return savedPayment;
    }

    // Get payments by user
    public List<Payment> getPaymentsByUser(User user) {
        return paymentRepository.findByUser(user);
    }

    // Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
