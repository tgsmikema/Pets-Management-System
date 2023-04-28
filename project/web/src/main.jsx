import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import "./index.css";
import { BrowserRouter } from "react-router-dom";
import { AuthProvider } from "./providers/AuthProvider.jsx";
import { theme } from "./theme.js";
import { ThemeProvider } from "@mui/material";
import { UtilProvider } from "./providers/UtilProvider";
import { WebServiceProvider } from "./providers/WebServiceProvider.jsx";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <WebServiceProvider>
          <UtilProvider>
            <ThemeProvider theme={theme}>
              <App />
            </ThemeProvider>
          </UtilProvider>
        </WebServiceProvider>
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
);
