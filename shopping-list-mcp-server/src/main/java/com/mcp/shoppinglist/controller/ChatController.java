package com.mcp.shoppinglist.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = builder
                .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                // SYSTEM PROMPT: Forces the AI to use tools as the source of truth
                .defaultSystem("""
                    You are a precise Inventory Manager.
                    
                    OPERATIONAL RULES:
                    1. If the user mentions buying a SPECIFIC AMOUNT (e.g., 'I bought 2 liters'), ALWAYS use 'deductItem'.
                    2. If the user says they finished or bought the WHOLE item (e.g., 'I got the sugar'), use 'completeItem'.
                    3. If an item reaches 0 quantity after deduction, the tool will automatically remove it.
                    4. Always use 'listItems' if you are unsure about what's currently in the database.
                    """)
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}