import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './index.css'
import {BrowserRouter} from "react-router-dom";
import {AuthProvider} from "./providers/AuthProvider.jsx";
import {theme} from "./theme.js";
import {ThemeProvider} from "@mui/material";

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <BrowserRouter>
            <ThemeProvider value={theme}>
                <AuthProvider>
                    <App/>
                </AuthProvider>
            </ThemeProvider>
        </BrowserRouter>
    </React.StrictMode>,
)
