import { Box, 
    Menu,
    MenuItem,
    Button,
    Typography,
    Dialog } from "@mui/material";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useEffect, useState } from "react";
import React from 'react';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { styled } from '@mui/material/styles';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';
import FlagCircleIcon from '@mui/icons-material/FlagCircle';
import ErrorIcon from '@mui/icons-material/Error';
import AddIcon from '@mui/icons-material/Add';
import AddDog from "../components/AddDog.jsx";


const StyledButton = styled(Button)({
  padding: '3% 3%',
  '& .MuiButton-endIcon': { 
    fontSize: '10rem', 
    color: 'black', 
  },
});

  //control row click
  //TODO: route to dog page
const handleViewClick = (params) => {
    const dogId = params;
    console.log("viewing " + dogId)
};

//TODO: replace hardcoded values with db
const rows = [
  { id: '8093', flagged: 'true', alert: 'true', name: 'Kyra', breed: 'Border Collie', last: '2021-10-05', weight: '14.8' },
  { id: '8043', flagged: 'false', alert: 'false', name: 'Malika', breed: 'Samoyed', last: '2021-10-05', weight: '22.4' },
  { id: '4759', flagged: 'false', alert: 'true', name: 'Nutella', breed: 'Dachshund', last: '2021-10-05', weight: '10.0' },
  { id: '2406', flagged: 'false', alert: 'false', name: 'Oliver', breed: 'Basset Hound', last: '2021-10-05', weight: '14.3' },
  { id: '2346', flagged: 'true', alert: 'true', name: 'Otto', breed: 'Mixed', last: '2021-10-05', weight: '23.4' },
  { id: '2467', flagged: 'true', alert: 'false', name: 'Ravioli', breed: 'Goldendoodle', last: '2021-10-05', weight: '18.7' },
  { id: '0489', flagged: 'false', alert: 'false', name: 'Ted', breed: 'Corgi', last: '2021-10-05', weight: '8.9' },
  { id: '1394', flagged: 'false', alert: 'false', name: 'Wolf', breed: 'Husky', last: '2021-10-05', weight: '23.9' },
  { id: '8401', flagged: 'false', alert: 'true', name: 'Zephyr', breed: 'Australian Shepherd', last: '2021-10-05', weight: '21.0' },
];

const columns = [
  { field: 'flagged', headerName: 'flagged', width: 100, renderCell: (params) => (
    params.value === 'true' ? <FlagCircleIcon /> : null) },
  { field: 'alert', headerName: 'alert', width: 100, renderCell: (params) => (
    params.value === 'true' ? <ErrorIcon /> : null) },
  { field: 'id', headerName: 'id', width: 150 },
  { field: 'name', headerName: 'name', width: 200 },
  { field: 'breed', headerName: 'breed', width: 200 },
  { field: 'last', headerName: 'last check-in', width: 200 },
  { field: 'weight', headerName: 'weight(kg)', width: 150 },
  {
    field: 'view',
    headerName: ' ',
    width: 150,
    renderCell: (params) => (
      <Button variant="contained" color="primary" onClick={() => handleViewClick(params.row.id)}>
        View
      </Button>
    ),
  },
];


const HomePage = () => {
  const { setSelected } = useUtilProvider();
  useEffect(() => {
    setSelected("Home");
  });
  
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
    <div>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%'}}>

        {/* Centres drop down */}
        <StyledButton
          id="basic-button"
          aria-controls={open ? 'basic-menu' : undefined}
          aria-haspopup="true"
          aria-expanded={open ? 'true' : undefined}
          onClick={handleClick}
          endIcon={<ExpandMoreIcon />}
        >
          {/* TODO: replace hardcoded string with db */}
          <Typography variant={"h2"} fontWeight={"700"}>
            All Centres
          </Typography>
          
        </StyledButton>
        <Menu
          id="basic-menu"
          anchorEl={anchorEl}
          open={open}
          onClose={handleClose}
          MenuListProps={{
            'aria-labelledby': 'basic-button',
          }}
        >
          {/* TODO: replace hardcoded string with db */}
          <MenuItem onClick={handleClose}>Centre 1</MenuItem>
          <MenuItem onClick={handleClose}>Centre 2</MenuItem>
          <MenuItem onClick={handleClose}>Centre 3</MenuItem>
        </Menu>


        {/* Add dog button */}
        <Box display="flex" justifyContent="flex-end" alignItems="center">
          <Button
            onClick={handleAddDogClick}
            variant="contained"
            color="primary"
            sx={{ borderRadius: '50%', minWidth: 0, width: 48, height: 48 }}
          >
            <AddIcon sx={{ fontSize: 28, color: 'black' }} />
          </Button>
        </Box>
      </Box>


        {/* Table */}
        <div style={{ height: '70%', width: '100%', paddingLeft: '4%' }}>
          <DataGrid rows={rows} columns={columns} autoPageSize={true} columnHeaderHeight={'45'} slots={{ toolbar: GridToolbar }}
          slotProps={{
            toolbar: {
              showQuickFilter: true,
              quickFilterProps: { debounceMs: 500 },
            },
          }}
          />
        </div>


      {/* Add dog modal */}
      <Dialog open={openAddDog} onClose={handleAddDogClose} maxWidth="xs" fullWidth='true' PaperProps={{ sx: { borderRadius: '30px', height: '50%' } }}>
        <AddDog onClose={handleAddDogClose} />
      </Dialog>
    </div>
  );
};

export default HomePage;
