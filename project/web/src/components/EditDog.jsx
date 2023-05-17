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
import { useAuth } from "../providers/AuthProvider.jsx";
import { useWebService } from "../providers/WebServiceProvider.jsx";
import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { constants } from "../constants.js";
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";

const EditDog = ({ onClose, dog }) => {
  const theme = useTheme();
  //TODO: replace hardcoded values with db
  const { name, id, breed, centreId } = dog;

  const { languageMap } = useLanguageProvider();

  const { user } = useAuth();
  const { allCentreForAllUser } = useWebService();
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const [dogCentreId, setDogCentreId] = useState(centreId);
  const [dogName, setDogName] = useState(name);
  const [dogBreed, setDogBreed] = useState(breed);

  const saveDogInfo = useCallback(async () => {
    const res = await axios.post(
      `${constants.backend}/dog/edit`,
      {
        id: id,
        name: dogName,
        breed: dogBreed,
        centreId: dogCentreId,
      },
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
  }, [id, dogName, dogBreed, dogCentreId, user]);

  const deleteDog = useCallback(async () => {
    const res = await axios.delete(
      `${constants.backend}/dog/delete?dogId=${id}`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
  }, [id, user]);

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
        {languageMap.EditDog}
      </Typography>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          {languageMap.Name}:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          defaultValue={name}
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
          onChange={(e) => setDogBreed(e.target.value)}
          fullWidth
          size={"small"}
          defaultValue={breed}
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
          defaultValue={
            allCentreForAllUser.find((it) => it.id === centreId).name
          }
          fullWidth="true"
          sx={{ backgroundColor: theme.palette.secondary.main, height: "45px" }}
        >
          {allCentreForAllUser?.map((it, index) => (
            <MenuItem
              key={index}
              value={it.name}
              onClick={() => {
                setDogCentreId(it.id);
              }}
            >
              {it.name}
            </MenuItem>
          ))}
        </Select>
      </Box>
      <Box pt={1.5}>
        <Typography variant={"body1"} color={"red"}>
          {errorMessage.trim().length !== 0 && errorMessage}
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
            backgroundColor: theme.palette.error.main,
            "&:hover": {
              backgroundColor: theme.palette.primary.main,
            },
          }}
          onClick={async () => {
            if (user.userType === "volunteer") {
              setErrorMessage("You can not delete a dog");
              return;
            }
            await deleteDog();
            onClose();
            navigate("/");
          }}
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
          onClick={async () => {
            if (
              dogName.trim().length === 0 ||
              dogBreed.trim().length === 0 ||
              dogCentreId === 0
            ) {
              setErrorMessage("please fill all information");
              return;
            }
            await saveDogInfo();
            onClose();
          }}
        >
          {languageMap.Save}
        </Button>
      </Box>
    </Box>
  );
};

export default EditDog;
