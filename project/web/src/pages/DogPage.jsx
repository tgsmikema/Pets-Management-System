import { Box, Button, Dialog, useTheme } from "@mui/material";
import { useParams } from "react-router-dom";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useCallback, useEffect, useState } from "react";
import Typography from "@mui/material/Typography";
import EditIcon from "@mui/icons-material/Edit";
import FlagCircleIcon from "@mui/icons-material/FlagCircle";
import ErrorIcon from "@mui/icons-material/Error";
import AddIcon from "@mui/icons-material/Add";
import LineChart from "../components/LineChart.jsx";
import { DataGrid } from "@mui/x-data-grid";
import EditDog from "../components/EditDog.jsx";
import Weight from "../components/Weight.jsx";
import { useAuth } from "../providers/AuthProvider.jsx";
import axios from "axios";
import { constants } from "../constants.js";
import { useWebService } from "../providers/WebServiceProvider.jsx";
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";

const styles = {
  boxShadow: "0px 4px 4px rgba(0, 0, 0, 0.25)",
  padding: "2% 2% 1% 2%",
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  height: "100%",
};

const timestampToDate = (timestamp) => {
  const date = new Date(timestamp);
  return (
    date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear()
  );
};

const timestampToLineDate = (timestamp) => {
  const date = new Date(timestamp);
  return (
    date.getDate() +
    "/" +
    (date.getMonth() + 1) +
    "/" +
    date.getFullYear().toString().substring(2)
  );
};

