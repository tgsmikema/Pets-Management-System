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

const EditUser = ({ onClose, selectedRow }) => {
  const name = selectedRow ? selectedRow.name : "";
  const job = selectedRow ? selectedRow.job : "";
  const accessLevel = selectedRow ? selectedRow.accessLevel : "";
  const id = selectedRow ? selectedRow.id : "";
  const theme = useTheme();

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
        New User
      </Typography>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          Name:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          defaultValue={name}
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          Job:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          size={"small"}
          defaultValue={job}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"700"}>
          Access Level:
        </Typography>
      </Box>
      <Box>
        <Select
          fullWidth="true"
          sx={{ backgroundColor: theme.palette.secondary.main, height: "45px" }}
        >
          <MenuItem value={"Volunteer"}>Volunteer</MenuItem>
          <MenuItem value={"Vet"}>Vet</MenuItem>
          <MenuItem value={"Admin"}>Admin</MenuItem>
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
            backgroundColor: theme.palette.error.main,
            "&:hover": {
              backgroundColor: theme.palette.primary.main,
            },
          }}
        //   TODO: add onClick to delete user
        >
          DELETE
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
          // TODO: add onClick to update user
        >
          SAVE
        </Button>
      </Box>
    </Box>
  );
};

export default EditUser;