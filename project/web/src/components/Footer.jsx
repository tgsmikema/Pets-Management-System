import { Box, useTheme } from "@mui/material";

const Footer = () => {
  const theme = useTheme();
  return (
    <Box
      height={"8%"}
      width={"100%"}
      sx={{
        backgroundColor: theme.palette.primary.dark,
      }}
    ></Box>
  );
};

export default Footer;
