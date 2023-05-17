import { Box, Button, Typography, Dialog, useTheme } from "@mui/material";
import { useAuth } from "../providers/AuthProvider.jsx";
import { useNavigate } from "react-router-dom";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useCallback, useEffect, useState } from "react";
import AddIcon from "@mui/icons-material/Add";
import AddUser from "../components/AddUser.jsx";
import EditUser from "../components/EditUser.jsx";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { useWebService } from "../providers/WebServiceProvider.jsx";
import axios from "axios";
import { constants } from "../constants.js";
import ChangePassword from "../components/ChangePassword.jsx";
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";

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

const ProfilePage = () => {
  const theme = useTheme();
  const { allCentres } = useWebService();
  const { languageMap } = useLanguageProvider();
  const { user } = useAuth();
  const column = [
    { field: "id", headerName: "id", flex: 1 },
    {
      field: "username",
      headerName: languageMap.Name,
      flex: 1.5,
      renderCell: (params) => (
        <div>
          {rows.find((it) => it.id === params.row.id).firstName +
            " " +
            rows.find((it) => it.id === params.row.id).lastName}
        </div>
      ),
    },
    { field: "email", headerName: languageMap.Email, flex: 1.5 },
    { field: "userType", headerName: languageMap.AccessLevel, flex: 1.5 },
    {
      field: "view",
      headerName: " ",
      flex: 1,
      renderCell: (params) => (
        <Button
          variant="contained"
          color="primary"
          onClick={() =>
            handleEditUserClick(rows.find((it) => it.id === params.row.id))
          }
        >
          {languageMap.View}
        </Button>
      ),
    },
  ];

  const { setSelected } = useUtilProvider();
  const { logout } = useAuth();
  const navigate = useNavigate();

  //fetch the data from the backend
  const [rows, setRows] = useState([]);

  const fetchAllUsers = useCallback(async () => {
    const res = await axios.get(`${constants.backend}/user/getAllUsers`, {
      headers: {
        Authorization: "Basic " + user.token,
      },
    });
    setRows(res.data);
  }, [user]);

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

  //control change password dialog
  const [openChangePassword, setOpenChangePassword] = useState(false);
  const handleChangePasswordOpen = useCallback(() => {
    setOpenChangePassword(true);
  }, [openChangePassword]);

  const handleChangePasswordClose = useCallback(() => {
    setOpenChangePassword(false);
  }, [openChangePassword]);

  useEffect(() => {
    setSelected(languageMap.Profile);
    fetchAllUsers();
  }, [user, openAddUser, openEditUser]);

  return (
    <Box
      sx={{
        backgroundColor: theme.palette.secondary.main,
      }}
    >
      <Box sx={styles}>
        <div style={{ flex: "1 1 0%" }}>
          <Typography variant={"h3"} fontWeight={"650"} color={"#000"}>
            {`${user.firstName} ${user.lastName}`}
          </Typography>
          <Typography variant={"h4"} fontWeight={"500"} color={"#000"}>
            {user.userType}
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
              handleChangePasswordOpen();
            }}
          >
            {languageMap.ChangePassword}
          </Button>
          <Button
            sx={button_styles}
            variant={"contained"}
            onClick={() => {
              logout();
              navigate("/");
            }}
          >
            {languageMap.Logout}
          </Button>
        </div>
      </Box>

      <Box
        width={"100%"}
        minHeight={"70vh"}
        padding={3}
        display={"flex"}
        flexDirection={"column"}
        justifyContent={"center"}
        boxSizing={"border-box"}
        sx={{
          overflowY: "auto",
        }}
      >
        {/* TODO: ADD CODE HERE TO MAKE THIS ONLY SHOW TO ADMINS */}
        {user.userType === "admin" ? (
          <>
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
              alignItems={"center"}
              mt={2}
              sx={{
                backgroundColor: "#fff",
                borderRadius: "13px",
                boxShadow: 3,
              }}
            >
              <DataGrid
                rows={rows}
                columns={column}
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
          </>
        ) : (
          <Typography variant={"h5"} color={"red"} alignSelf={"center"}>
            You can not manage or view other users, ask for permission from the
            admin
          </Typography>
        )}

        {/* Add user modal */}
        <Dialog
          open={openAddUser}
          onClose={handleAddUserClose}
          maxWidth="xs"
          PaperProps={{ sx: { borderRadius: "20px", height: "70%" } }}
        >
          <AddUser
            onClose={handleAddUserClose}
            allCentres={allCentres.slice(1)}
          />
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
        <Dialog
          open={openChangePassword}
          onClose={handleChangePasswordClose}
          maxWidth="xs"
          PaperProps={{ sx: { borderRadius: "20px", height: "70%" } }}
        >
          <ChangePassword handleClose={handleChangePasswordClose} />
        </Dialog>
      </Box>
    </Box>
  );
};

export default ProfilePage;
