import {
  Box,
  Button,
  TextField,
  Typography,
  useTheme,
  Select,
  MenuItem,
} from "@mui/material";
import { useCallback, useEffect, useState } from "react";
import { customColor } from "../theme.js";
import ProcessLoading from "./ProcessLoading.jsx";
import axios from "axios";
import { constants } from "../constants";
import { useAuth } from "../providers/AuthProvider.jsx";
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";

const Weight = ({ onClose, scaleList, dog }) => {
  const [screen, setScreen] = useState("init");
  const theme = useTheme();
  const { user } = useAuth();
  const { id, name, centreId } = dog;

  const { languageMap } = useLanguageProvider();

  const [scaleId, setScaleId] = useState(1);

  const [timeIntervalId, setTimeIntervalId] = useState(0);

  const handleCancel = () => {
    onClose();
  };

  const [dogWeight, setDogWeight] = useState(0);

  const fetchDogWeight = useCallback(async () => {
    const res = await axios.get(
      `${constants.backend}/dog/getCurrentWeightFromScale?dogId=${id}`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
    return res.data;
  }, [dogWeight, id, user]);

  const invokeScaleRequest = useCallback(async () => {
    const res = await axios.post(
      `${constants.backend}/dog/invokeScaleRequest`,
      {
        dogId: id,
        scaleId: scaleId,
      },
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
  }, [scaleId, user, id]);

  const saveWeight = useCallback(async () => {
    const res = await axios.get(
      `${constants.backend}/dog/saveCurrentWeight?dogId=${id}`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
  }, [user, id]);

  function setIntervalToFetch() {
    setTimeIntervalId(
      setInterval(async () => {
        const data = await fetchDogWeight();
        console.log(data.weight);
        if (data.weight !== 0) {
          clearInterval(timeIntervalId);
          console.log(data.weight);
          setDogWeight(data.weight);
        }
      }, 2000)
    );
  }

  function clearFetchInterval() {
    clearInterval(timeIntervalId);
  }

  const init_screen = (
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
        {languageMap.AddWeight}
      </Typography>
      <Typography variant={"h5"} fontWeight={"700"}>
        Place {name} on the scales
      </Typography>
      <Box>
        <Select
          fullWidth
          value={scaleList.find((it) => it.id === scaleId).name}
          sx={{ backgroundColor: theme.palette.secondary.main, height: "45px" }}
        >
          {scaleList.map((it, index) => (
            <MenuItem
              value={it.name}
              key={index}
              onClick={() => {
                setScaleId(it.id);
              }}
            >
              {it.name}
            </MenuItem>
          ))}
        </Select>
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
          onClick={() => {
            handleCancel();
          }}
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
          onClick={async () => {
            setScreen("weight");
            await invokeScaleRequest();
            setIntervalToFetch();
          }}
        >
          {languageMap.AddWeight}
        </Button>
      </Box>
    </Box>
  );

  const weight_screen = (
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
        {languageMap.AddWeight}
      </Typography>
      <Typography variant={"h5"} fontWeight={"700"} alignSelf={"center"}>
        Place {name} on {`scale ${scaleId}`}
      </Typography>
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
        }}
      >
        {dogWeight === 0 ? (
          <ProcessLoading />
        ) : (
          <Typography variant={"h4"} fontWeight={700}>
            {dogWeight}
          </Typography>
        )}
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
          onClick={() => {
            clearFetchInterval();
            handleCancel();
          }}
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
          onClick={async () => {
            await saveWeight();
            handleCancel();
          }}
        >
          {languageMap.Save}
        </Button>
      </Box>
    </Box>
  );

  return screen === "init" ? init_screen : weight_screen;
};

export default Weight;
