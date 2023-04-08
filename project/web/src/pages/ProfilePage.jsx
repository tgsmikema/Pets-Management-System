import { Box, Button } from "@mui/material";
import { useAuth } from "../providers/AuthProvider.jsx";
import { useNavigate } from "react-router-dom";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useEffect } from "react";

const ProfilePage = () => {
  const { setSelected } = useUtilProvider();
  const { logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    setSelected("Profile");
  });
  return (
    <Box>
      this is profile Page
      <Button
        variant={"contained"}
        onClick={() => {
          logout();
          navigate("/");
        }}
      >
        Log out
      </Button>
    </Box>
  );
};

export default ProfilePage;
