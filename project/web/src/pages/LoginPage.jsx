import { Box, useTheme } from "@mui/material";
import Footer from "../components/Footer.jsx";
import TopBar from "../components/TopBar.jsx";
import LoginForm from "../components/LoginForm.jsx";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
  const navigate = useNavigate();
  const theme = useTheme();
  useEffect(() => {
    navigate("/");
  }, []);
  return (
    <Box height={"100vh"} width={"100vw"}>
      <TopBar isLogin={true} />
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
        <LoginForm />
      </Box>
      <Footer />
    </Box>
  );
};

export default LoginPage;
