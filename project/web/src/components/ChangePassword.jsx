import { Box, Button, TextField, Typography, useTheme } from "@mui/material";
import { customColor } from "../theme.js";
import { useCallback, useState } from "react";
import { useAuth } from "../providers/AuthProvider.jsx";
import axios from "axios";
import { constants } from "../constants.js";
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";

const ChangePassword = ({ handleClose }) => {
  const theme = useTheme();
  const { user, setUser } = useAuth();

  const [errorMessage, setErrorMessage] = useState("");
  const [password, setPassword] = useState("");
  const [oldPassword, setOldPassword] = useState("");
  const [confirmedPassword, setConfirmedPassword] = useState("");

  const { languageMap } = useLanguageProvider();

  const changePassword = useCallback(() => {
    if (
      password.trim() === "" ||
      confirmedPassword.trim() === "" ||
      oldPassword.trim() === ""
    ) {
      setErrorMessage("Please fill all information");
    } else if (password.trim() !== confirmedPassword.trim()) {
      setErrorMessage("The password is not same as confirmed password");
    } else {
      axios
        .post(
          `${constants.backend}/user/changePassword`,
          {
            oldPassword: oldPassword,
            newPassword: password,
          },
          {
            headers: {
              Authorization: "Basic " + user.token,
            },
          }
        )
        .then((res) => {
          setUser({ ...user, token: res.data });
          handleClose();
        })
        .catch((e) => {
          setErrorMessage("Your old password is not correct");
        });
    }
  }, [password, confirmedPassword]);

  return (
    <Box
      p={4}
      sx={{
        height: "100%",
        border: "5px #8BB6D8  solid",
        borderRadius: "20px",
        backgroundColor: customColor.whiteBackGround,
      }}
      display={"flex"}
      flexDirection={"column"}
      justifyContent={"space-evenly"}
    >
      <Typography variant={"h4"} fontWeight={"600"} paddingBottom={"5px"}>
        {languageMap.ChangePassword}
      </Typography>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          Old Password:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          type={"password"}
          size={"small"}
          onChange={(e) => setOldPassword(e.target.value)}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          New Password:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          type={"password"}
          size={"small"}
          onChange={(e) => setPassword(e.target.value)}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          Confirmed Password:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          onChange={(e) => setConfirmedPassword(e.target.value)}
          type={"password"}
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"body1"} color={"red"}>
          {errorMessage.trim().length !== 0 && errorMessage}
        </Typography>
      </Box>
      <Box
        display={"flex"}
        justifyContent={"space-between"}
        paddingTop={"10px"}
      >
        <Button
          variant={"contained"}
          sx={{
            fontWeight: "700",
            fontSize: "0.8rem",
            backgroundColor: theme.palette.error.main,
            "&:hover": {
              backgroundColor: theme.palette.primary.main,
            },
          }}
          onClick={handleClose}
        >
          {languageMap.Cancel}
        </Button>
        <Button
          variant={"contained"}
          sx={{
            fontWeight: "700",
            fontSize: "0.8rem",
            "&:hover": {
              backgroundColor: theme.palette.primary.main,
            },
          }}
          onClick={changePassword}
        >
          {languageMap.Save}
        </Button>
      </Box>
    </Box>
  );
};

export default ChangePassword;
