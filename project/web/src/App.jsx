import { Box, useTheme } from "@mui/material";
import { useAuth } from "./providers/AuthProvider.jsx";
import { customColor } from "./theme.js";
import LoginPage from "./pages/LoginPage.jsx";
import DashBoard from "./pages/DashBoard.jsx";

function App() {
  const { user } = useAuth();
  const theme = useTheme();
  const colors = customColor;
  return user === null ? <LoginPage /> : <DashBoard />;
}

export default App;
