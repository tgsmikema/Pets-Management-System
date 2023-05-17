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
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";

const AddUser = (props) => {
  const { onClose, allCentres } = props;
  const theme = useTheme();
  const { user } = useAuth();
  const handleCancel = () => {
    onClose();
  };

  const { languageMap } = useLanguageProvider();

  const [userName, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [centre, setCentre] = useState(0);
  const [password, setPassword] = useState("");
  const [userType, setUserType] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");

  const [errorMessage, setErrorMessage] = useState("");

  const checkFillAllInfo = useCallback(() => {
    return !(
      userName === "" ||
      email === "" ||
      centre === 0 ||
      password === "" ||
      userType === "" ||
      firstName === "" ||
      lastName === ""
    );
  }, [userName, email, centre, password, userType, firstName, lastName]);

  const createNewUser = useCallback(() => {
    if (!checkFillAllInfo()) {
      setErrorMessage("Please fill all information");
      return;
    }
    axios
      .post(
        `${constants.backend}/user/register`,
        {
          userName: userName,
          email: email,
          password: password,
          userType: userType,
          firstName: firstName,
          lastName: lastName,
          centreId: centre,
        },
        {
          headers: {
            Authorization: "Basic " + user.token,
          },
        }
      )
      .then((res) => {
        handleCancel();
      })
      .catch((e) => {
        setErrorMessage(
          "The userName already existed,please change another one"
        );
      });
  }, [userName, email, password, userType, firstName, lastName, centre]);

  return (
    <Box
      p={4}
      width={"25vw"}
      sx={{
        border: "5px #8BB6D8  solid",
        borderRadius: "20px",
        backgroundColor: customColor.whiteBackGround,
      }}
      display={"flex"}
      flexDirection={"column"}
      justifyContent={"space-evenly"}
    >
      <Typography variant={"h4"} fontWeight={"700"} paddingBottom={"5px"}>
        New User
      </Typography>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          UserName:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          onChange={(e) => setUserName(e.target.value)}
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          Email:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          onChange={(e) => setEmail(e.target.value)}
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          Password:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          type={"password"}
          onChange={(e) => setPassword(e.target.value)}
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          FirstName:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          onChange={(e) => setFirstName(e.target.value)}
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          LastName:
        </Typography>
      </Box>
      <Box>
        <TextField
          onChange={(e) => setLastName(e.target.value)}
          fullWidth
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          Centre:
        </Typography>
      </Box>
      <Box>
        <Select
          fullWidth
          defaultValue={""}
          sx={{ backgroundColor: theme.palette.secondary.main, height: "45px" }}
        >
          {allCentres?.map((it, index) => (
            <MenuItem
              key={index}
              value={it}
              onClick={() => {
                setCentre(index + 1);
              }}
            >
              {it}
            </MenuItem>
          ))}
        </Select>
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          Access Level:
        </Typography>
      </Box>
      <Box>
        <Select
          fullWidth
          defaultValue={""}
          sx={{ backgroundColor: theme.palette.secondary.main, height: "45px" }}
        >
          <MenuItem
            value={"volunteer"}
            onClick={() => {
              setUserType("volunteer");
            }}
          >
            volunteer
          </MenuItem>
          <MenuItem
            value={"vet"}
            onClick={() => {
              setUserType("vet");
            }}
          >
            vet
          </MenuItem>
          <MenuItem
            value={"admin"}
            onClick={() => {
              setUserType("admin");
            }}
          >
            admin
          </MenuItem>
        </Select>
      </Box>
      <Box>
        <Typography variant={"body1"} color={"red"} pt={2}>
          {errorMessage}
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
            "&:hover": {
              backgroundColor: theme.palette.primary.main,
            },
          }}
          onClick={handleCancel}
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
          onClick={createNewUser}
        >
          {languageMap.Create}
        </Button>
      </Box>
    </Box>
  );
};

export default AddUser;
