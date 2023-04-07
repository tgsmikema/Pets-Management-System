import { Box, useTheme } from "@mui/material";
import Footer from "../components/Footer.jsx";
import TopBar from "../components/TopBar.jsx";
import LoginForm from "../components/LoginForm.jsx";

const LoginPage = () => {
  const theme = useTheme();
  return (
    <Box height={"100vh"}>
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
