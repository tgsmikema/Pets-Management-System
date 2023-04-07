import { Box, Button, useTheme } from "@mui/material";
import { useNavigate } from "react-router-dom";

const NavItem = ({ icon, to, name, selected, setSelected }) => {
  const navigate = useNavigate();
  const theme = useTheme();
  return (
    <Box>
      <Button
        variant={"text"}
        startIcon={icon}
        onClick={() => {
          navigate(to);
          setSelected(name);
        }}
        color={"success"}
        sx={{
          fontWeight: selected === name ? "700" : "300",
          color: selected === name ? "#000" : "#333",
          fontSize: selected === name ? "1rem" : "0.8rem",
          textDecoration: selected === name ? "underline" : "none",
        }}
      >
        {name}
      </Button>
    </Box>
  );
};

export default NavItem;
