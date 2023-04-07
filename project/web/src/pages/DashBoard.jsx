import { Box, useTheme } from "@mui/material";
import TopBar from "../components/TopBar.jsx";
import Footer from "../components/Footer.jsx";
import { useEffect } from "react";
import { useAuth } from "../providers/AuthProvider.jsx";
import axios from "axios";

const DashBoard = () => {
  const theme = useTheme();
  const { user, token } = useAuth();

  useEffect(() => {}, []);

  return (
    <Box height={"100vh"}>
      <TopBar isLogin={false} />
      <Box
        height={"84%"}
        width={"100%"}
        sx={{
          backgroundColor: theme.palette.secondary.main,
        }}
        display={"flex"}
        alignItems={"center"}
        justifyContent={"center"}
      >
        this is dashboard
      </Box>
      <Footer />
    </Box>
  );
};

export default DashBoard;
