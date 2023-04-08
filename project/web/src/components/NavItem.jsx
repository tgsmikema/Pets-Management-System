import { Box, Button, useTheme } from "@mui/material";
import { useNavigate } from "react-router-dom";

//this is navigation item in the top bar, which is home,stats,chat,profile
const NavItem = ({ icon, to, name, selected, setSelected }) => {
  const navigate = useNavigate();
  return (
    <Box>
      <Button
        variant={"text"}
        startIcon={icon}
        onClick={() => {
          setSelected(name);
          navigate(to);
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
