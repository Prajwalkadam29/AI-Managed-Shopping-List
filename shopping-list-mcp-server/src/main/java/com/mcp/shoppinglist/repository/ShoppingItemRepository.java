package com.mcp.shoppinglist.repository;

import com.mcp.shoppinglist.domain.ShoppingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long> {
    Optional<ShoppingItem> findByNameIgnoreCase(String name);
    void deleteByNameIgnoreCase(String name);
    @Modifying
    @Query("DELETE FROM ShoppingItem s WHERE s.completed = true")
    int deleteCompletedItems();
}