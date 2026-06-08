import { useState } from "react";
import API from "../services/api";

function CreateTicket() {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [category, setCategory] = useState("");
  const [priority, setPriority] = useState("");
  
  const createTicket = async () => {
    try {
      await API.post("/tickets", {
        title,
        description,
        category,
        priority,
        status: "OPEN",
        email,
      });

      alert("Ticket Created Successfully");
    } catch (err) {
      alert("Failed to create ticket");
    }
  };
  const email = localStorage.getItem("email")

return (
  <div className="create-support-page">
    <div className="create-support-box">

      <h2 className="create-support-title">
        🎫 Create Support Ticket
      </h2>

      <p className="create-support-text">
        Submit a new support request
      </p>

      <input
        className="create-support-input"
        placeholder="Title"
        onChange={(e) => setTitle(e.target.value)}
      />

      <input
        className="create-support-input"
        placeholder="Description"
        onChange={(e) => setDescription(e.target.value)}
      />

      <input
        className="create-support-input"
        placeholder="Category"
        onChange={(e) => setCategory(e.target.value)}
      />

      <input
        className="create-support-input"
        placeholder="Priority"
        onChange={(e) => setPriority(e.target.value)}
      />

      <button
        className="create-support-btn"
        onClick={createTicket}
      >
        Create Ticket
      </button>

    </div>
  </div>
);
    
}

export default CreateTicket;