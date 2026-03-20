package com.example.myapp.controller;

import com.example.myapp.service.BookingService;
import com.example.myapp.service.ItemService;
import com.example.myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingService bookingService;

    // Admin Dashboard
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalItems", itemService.getAllItems().size());
        model.addAttribute("totalBookings", bookingService.getAllBookings().size());
        model.addAttribute("recentBookings", bookingService.getAllBookings());
        return "admin-dashboard";
    }

    // Manage Users
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin-users";
    }

    // Delete User
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    // Manage Bookings
    @GetMapping("/bookings")
    public String manageBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin-bookings";
    }

    // Confirm Booking
    @GetMapping("/bookings/confirm/{id}")
    public String confirmBooking(@PathVariable Long id) {
        bookingService.confirmBooking(id);
        return "redirect:/admin/bookings";
    }

    // Cancel Booking
    @GetMapping("/bookings/cancel/{id}")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/admin/bookings";
    }
    // Make User Admin
@GetMapping("/users/make-admin/{id}")
public String makeAdmin(@PathVariable Long id) {
    userService.makeAdmin(id);
    return "redirect:/admin/users";
}
}