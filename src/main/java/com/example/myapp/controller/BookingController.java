package com.example.myapp.controller;

import com.example.myapp.model.Booking;
import com.example.myapp.model.Item;
import com.example.myapp.model.User;
import com.example.myapp.service.BookingService;
import com.example.myapp.service.ItemService;
import com.example.myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    // Show booking form
    @GetMapping("/book/{itemId}")
    public String showBookingForm(@PathVariable Long itemId, Model model) {
        itemService.getItemById(itemId).ifPresent(item -> {
            model.addAttribute("item", item);
            model.addAttribute("booking", new Booking());
            model.addAttribute("dynamicPrice",
                itemService.calculateDynamicPrice(itemId));
        });
        return "booking-form";
    }

    // Handle booking form
    @PostMapping("/book/{itemId}")
    public String createBooking(@PathVariable Long itemId,
                                 @ModelAttribute Booking booking,
                                 Principal principal, Model model) {
        try {
            User user = userService.getUserByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found!"));
            Item item = itemService.getItemById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found!"));

            booking.setUser(user);
            booking.setItem(item);
            bookingService.createBooking(booking);
            return "redirect:/bookings/my-bookings";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "booking-form";
        }
    }

    // Show my bookings
    @GetMapping("/my-bookings")
    public String showMyBookings(Principal principal, Model model) {
        userService.getUserByEmail(principal.getName()).ifPresent(user -> {
            model.addAttribute("bookings", bookingService.getBookingsByUser(user));
        });
        return "my-bookings";
    }

    // Cancel booking
    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/bookings/my-bookings";
    }
}
