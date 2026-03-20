package com.example.myapp.repository;

import com.example.myapp.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCategory(Item.Category category);
    List<Item> findByAvailableTrue();
    List<Item> findByNameContainingIgnoreCase(String name);
}

