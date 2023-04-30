import { Box, Button, Typography, Dialog, useTheme } from "@mui/material";
import { useAuth } from "../providers/AuthProvider.jsx";
import { useNavigate } from "react-router-dom";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useEffect, useState } from "react";
import AddIcon from "@mui/icons-material/Add";
import AddUser from "../components/AddUser.jsx";
import EditUser from "../components/EditUser.jsx";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { useWebService } from "../providers/WebServiceProvider.jsx";

const styles = {
  boxShadow: "0px 4px 4px rgba(0, 0, 0, 0.25)",
  padding: "1.5%",
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  height: "100%",
};

const button_styles = {
  backgroundColor: "#FFFFFF",
  borderRadius: "5px",
  minWidth: "200px",
  margin: "10px",
};

//TODO: replace hardcoded values with db
const rows = [
  {
    id: "8093",
    name: "John Doe",
    job: "Electrical Engineer at UOA",
    access: "Admin",
  },
  {
    id: "8043",
    name: "Anna Verdi",
    job: "Software Engineer at UOA",
    access: "Admin",
  },
  {
    id: "4759",
    name: "Fulano de Tal",
    job: "Vet at Manukau",
    access: "Vet",
  },
  {
    id: "2406",
    name: "Zhang San",
    job: "Vet at Papakura",
    access: "Vet",
  },
  {
    id: "6742",
    name: "Hanako Yamada",
    job: "Electrical Engineer at UOA",
    access: "Volunteer",
  },
  {
    id: "1255",
    name: "Maria Ivanova",
    job: "Software Engineer at UOA",
    access: "Admin",
  },
  {
    id: "7624",
    name: "Nomen Nescio",
    job: "Software Engineer at UOA",
    access: "Volunteer",
  },
];

const ProfilePage = () => {
  const theme = useTheme();
  const { setAllCentres } = useWebService();
  const columns = [
    { field: "id", headerName: "id", flex: 1 },
    { field: "name", headerName: "name", flex: 1.5 },
    { field: "job", headerName: "job", flex: 1.5 },
    { field: "access", headerName: "access level", flex: 1.5 },
    {
      field: "view",
      headerName: " ",
      flex: 1,
      renderCell: (params) => (
        <Button
          variant="contained"
          color="primary"
          onClick={() => handleEditUserClick(params.row)}
        >
          View
        </Button>
      ),
    },
  ];

  const { setSelected } = useUtilProvider();
  const { logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    setSelected("Profile");
  });

  //control plus button
  const [openAddUser, setOpenAddUser] = useState(false);
  const handleAddUserClick = () => {
    setOpenAddUser(true);
  };
  const handleAddUserClose = () => {
    setOpenAddUser(false);
  };

  //control edit button
  const [openEditUser, setOpenEditUser] = useState(false);
  const [selectedRow, setSelectedRow] = useState(null);
  const handleEditUserClick = (row) => {
    setSelectedRow(row);
    setOpenEditUser(true);
  };
  const handleEditUserClose = () => {
    setSelectedRow(null);
    setOpenEditUser(false);
  };

  return (
    <Box
      sx={{
        backgroundColor: theme.palette.secondary.main,
      }}
    >
      <Box sx={styles}>
        <div style={{ flex: "1 1 0%" }}>
          <Typography variant={"h4"} fontWeight={"650"} color={"#000"}>
            Maria Ivanova
          </Typography>
          <Typography variant={"h6"} fontWeight={"500"} color={"#000"}>
            Software Engineer at UOA
          </Typography>
          <Typography variant={"h6"} fontWeight={"500"} color={"#000"}>
            Admin
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
          <Button
            sx={{ ...button_styles, marginLeft: "auto" }}
            variant={"contained"}
            onClick={() => {
              // TODO: Add code to change password
            }}
          >
            Change Password
          </Button>
          <Button
            sx={button_styles}
            variant={"contained"}
            onClick={() => {
              logout();
              setAllCentres([]);
              navigate("/");
            }}
          >
            Log out
          </Button>
        </div>
      </Box>

      <Box
        width={"100%"}
        padding={3}
        boxSizing={"border-box"}
        sx={{
          overflowY: "auto",
        }}
      >
        {/* TODO: ADD CODE HERE TO MAKE THIS ONLY SHOW TO ADMINS */}
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
          }}
        >
          <Typography variant={"h4"} fontWeight={"650"} color={"#000"}>
            Manage User Access
          </Typography>
          {/* Add user button */}
          <Box display="flex" justifyContent="flex-end" alignItems="center">
            <Button
              onClick={handleAddUserClick}
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
        </div>
        {/* Table */}
        <Box
          height={"500px"}
          width={"100%"}
          display={"flex"}
          justifyContent={"center"}
          mt={2}
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
        {/* Add user modal */}
        <Dialog
          open={openAddUser}
          onClose={handleAddUserClose}
          maxWidth="xs"
          PaperProps={{ sx: { borderRadius: "20px", height: "70%" } }}
        >
          <AddUser onClose={handleAddUserClose} />
        </Dialog>
        {/* Edit user modal */}
        <Dialog
          open={openEditUser}
          onClose={handleEditUserClose}
          maxWidth="xs"
          PaperProps={{ sx: { borderRadius: "20px", height: "70%" } }}
        >
          <EditUser onClose={handleEditUserClose} selectedRow={selectedRow} />
        </Dialog>
      </Box>
    </Box>
  );
};

export default ProfilePage;
