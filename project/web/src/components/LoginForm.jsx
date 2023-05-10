import { Box, Button, TextField, Typography, useTheme } from "@mui/material";
import { customColor } from "../theme.js";
import { useAuth } from "../providers/AuthProvider.jsx";
import { useState } from "react";

const LoginForm = () => {
  const theme = useTheme();
  const { login } = useAuth();
  const [userName, setName] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [messageColor, setMessageColor] = useState(theme.palette.error.main);

  return (
    <Box
      p={4}
      height={"60%"}
      width={"25%"}
      sx={{
        border: "7px #8BB6D8  solid",
        borderRadius: "30px",
        backgroundColor: customColor.whiteBackGround,
      }}
      display={"flex"}
      flexDirection={"column"}
      justifyContent={"space-evenly"}
    >
      <Box>
        <Typography variant={"h5"} fontWeight={"800"}>
          Username:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
          onChange={(e) => setName(e.target.value)}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"800"}>
          Password:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          size={"small"}
          type={"password"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
          onChange={(e) => setPassword(e.target.value)}
        />
      </Box>
      <Box display={"flex"} justifyContent={"space-between"}>
        <Box>
          <Typography variant={"body2"} color={messageColor}>
            {message}
          </Typography>
        </Box>
        <Box>
          <Button
            variant={"contained"}
            sx={{
              fontWeight: "700",
              fontSize: "0.8rem",
              "&:hover": {
                backgroundColor: theme.palette.primary.main,
              },
            }}
            onClick={async () => {
              if (userName === "") {
                setMessage("Please enter your username");
                setMessageColor(theme.palette.error.main);
              } else if (password === "") {
                setMessage("Please enter your password");
                setMessageColor(theme.palette.error.main);
              } else {
                setMessage("Loading...");
                setMessageColor("black");
                try {
                  await login(userName, password);
                  setMessageColor("green");
                  setMessage("Login Successfully");
                } catch (e) {
                  setMessageColor(theme.palette.error.main);
                  setMessage("Username or password is incorrect");
                }
              }
            }}
          >
            Log in
          </Button>
        </Box>
      </Box>
    </Box>
  );
};

export default LoginForm;
