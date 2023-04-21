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

const EditDog = ({ onClose, id }) => {
  const theme = useTheme();
  //TODO: replace hardcoded values with db
    const name = "name";
    const breed = "breed";

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
        Edit Dog
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
          Breed:
        </Typography>
      </Box>
      <Box>
        <TextField
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
          Location:
        </Typography>
      </Box>
      <Box>
        <Select
          fullWidth="true"
          sx={{ backgroundColor: theme.palette.secondary.main, height: "45px" }}
        >
          <MenuItem value={"a"}>a</MenuItem>
          <MenuItem value={"b"}>b</MenuItem>
          <MenuItem value={"c"}>c</MenuItem>
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
        //   TODO: add onClick to delete dog
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
          // TODO: add onClick to update dog
        >
          SAVE
        </Button>
      </Box>
    </Box>
  );
};

export default EditDog;
