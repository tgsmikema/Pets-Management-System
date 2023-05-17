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

const LoginForm = (props) => {
  const { onClose, allCentres } = props;
  const theme = useTheme();
  const { user } = useAuth();
  const { languageMap } = useLanguageProvider();

  const [dogName, setDogName] = useState("");
  const [dogBreed, setDogBreed] = useState("");
  const [centreId, setCentreId] = useState(0);

  const [errorDisplay, setErrorDisplay] = useState("none");

  const handleCancel = () => {
    onClose();
  };

  //create a dog request
  const createDog = useCallback(async () => {
    if (dogName.trim() === "" || dogBreed.trim() === "" || centreId === 0) {
      setErrorDisplay("block");
      return;
    }
    onClose();
    const res = await axios.post(
      `${constants.backend}/dog/register`,
      {
        name: dogName,
        breed: dogBreed,
        centreId: centreId,
      },
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
  }, [dogBreed, dogName, centreId]);
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
        {languageMap.NewDog}
      </Typography>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          {languageMap.Name}:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          onChange={(e) => setDogName(e.target.value)}
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          {languageMap.Breed}:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          onChange={(e) => setDogBreed(e.target.value)}
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          {languageMap.location}:
        </Typography>
      </Box>
      <Box>
        <Select
          defaultValue={""}
          fullWidth
          sx={{ backgroundColor: theme.palette.secondary.main, height: "45px" }}
        >
          {/* TODO: replace names */}
          {allCentres.map((it, index) => (
            <MenuItem
              key={index}
              value={it}
              onClick={() => {
                setCentreId(index + 1);
              }}
            >
              {it}
            </MenuItem>
          ))}
        </Select>
      </Box>
      <Box>
        <Typography
          variant={"body2"}
          fontWeight={"500"}
          color={"red"}
          pt={2}
          display={errorDisplay}
        >
          Please fill all information
        </Typography>
      </Box>
      <Box
        display={"flex"}
        justifyContent={"space-between"}
        paddingTop={"20px"}
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
          onClick={createDog}
        >
          {languageMap.Create}
        </Button>
      </Box>
    </Box>
  );
};

export default LoginForm;
