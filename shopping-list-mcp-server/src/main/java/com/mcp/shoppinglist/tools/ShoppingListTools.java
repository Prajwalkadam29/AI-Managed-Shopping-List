package com.mcp.shoppinglist.tools;

import com.mcp.shoppinglist.domain.ShoppingItem;
import com.mcp.shoppinglist.service.ShoppingListService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("shoppingListTools")
public class ShoppingListTools {

    private final ShoppingListService shoppingListService;

    public ShoppingListTools(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @Tool(description = "Get all items currently in the shopping list")
    public String listItems() {
        List<ShoppingItem> items = shoppingListService.getAllItems();
        if (items.isEmpty()) return "The shopping list is currently empty.";

        return items.stream()
                .map(item -> String.format("- %s: %d %s [%s]",
                        item.getName(), item.getQuantity(),
                        item.getUnit() != null ? item.getUnit() : "units",
                        item.isCompleted() ? "COMPLETED" : "PENDING"))
                .collect(Collectors.joining("\n"));
    }

    @Tool(description = "Add a new item. Provide 'name', 'quantity', and 'unit' (e.g., liters, kg).")
    public String addItem(String name, Integer quantity, String unit) {
        int qty = (quantity == null) ? 1 : quantity;
        ShoppingItem saved = shoppingListService.addItem(name, qty, unit);
        return String.format("Added %d %s of %s. Total now: %d.", qty, unit, name, saved.getQuantity());
    }

    // FIXED: Removed RemoveItemRequest DTO and used flat String parameter
    @Tool(description = "Permanently remove an item from the list by its name.")
    public String removeItem(String name) {
        shoppingListService.removeItem(name);
        return String.format("Removed '%s' from the shopping list.", name);
    }

    @Tool(description = "Deduct a portion of an item's quantity. Example: 'I bought 1kg of the 5kg sugar'.")
    public String deductItem(String name, Integer amount) {
        return shoppingListService.deductItem(name, amount);
    }

    @Tool(description = "Mark an item as fully completed/bought. Use this only if the user bought the total amount.")
    public String completeItem(String name) {
        try {
            shoppingListService.completeItem(name);
            return "Marked " + name + " as completed.";
        } catch (Exception e) { return e.getMessage(); }
    }

    @Tool(description = "Remove all items from the shopping list that are marked as completed")
    public String clearCompleted() {
        int count = shoppingListService.clearCompleted();
        if (count == 0) return "No completed items found to remove.";
        return String.format("Successfully cleared %d completed items from the list.", count);
    }
}