# API Endpoints Documentation

## 1. AI Chat Endpoint (Proxy to Groq)
Used by the frontend to send natural language commands to the AI.

- **URL**: `/chat`
- **Method**: `GET`
- **Query Params**: `message` (String)
- **Response**: `200 OK` (Plain Text - AI Response)

## 2. Inventory Management (REST)
Direct database access used by the Live Inventory panel.

### Get All Items
- **URL**: `/api/items`
- **Method**: `GET`
- **Response**: `List<ShoppingItem>`

### Mark as Complete
- **URL**: `/api/items/{name}/complete`
- **Method**: `POST`
- **Response**: `200 OK`

### Delete Item
- **URL**: `/api/items/{name}`
- **Method**: `DELETE`
- **Response**: `200 OK`

## 3. MCP SSE Protocol
The protocol layer for connecting external AI clients (like Claude Desktop).

- **SSE Stream**: `GET /sse`
- **Message Endpoint**: `POST /mcp/message`