function DogPage() {
  const theme = useTheme();
  const { id } = useParams();
  const { setSelected } = useUtilProvider();
  const { user } = useAuth();
  const { allCentres } = useWebService();
  const { languageMap } = useLanguageProvider();

  const columns = [
    {
      field: "timeStamp",
      headerName: languageMap.LastCheckIn,
      flex: 1.5,
      renderCell: (params) => (
        <div>{timestampToDate(parseInt(params.row.timeStamp) * 1000)}</div>
      ),
    },
    { field: "dogWeight", headerName: languageMap.Weight + "(kg)", flex: 1 },
  ];

  // control flag button
  //TODO: set/update based on db
  const [isFlag, setIsFlag] = useState(false);
  const handleFlagClick = useCallback(async () => {
    if (user.userType === "volunteer") return;
    setIsFlag(!isFlag);
    const res = await axios.get(
      `${constants.backend}/dog/toggleFlag?dogId=${id}`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
  }, [isFlag]);

  // control alert button
  //TODO: set/update based on db
  const [isAlert, setIsAlert] = useState(false);
  const handleAlertClick = useCallback(async () => {
    if (user.userType === "volunteer") return;
    setIsAlert(!isAlert);
    //update the state in db
    await axios.get(`${constants.backend}/dog/toggleAlert?dogId=${id}`, {
      headers: {
        Authorization: "Basic " + user.token,
      },
    });
  }, [isAlert]);

  //control edit button
  const [openEditDog, setOpenEditDog] = useState(false);
  const handleEditDogClick = () => {
    setOpenEditDog(true);
  };
  const handleEditDogClose = () => {
    setOpenEditDog(false);
  };

  //control add button
  const [openWeight, setOpenWeight] = useState(false);
  const handleWeightClick = () => {
    setOpenWeight(true);
  };
  const handleWeightClose = () => {
    setOpenWeight(false);
  };

  //fetch the dog detail data from the backend
  const [dog, setDog] = useState(null);
  const fetchDogInfo = useCallback(async () => {
    const res = await axios.get(
      `${constants.backend}/dog/${
        user.userType === "admin"
          ? "detailFromAllCentre"
          : "detailFromOwnCentre"
      }?dogId=${id}`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
    setDog(res.data);
    setIsFlag(res.data.isFlag);
    setIsAlert(res.data.isAlert);
  }, [dog]);

  const [weightHistory, setWeightHistory] = useState([]);

  const [weightDataForLineChart, setWeightDataForLineChart] = useState([]);
  const fetchDogWeightHistory = useCallback(async () => {
    const res = await axios.get(
      `${constants.backend}/dog/getWeightHistory?dogId=${id}`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
    setWeightHistory(res.data);
    setWeightDataForLineChart(
      res.data.reverse().map((it, index) => ({
        x: timestampToLineDate(parseInt(it.timeStamp) * 1000),
        y: it.dogWeight,
      }))
    );
  }, []);

  const dataForLine = [
    {
      id: "weight",
      data: weightDataForLineChart,
    },
  ];

  const [scaleList, setScaleList] = useState([]);
  const fetchScaleList = useCallback(async () => {
    const res = await axios.get(`${constants.backend}/util/listAllScales`, {
      headers: {
        Authorization: "Basic " + user.token,
      },
    });
    setScaleList(res.data);
  }, []);

  useEffect(() => {
    setSelected(null);
    fetchDogInfo();
    fetchDogWeightHistory();
    fetchScaleList();
  }, [user, openWeight, openEditDog]);

  return (
    <Box
      sx={{
        backgroundColor: theme.palette.secondary.main,
      }}
    >
      {/* Top banner */}
      <Box sx={styles}>
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            marginRight: "1%",
          }}
        >
          {/* Add button */}
          <Box display="flex" justifyContent="flex-end" alignItems="center">
            <Button
              onClick={handleWeightClick}
              variant="contained"
              color="primary"
              sx={{
                borderRadius: "50%",
                minWidth: 0,
                width: 48,
                height: 48,
              }}
            >
              <AddIcon sx={{ fontSize: 45, color: "#000" }} />
            </Button>
          </Box>
          <Button onClick={handleEditDogClick} style={{ marginTop: "10%" }}>
            <EditIcon sx={{ fontSize: 45, color: "#000" }} />
          </Button>
        </div>
        <div style={{ flex: "1 1 0%" }}>
          <Typography variant={"h5"} fontWeight={"700"} color={"#000"}>
            {id}
          </Typography>
          <Typography variant={"h3"} fontWeight={"600"} color={"#000"}>
            {dog?.name}
          </Typography>
          <Typography variant={"h5"} fontWeight={"500"} color={"#000"}>
            {dog?.breed}
          </Typography>
          <Typography variant={"h5"} fontWeight={"500"} color={"#000"}>
            {user.userType === "admin"
              ? allCentres[dog?.centreId]
              : allCentres[0]}
          </Typography>
        </div>
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "flex-end",
          }}
        >
          <div style={{ display: "flex", alignItems: "baseline" }}>
            <Typography variant={"h3"} fontWeight={"500"} color={"#000"}>
              {dog?.lastCheckInWeight}
            </Typography>
            <Typography variant={"h5"} fontWeight={"300"} color={"#000"}>
              kg
            </Typography>
          </div>
          <Typography variant={"h6"} fontWeight={"400"} color={"#000"}>
            {timestampToDate(parseInt(dog?.lastCheckInTimeStamp) * 1000)}
          </Typography>
        </div>
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "flex-end",
          }}
        >
          <Button onClick={handleFlagClick}>
            <FlagCircleIcon
              sx={{ fontSize: 60, color: isFlag ? "#E34B28" : "#000000" }}
            />
          </Button>
          <Button onClick={handleAlertClick}>
            <ErrorIcon
              sx={{ fontSize: 60, color: isAlert ? "#E34B28" : "#000000" }}
            />
          </Button>
        </div>
      </Box>

      <div style={{ display: "flex", padding: "4%" }}>
        <Box
          height={"500px"}
          width={"75%"}
          display={"flex"}
          justifyContent={"center"}
          alignItems={"center"}
          sx={{
            backgroundColor: "#fff",
            borderRadius: "13px",
            boxShadow: 3,
          }}
        >
          {weightDataForLineChart.length === 0 ? (
            <Typography variant={"h4"} fontWeight={600}>
              No data display
            </Typography>
          ) : (
            <LineChart
              data={dataForLine}
              rowLabel={languageMap.Date}
              columnLabel={languageMap.Weight + "(kg)"}
            />
          )}
        </Box>
        <Box width={"4%"}></Box>
        <Box
          height={"500px"}
          width={"24%"}
          sx={{
            backgroundColor: "#fff",
            borderRadius: "13px",
            boxShadow: 3,
          }}
        >
          <DataGrid
            rows={weightHistory}
            columns={columns}
            style={{ fontSize: "16px" }}
          />
        </Box>
      </div>
      {/* Edit dog modal */}
      <Dialog
        open={openEditDog}
        onClose={handleEditDogClose}
        maxWidth="xs"
        PaperProps={{ sx: { borderRadius: "20px", height: "70%" } }}
      >
        <EditDog onClose={handleEditDogClose} dog={dog} />
      </Dialog>
      {/* Add weight modal */}
      <Dialog
        open={openWeight}
        maxWidth="xs"
        PaperProps={{ sx: { borderRadius: "20px", height: "70%" } }}
      >
        <Weight onClose={handleWeightClose} scaleList={scaleList} dog={dog} />
      </Dialog>
    </Box>
  );
}

export default DogPage;
