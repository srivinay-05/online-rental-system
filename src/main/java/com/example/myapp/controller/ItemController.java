package com.example.myapp.controller;

import com.example.myapp.model.Item;
import com.example.myapp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Show all items
    @GetMapping
    public String showAllItems(Model model) {
        model.addAttribute("items", itemService.getAvailableItems());
        return "items";
    }

    // Show item details
    @GetMapping("/{id}")
    public String showItemDetails(@PathVariable Long id, Model model) {
        itemService.getItemById(id).ifPresent(item -> {
            model.addAttribute("item", item);
            model.addAttribute("dynamicPrice",
                itemService.calculateDynamicPrice(id));
        });
        return "item-details";
    }

    // Search items
    @GetMapping("/search")
    public String searchItems(@RequestParam String name, Model model) {
        model.addAttribute("items", itemService.searchItems(name));
        return "items";
    }

    // Filter by category
    @GetMapping("/category/{category}")
    public String filterByCategory(@PathVariable String category, Model model) {
        model.addAttribute("items",
            itemService.getItemsByCategory(Item.Category.valueOf(category)));
        return "items";
    }

    // Show add item form (Admin)
    @GetMapping("/add")
    public String showAddItemForm(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("categories", Item.Category.values());
        return "add-item";
    }

    // Handle add item form (Admin)
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item) {
        itemService.addItem(item);
        return "redirect:/items";
    }

    // Delete item (Admin)
    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return "redirect:/items";
    }
}
