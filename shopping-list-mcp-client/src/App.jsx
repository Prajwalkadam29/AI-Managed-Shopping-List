import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  ShoppingCart,
  Send,
  Trash2,
  CheckCircle,
  ListChecks,
  RefreshCw,
} from "lucide-react";

const API_BASE = "http://localhost:8080";

function App() {
  const [message, setMessage] = useState("");
  const [chatHistory, setChatHistory] = useState([]);
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  // 1. Fetch Items from PostgreSQL
  const fetchItems = async () => {
    try {
      const res = await axios.get(`${API_BASE}/api/items`);
      setItems(res.data);
    } catch (err) {
      console.error("Database fetch failed", err);
    }
  };

  // 2. Real-time Polling (Refreshes list every 3 seconds)
  useEffect(() => {
    fetchItems();
    const interval = setInterval(fetchItems, 3000);
    return () => clearInterval(interval);
  }, []);

  // 3. AI Chat Logic
  const sendMessage = async () => {
    if (!message) return;
    setLoading(true);
    setChatHistory((prev) => [...prev, { role: "user", content: message }]);
    try {
      const response = await axios.get(`${API_BASE}/chat?message=${message}`);
      setChatHistory((prev) => [
        ...prev,
        { role: "ai", content: response.data },
      ]);
      setMessage("");
      fetchItems(); // Immediate refresh after AI action
    } catch (error) {
      setChatHistory((prev) => [
        ...prev,
        { role: "ai", content: "Error: Check backend connection." },
      ]);
    } finally {
      setLoading(false);
    }
  };

  // 4. UI Actions (Manual override)
  const toggleComplete = async (name) => {
    await axios.post(`${API_BASE}/api/items/${name}/complete`);
    fetchItems();
  };

  return (
    <div className="min-h-screen bg-black text-slate-100 p-6 md:p-12 font-sans">
      <div className="max-w-7xl mx-auto grid grid-cols-1 lg:grid-cols-12 gap-8">
        {/* AI Chat Panel (Midnight Style) */}
        <div className="lg:col-span-7 bg-zinc-900 rounded-3xl shadow-2xl flex flex-col h-[750px] border border-zinc-800 overflow-hidden">
          <div className="p-6 bg-indigo-600 text-white flex justify-between items-center shadow-lg">
            <h2 className="font-bold text-xl flex items-center gap-3">
              <ShoppingCart /> AI Assistant
            </h2>
          </div>

          <div className="flex-1 overflow-y-auto p-6 space-y-4 bg-zinc-950/50">
            {chatHistory.map((msg, i) => (
              <div
                key={i}
                className={`flex ${
                  msg.role === "user" ? "justify-end" : "justify-start"
                }`}
              >
                <div
                  className={`max-w-[85%] p-4 rounded-2xl ${
                    msg.role === "user"
                      ? "bg-indigo-600 text-white"
                      : "bg-zinc-800 border border-zinc-700 text-zinc-200"
                  }`}
                >
                  {msg.content}
                </div>
              </div>
            ))}
          </div>

          <div className="p-6 bg-zinc-900 border-t border-zinc-800 flex gap-3">
            <input
              className="flex-1 bg-zinc-800 border border-zinc-700 text-white rounded-2xl px-6 py-4 focus:ring-2 focus:ring-indigo-500 outline-none placeholder-zinc-500"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              placeholder="Ask AI: 'I bought 2 liters of milk'..."
            />
            <button
              onClick={sendMessage}
              className="bg-indigo-600 hover:bg-indigo-500 text-white px-8 rounded-2xl transition-all"
            >
              <Send />
            </button>
          </div>
        </div>

        {/* Live Inventory (Right Side) */}
        <div className="lg:col-span-5 bg-zinc-900 rounded-3xl shadow-2xl p-8 border border-zinc-800">
          <h2 className="text-2xl font-black mb-8 flex items-center gap-3 text-white">
            <ListChecks className="text-indigo-500" /> Live Inventory
          </h2>
          <div className="space-y-4">
            {items.map((item) => (
              <div
                key={item.id}
                className="flex items-center justify-between p-5 bg-zinc-800/50 rounded-2xl border border-zinc-700/50 hover:bg-zinc-800 transition-all"
              >
                <div className="flex items-center gap-4">
                  <CheckCircle
                    className={
                      item.completed ? "text-green-500" : "text-zinc-600"
                    }
                  />
                  <div>
                    <h3
                      className={`text-lg font-bold ${
                        item.completed
                          ? "line-through text-zinc-500"
                          : "text-zinc-100"
                      }`}
                    >
                      {item.name}
                    </h3>
                    <span className="text-xs font-bold text-indigo-400 uppercase tracking-widest">
                      {item.quantity} {item.unit || "units"}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
