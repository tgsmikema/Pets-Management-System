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
import { useEffect, useState } from "react";
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

const StyledButton = styled(Button)({
  padding: "2% 2%",
  "& .MuiButton-endIcon": {
    fontSize: "10rem",
    color: "black",
  },
});

//control row click
//TODO: route to dog page

function ViewButton(params) {
  const id = params.params;
  const navigate = useNavigate();
  const handleViewClick = () => {
    console.log("viewing " + id);
    navigate(`/dogs/${id}`);
  };
  return (
    <Button
      variant="contained"
      color="primary"
      onClick={() => handleViewClick()}
    >
      View
    </Button>
  );
}

//TODO: replace hardcoded values with db
const rows = [
  {
    id: "8093",
    flagged: "true",
    alert: "true",
    name: "Kyra",
    breed: "Border Collie",
    last: "2021-10-05",
    weight: "14.8",
  },
  {
    id: "8043",
    flagged: "false",
    alert: "false",
    name: "Malika",
    breed: "Samoyed",
    last: "2021-10-05",
    weight: "22.4",
  },
  {
    id: "4759",
    flagged: "false",
    alert: "true",
    name: "Nutella",
    breed: "Dachshund",
    last: "2021-10-05",
    weight: "10.0",
  },
  {
    id: "2406",
    flagged: "false",
    alert: "false",
    name: "Oliver",
    breed: "Basset Hound",
    last: "2021-10-05",
    weight: "14.3",
  },
  {
    id: "2346",
    flagged: "true",
    alert: "true",
    name: "Otto",
    breed: "Mixed",
    last: "2021-10-05",
    weight: "23.4",
  },
  {
    id: "2467",
    flagged: "true",
    alert: "false",
    name: "Ravioli",
    breed: "Goldendoodle",
    last: "2021-10-05",
    weight: "18.7",
  },
  {
    id: "0489",
    flagged: "false",
    alert: "false",
    name: "Ted",
    breed: "Corgi",
    last: "2021-10-05",
    weight: "8.9",
  },
  {
    id: "1394",
    flagged: "false",
    alert: "false",
    name: "Wolf",
    breed: "Husky",
    last: "2021-10-05",
    weight: "23.9",
  },
  {
    id: "8401",
    flagged: "false",
    alert: "true",
    name: "Zephyr",
    breed: "Australian Shepherd",
    last: "2021-10-05",
    weight: "21.0",
  },
];

const columns = [
  {
    field: "flagged",
    headerName: "flagged",
    flex: 1,
    renderCell: (params) =>
      params.value === "true" ? <FlagCircleIcon /> : null,
  },
  {
    field: "alert",
    headerName: "alert",
    flex: 1,
    renderCell: (params) => (params.value === "true" ? <ErrorIcon /> : null),
  },
  { field: "id", headerName: "id", flex: 1 },
  { field: "name", headerName: "name", flex: 1.5 },
  { field: "breed", headerName: "breed", flex: 1.5 },
  { field: "last", headerName: "last check-in", flex: 1.5 },
  { field: "weight", headerName: "weight(kg)", flex: 1 },
  {
    field: "view",
    headerName: " ",
    flex: 1,
    renderCell: (params) => <ViewButton params={params.row.id} />,
  },
];

const HomePage = () => {
  const { user } = useAuth();
  const { setSelected } = useUtilProvider();
  const { allCentres } = useWebService();
  const [centreValue, setCentreValue] = useState("");
  const theme = useTheme();
  useEffect(() => {
    setSelected("Home");
    setCentreValue(allCentres[0]);
  }, []);

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
            {centreValue}
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
          rows={rows}
          columns={columns}
          columnHeaderHeight={"45"}
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
        <AddDog onClose={handleAddDogClose} />
      </Dialog>
    </Box>
  );
};

export default HomePage;
