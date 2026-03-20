package com.example.myapp.service;

import com.example.myapp.model.Booking;
import com.example.myapp.model.Item;
import com.example.myapp.repository.BookingRepository;
import com.example.myapp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    // Runs every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void updateItemAvailability() {
        System.out.println("Running availability update scheduler...");

        List<Booking> bookings = bookingRepository
            .findByStatus(Booking.Status.CONFIRMED);

        LocalDate today = LocalDate.now();

        for (Booking booking : bookings) {
            if (booking.getEndDate().isBefore(today)) {
                booking.setStatus(Booking.Status.COMPLETED);
                bookingRepository.save(booking);

                Item item = booking.getItem();
                item.setAvailable(true);
                itemRepository.save(item);

                System.out.println("Item " + item.getName() +
                    " is now available again!");
            }
        }
    }

    // Runs every hour
    @Scheduled(fixedRate = 3600000)
    public void checkItemAvailability() {
        List<Booking> bookings = bookingRepository
            .findByStatus(Booking.Status.CONFIRMED);

        LocalDate today = LocalDate.now();

        for (Booking booking : bookings) {
            if (booking.getEndDate().isBefore(today)) {
                booking.setStatus(Booking.Status.COMPLETED);
                bookingRepository.save(booking);

                Item item = booking.getItem();
                item.setAvailable(true);
                itemRepository.save(item);
            }
        }
    }
}