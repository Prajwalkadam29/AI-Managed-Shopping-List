package com.mcp.shoppinglist.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddItemRequest(String name, Integer quantity, String unit) {}