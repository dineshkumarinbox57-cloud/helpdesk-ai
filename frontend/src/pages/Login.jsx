import { useState } from "react";
import API from "../services/api";
import { useNavigate } from "react-router-dom";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const login = async () => {
  try {

          const response = await API.post("/auth/login", {
            email,
            password
          });

          if (response.data.message === "Login successful") {

            localStorage.setItem(
              "token",
              response.data.token
            );
            localStorage.setItem(
              "email",
              email 
            )

            alert("Login successful");
            navigate("/dashboard");

          } else {

            alert(response.data.message);
          }

        } catch (error) {

          alert("Invalid email or password");
          console.log(error);
        }
      };

       return (
        <div className="login-container">

          <div className="login-card">

            <div className="login-logo">
              🤖
            </div>

            <h1>
              IT Helpdesk <span>AI</span>
            </h1>

            <p>Smart troubleshooting before ticket creation</p>

            <input
              type="email"
              placeholder="📧 Email"
              onChange={(e) => setEmail(e.target.value)}
            />

            <input
              type="password"
              placeholder="🔒 Password"
              onChange={(e) => setPassword(e.target.value)}
            />

            <button onClick={login}>
              🔑 Sign In
            </button>

            <div className="login-footer">
              Don't have an account?
              <a href="/register"> Register</a>
            </div>

          </div>

        </div>
      );

}

export default Login;