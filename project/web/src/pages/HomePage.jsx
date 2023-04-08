import { Box } from "@mui/material";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useEffect } from "react";

const HomePage = () => {
  const { setSelected } = useUtilProvider();
  useEffect(() => {
    setSelected("Home");
  });
  return <Box>This is Home page</Box>;
};

export default HomePage;
