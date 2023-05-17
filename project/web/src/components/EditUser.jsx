import {
  Box,
  Button,
  TextField,
  Typography,
  useTheme,
  Select,
  MenuItem,
} from "@mui/material";
import { customColor } from "../theme.js";
import { useCallback, useState } from "react";
import axios from "axios";
import { constants } from "../constants.js";
import { useAuth } from "../providers/AuthProvider.jsx";
import { useLanguageProvider } from '../providers/LanguageProvider.jsx';

const EditUser = ({ onClose, selectedRow }) => {
  const name = selectedRow
    ? selectedRow.firstName + " " + selectedRow.lastName
    : "";
  const accessLevel = selectedRow ? selectedRow.userType.trim() : "";
  const id = selectedRow?.id;
  const theme = useTheme();
  const { languageMap } = useLanguageProvider();


  const [userType, setUserType] = useState(accessLevel);
  const { user } = useAuth();
  const [errorMessage, setErrorMessage] = useState("");

  const saveChange = useCallback(async () => {
    if (id === user.id) {
      setErrorMessage("You can not edit yourself");
      return;
    }
    await axios
      .post(
        `${constants.backend}/user/editUserAccess`,
        {
          userId: id,
          userType: userType,
        },
        {
          headers: {
            Authorization: "Basic " + user.token,
          },
        }
      )
      .then((res) => {
        onClose();
      });
  }, [id, userType]);

  const deleteUser = useCallback(async () => {
    if (id === user.id) {
      setErrorMessage("You can not delete yourself");
      return;
    }
    if (userType === "admin") {
      setErrorMessage("You can not delete a admin user");
      return;
    }

    await axios.delete(`${constants.backend}/user/delete?userId=${id}`, {
      headers: {
        Authorization: "Basic " + user.token,
      },
    });
    onClose();
  }, [id, userType]);

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
      <Typography variant={"h3"} fontWeight={"700"} paddingBottom={"5px"}>
        Edit User
      </Typography>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          {languageMap.Name}:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          value={name}
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          {languageMap.AccessLevel}:
        </Typography>
      </Box>
      <Box>
        <Select
          fullWidth
          value={userType}
          sx={{ backgroundColor: theme.palette.secondary.main, height: "45px" }}
        >
          <MenuItem
            value={"volunteer"}
            onClick={() => setUserType("volunteer")}
          >
            volunteer
          </MenuItem>
          <MenuItem value={"vet"} onClick={() => setUserType("vet")}>
            vet
          </MenuItem>
          <MenuItem value={"admin"} onClick={() => setUserType("admin")}>
            admin
          </MenuItem>
        </Select>
      </Box>
      <Box>
        <Typography variant={"body1"} fontWeight={"500"} color={"red"}>
          {errorMessage.trim().length !== 0 && errorMessage}
        </Typography>
      </Box>
      <Box
        display={"flex"}
        justifyContent={"space-between"}
        paddingTop={"30px"}
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
          onClick={deleteUser}
        >
          {languageMap.Delete}
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
          onClick={saveChange}
        >
          {languageMap.Save}
        </Button>
      </Box>
    </Box>
  );
};

export default EditUser;
