import { Box } from "@mui/material";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useEffect } from "react";

const StatsPage = () => {
  const { setSelected } = useUtilProvider();
  useEffect(() => {
    setSelected("Stats");
  });
  return <Box>this is statistic page</Box>;
};

export default StatsPage;
