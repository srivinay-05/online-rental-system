package com.example.myapp.service;

import com.example.myapp.model.Booking;
import com.example.myapp.model.Item;
import com.example.myapp.model.User;
import com.example.myapp.repository.BookingRepository;
import com.example.myapp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private EmailService emailService;

    // Create booking
    public Booking createBooking(Booking booking) {
        // Calculate dynamic price
        Double pricePerDay = itemService.calculateDynamicPrice(booking.getItem().getId());

        // Calculate total days
        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        if (days <= 0) throw new RuntimeException("End date must be after start date!");

        // Set total price
        booking.setTotalPrice(pricePerDay * days);
        booking.setStatus(Booking.Status.PENDING);

        // Mark item as unavailable
        Item item = booking.getItem();
        item.setAvailable(false);
        itemRepository.save(item);

        Booking savedBooking = bookingRepository.save(booking);

        // Send booking confirmation email
        try {
            emailService.sendBookingConfirmation(savedBooking);
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }

        return savedBooking;
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get booking by ID
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    // Get bookings by user
    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    // Confirm booking
    public Booking confirmBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found!"));
        booking.setStatus(Booking.Status.CONFIRMED);
        return bookingRepository.save(booking);
    }

    // Cancel booking
    public Booking cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found!"));
        booking.setStatus(Booking.Status.CANCELLED);

        // Mark item as available again
        Item item = booking.getItem();
        item.setAvailable(true);
        itemRepository.save(item);

        Booking cancelledBooking = bookingRepository.save(booking);

        // Send cancellation email
        try {
            emailService.sendBookingCancellation(cancelledBooking);
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }

        return cancelledBooking;
    }

    // Delete booking
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}