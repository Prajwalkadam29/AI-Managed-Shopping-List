package com.mcp.shoppinglist.service;

import com.mcp.shoppinglist.domain.ShoppingItem;
import com.mcp.shoppinglist.repository.ShoppingItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingListService {

    private final ShoppingItemRepository repository;

    @Transactional(readOnly = true)
    public List<ShoppingItem> getAllItems() {
        return repository.findAll();
    }


    @Transactional
    public ShoppingItem addItem(String name, Integer quantity, String unit) {
        return repository.findByNameIgnoreCase(name)
                .map(item -> {
                    item.setQuantity(item.getQuantity() + quantity);
                    if (unit != null) item.setUnit(unit);
                    return repository.save(item);
                })
                .orElseGet(() -> repository.save(
                        ShoppingItem.builder()
                                .name(name)
                                .quantity(quantity)
                                .unit(unit)
                                .build()
                ));
    }

    @Transactional
    public String deductItem(String name, Integer amount) {
        return repository.findByNameIgnoreCase(name)
                .map(item -> {
                    int newQty = item.getQuantity() - amount;
                    if (newQty <= 0) {
                        repository.delete(item);
                        return "Removed " + name + " completely from list.";
                    } else {
                        item.setQuantity(newQty);
                        repository.save(item);
                        return "Deducted " + amount + ". Remaining: " + newQty + " " + (item.getUnit() != null ? item.getUnit() : "");
                    }
                })
                .orElse("Item not found.");
    }

    @Transactional
    public void removeItem(String name) {
        repository.deleteByNameIgnoreCase(name);
    }

    @Transactional
    public ShoppingItem completeItem(String name) {
        return repository.findByNameIgnoreCase(name)
                .map(item -> {
                    item.setCompleted(true);
                    return repository.save(item);
                })
                .orElseThrow(() -> new RuntimeException("Item not found: " + name));
    }

    @Transactional
    public int clearCompleted() {
        return repository.deleteCompletedItems();
    }
}