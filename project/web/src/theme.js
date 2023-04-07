import { createTheme } from "@mui/material";

export const theme = createTheme({
  palette: {
    primary: {
      light: "#caddec",
      main: "#8BB6D8",
      dark: "#044977",
    },
    secondary: {
      main: "#FAF5F1",
    },
    success: {
      main: "#000",
    },
    error: {
      main: "#E34B28",
    },
  },
});

//you can add this color later
export const customColor = {
  grey: "#eee",
  textBlack: "000",
  whiteBackGround: "#fff",
};
