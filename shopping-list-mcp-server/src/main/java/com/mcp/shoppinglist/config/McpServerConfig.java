package com.mcp.shoppinglist.config;

import com.mcp.shoppinglist.tools.ShoppingListTools;
import org.springframework.ai.tool.method.MethodToolCallbackProvider; // Added .method here
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider shoppingListToolsProvider(ShoppingListTools shoppingListTools) {
        // This registers your tools bean with the Spring AI framework.
        return MethodToolCallbackProvider.builder()
                .toolObjects(shoppingListTools)
                .build();
    }
}