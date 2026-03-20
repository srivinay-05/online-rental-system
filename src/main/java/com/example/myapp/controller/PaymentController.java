package com.example.myapp.controller;

import com.example.myapp.model.Payment;
import com.example.myapp.model.User;
import com.example.myapp.service.BookingService;
import com.example.myapp.service.PaymentService;
import com.example.myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    // Show Payment Page
    @GetMapping("/pay/{bookingId}")
    public String showPaymentPage(@PathVariable Long bookingId,
                                   Model model, Principal principal) {
        bookingService.getBookingById(bookingId).ifPresent(booking -> {
            model.addAttribute("booking", booking);
            model.addAttribute("paymentMethods", Payment.PaymentMethod.values());
        });
        return "payment";
    }

    // Process Payment
    @PostMapping("/pay/{bookingId}")
    public String processPayment(@PathVariable Long bookingId,
                                  @RequestParam Payment.PaymentMethod paymentMethod,
                                  Principal principal, Model model) {
        try {
            User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found!"));
            Payment payment = paymentService.processPayment(bookingId, user, paymentMethod);
            model.addAttribute("payment", payment);
            return "payment-success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "payment";
        }
    }

    // Show My Payments
    @GetMapping("/my-payments")
    public String showMyPayments(Principal principal, Model model) {
        userService.getUserByEmail(principal.getName()).ifPresent(user -> {
            model.addAttribute("payments", paymentService.getPaymentsByUser(user));
        });
        return "my-payments";
    }

    // Admin - View All Payments
    @GetMapping("/admin/all-payments")
    public String showAllPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "admin-payments";
    }
}