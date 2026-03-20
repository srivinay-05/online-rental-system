package com.example.myapp.service;

import com.example.myapp.model.Item;
import com.example.myapp.repository.ItemRepository;
import com.example.myapp.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Add new item
    public Item addItem(Item item) {
        item.setCurrentPrice(item.getBasePrice());
        return itemRepository.save(item);
    }

    // Get all items
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // Get available items
    public List<Item> getAvailableItems() {
        return itemRepository.findByAvailableTrue();
    }

    // Get item by ID
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    // Get items by category
    public List<Item> getItemsByCategory(Item.Category category) {
        return itemRepository.findByCategory(category);
    }

    // Search items by name
    public List<Item> searchItems(String name) {
        return itemRepository.findByNameContainingIgnoreCase(name);
    }

    // Update item
    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    // Delete item
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    // Dynamic Pricing Logic
    public Double calculateDynamicPrice(Long itemId) {
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (itemOpt.isEmpty()) return 0.0;

        Item item = itemOpt.get();
        Double basePrice = item.getBasePrice();

        // Count active bookings for this item
        long activeBookings = bookingRepository.findAll()
            .stream()
            .filter(b -> b.getItem().getId().equals(itemId))
            .filter(b -> b.getStatus() == com.example.myapp.model.Booking.Status.CONFIRMED)
            .count();

        // Increase price based on demand
        double dynamicPrice;
        if (activeBookings >= 10) {
            dynamicPrice = basePrice * 1.5; // 50% increase
        } else if (activeBookings >= 5) {
            dynamicPrice = basePrice * 1.25; // 25% increase
        } else {
            dynamicPrice = basePrice; // Normal price
        }

        // Save updated price
        item.setCurrentPrice(dynamicPrice);
        itemRepository.save(item);

        return dynamicPrice;
    }
}
