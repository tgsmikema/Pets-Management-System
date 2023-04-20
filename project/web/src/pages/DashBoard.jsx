import { Box, useTheme } from "@mui/material";
import TopBar from "../components/TopBar.jsx";
import Footer from "../components/Footer.jsx";
import { useAuth } from "../providers/AuthProvider.jsx";
import { Routes, Route, Navigate } from "react-router-dom";
import HomePage from "./HomePage.jsx";
import StatsPage from "./StatsPage.jsx";
import ChatPage from "./ChatPage.jsx";
import ProfilePage from "./ProfilePage.jsx";
import DogPage from "./DogPage.jsx";

//the dahsboard pages is used for router controller, the functionality is same as the pageLayout
const DashBoard = () => {
  const theme = useTheme();
  const { user } = useAuth();

  return (
    <Box height={"100vh"} width={"100vw"}>
      <TopBar isLogin={false} />
      <Box
        height={"84%"}
        width={"100%"}
        sx={{
          backgroundColor: theme.palette.secondary.main,
        }}
      >
        <Routes>
          <Route index element={<Navigate to={"/home"} />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/stats" element={<StatsPage />} />
          <Route path="/chat" element={<ChatPage />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/dogs/:id" element={<DogPage />} />
        </Routes>
      </Box>
      <Footer />
    </Box>
  );
};

export default DashBoard;
