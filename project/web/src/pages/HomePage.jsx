import {
  Box,
  Menu,
  MenuItem,
  Button,
  Typography,
  Dialog,
  useTheme,
} from "@mui/material";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useCallback, useEffect, useState } from "react";
import React from "react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { styled } from "@mui/material/styles";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import FlagCircleIcon from "@mui/icons-material/FlagCircle";
import ErrorIcon from "@mui/icons-material/Error";
import AddIcon from "@mui/icons-material/Add";
import AddDog from "../components/AddDog.jsx";
import { useNavigate } from "react-router-dom";
import { useWebService } from "../providers/WebServiceProvider.jsx";
import { useAuth } from "../providers/AuthProvider.jsx";
import ProcessLoading from "../components/ProcessLoading.jsx";
import axios from "axios";
import { constants } from "../constants.js";
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";

const StyledButton = styled(Button)({
  padding: "2% 2%",
  "& .MuiButton-endIcon": {
    fontSize: "10rem",
    color: "black",
  },
});

const HomePage = () => {
  const { user } = useAuth();
  const { setSelected } = useUtilProvider();
  const { allCentres, centreLoading, allCentreForAllUser } = useWebService();
  //display the centre name
  const [centreValue, setCentreValue] = useState("");

  const { languageMap } = useLanguageProvider();

  function ViewButton(params) {
    const id = params.params;
    const navigate = useNavigate();
    const handleViewClick = () => {
      navigate(`/dogs/${id}`);
    };
    return (
      <Button
        variant="contained"
        color="primary"
        onClick={() => handleViewClick()}
      >
        {languageMap.View}
      </Button>
    );
  }

  const columns = [
    {
      field: "isFlag",
      headerName: languageMap.Flagged,
      flex: 1,
      renderCell: (params) => (params.row.isFlag ? <FlagCircleIcon /> : null),
    },
    {
      field: "isAlert",
      headerName: languageMap.Alert,
      flex: 1,
      renderCell: (params) => (params.row.isAlert ? <ErrorIcon /> : null),
    },
    { field: "id", headerName: "id", flex: 1 },
    { field: "name", headerName: languageMap.Name, flex: 1.5 },
    { field: "breed", headerName: languageMap.Breed, flex: 1.5 },
    {
      field: "lastCheckInTimeStamp",
      headerName: languageMap.LastCheckIn,
      flex: 1.5,
    },
    {
      field: "lastCheckInWeight",
      headerName: languageMap.Weight + "(kg)",
      flex: 1,
    },
    {
      field: "view",
      headerName: "",
      flex: 1,
      renderCell: (params) => <ViewButton params={params.row.id} />,
    },
  ];

  //this centreIdx is used for the admin user, as it will list all the centres,
  //the centreIdx will match the centre id, such as when centreIdx = 0 , it represents the all centre
  //you can use this centreIdx to send a get request to fetch the all centres data
  const [centreIdx, setCentreIdx] = useState(0);

  const theme = useTheme();

  //control centres drop down
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  //control plus button
  const [openAddDog, setOpenAddDog] = useState(false);
  const handleAddDogClick = () => {
    setOpenAddDog(true);
  };
  const handleAddDogClose = () => {
    setOpenAddDog(false);
  };

  const getDateString = useCallback((timestamp) => {
    const date = new Date(timestamp * 1000);
    return (
      date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear()
    );
  }, []);

  // fetch data from the backend
  const [dogList, setDogList] = useState([]);
  const fetchDogData = useCallback(async () => {
    const res = await axios.get(
      `${constants.backend}/dog/${
        user.userType === "admin"
          ? centreIdx === 0
            ? "adminListAllCentres"
            : "adminListOneCentre?centreId=" + centreIdx
          : "userListOwnCentre"
      }`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );

    const dogArr = res.data.map((it, index) => ({
      ...it,
      lastCheckInTimeStamp: getDateString(it.lastCheckInTimeStamp),
    }));
    setDogList(dogArr);
  }, [user, centreIdx]);

  useEffect(() => {
    setSelected(languageMap.Home);
    setCentreValue(allCentres[centreIdx]);
    fetchDogData();
  }, [user, centreIdx, centreLoading, openAddDog]);

  return (
    <Box
      height={"100%"}
      width={"100%"}
      display={"flex"}
      flexDirection={"column"}
      alignItems={"center"}
      sx={{
        backgroundColor: theme.palette.secondary.main,
      }}
    >
      <Box
        sx={{ display: "flex", justifyContent: "space-between", width: "95%" }}
      >
        {/* Centres drop down */}
        <StyledButton
          id="basic-button"
          aria-controls={open ? "basic-menu" : undefined}
          aria-haspopup="true"
          aria-expanded={open ? "true" : undefined}
          onClick={handleClick}
          endIcon={<ExpandMoreIcon />}
        >
          {/* TODO: replace hardcoded string with db */}
          <Typography variant={"h4"} fontWeight={"600"} color={"#000"}>
            {centreLoading ? <ProcessLoading /> : centreValue}
          </Typography>
        </StyledButton>
        <Menu
          id="basic-menu"
          anchorEl={anchorEl}
          open={open}
          onClose={handleClose}
          MenuListProps={{
            "aria-labelledby": "basic-button",
          }}
        >
          {/* already replace hardcoded string with db */}
          {allCentres.map((it, index) => (
            <MenuItem
              key={index}
              onClick={() => {
                handleClose();
                setCentreValue(it);
                setCentreIdx(index);
              }}
            >
              {it}
            </MenuItem>
          ))}
        </Menu>

        {/* Add dog button */}
        <Box display="flex" justifyContent="flex-end" alignItems="center">
          <Button
            onClick={handleAddDogClick}
            variant="contained"
            color="primary"
            sx={{
              borderRadius: "50%",
              minWidth: 0,
              width: 48,
              height: 48,
            }}
          >
            <AddIcon sx={{ fontSize: 28, color: "#fff" }} />
          </Button>
        </Box>
      </Box>

      {/* Table */}
      <Box
        height={"80%"}
        width={"95%"}
        sx={{
          backgroundColor: "#fff",
          borderRadius: "13px",
          boxShadow: 3,
        }}
      >
        <DataGrid
          rows={dogList}
          columns={columns}
          columnHeaderHeight={45}
          slots={{ toolbar: GridToolbar }}
          slotProps={{
            toolbar: {
              showQuickFilter: true,
              quickFilterProps: { debounceMs: 500 },
            },
          }}
        />
      </Box>

      {/* Add dog modal */}
      <Dialog
        open={openAddDog}
        onClose={handleAddDogClose}
        maxWidth="xs"
        fullWidth={true}
        PaperProps={{ sx: { borderRadius: "20px", height: "70%" } }}
      >
        <AddDog
          onClose={handleAddDogClose}
          allCentres={allCentreForAllUser?.map((it) => it.name)}
        />
      </Dialog>
    </Box>
  );
};

export default HomePage;
