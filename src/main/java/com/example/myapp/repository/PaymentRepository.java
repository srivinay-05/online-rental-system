package com.example.myapp.repository;

import com.example.myapp.model.Payment;
import com.example.myapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUser(User user);
    List<Payment> findByPaymentStatus(Payment.PaymentStatus paymentStatus);
}