package com.mcp.shoppinglist.controller;

import com.mcp.shoppinglist.domain.ShoppingItem;
import com.mcp.shoppinglist.service.ShoppingListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Ensure React can call this
public class ShoppingListController {

    private final ShoppingListService service;

    @GetMapping
    public List<ShoppingItem> getItems() {
        return service.getAllItems();
    }

    @PostMapping("/{name}/complete")
    public void markAsComplete(@PathVariable String name) {
        service.completeItem(name);
    }

    @DeleteMapping("/{name}")
    public void deleteItem(@PathVariable String name) {
        service.removeItem(name);
    }
}