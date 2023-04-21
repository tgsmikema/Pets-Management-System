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

const Weight = ({ onClose, id }) => {
  const theme = useTheme();
  //TODO: replace hardcoded values with db
    const name = "Oliver";

    const handleCancel = () => {
    onClose();
  };

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
        Add Weight
      </Typography>
      <Typography variant={"h5"} fontWeight={"700"}>
        Place {name} on the scales
      </Typography>
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
      <Box>
        <TextField
          fullWidth
          size={"large"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Typography variant={"h6"} fontWeight={"600"} style={{textAlign: 'center'}}>
        18/03/13
    </Typography>
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
          onClick={handleCancel}
        >
          CANCEL
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
          // TODO: add onClick to update db
        >
          CREATE
        </Button>
      </Box>
    </Box>
  );
};

export default Weight;