import { Box, Button, TextField, Typography, useTheme, Select, MenuItem} from "@mui/material";
import { customColor } from "../theme.js";

const LoginForm = (props) => {
    const {onClose} = props;
  const theme = useTheme();

  const handleCancel = () => {
    onClose();
  };

  return (
    <Box
      p={4}
      sx={{
        height: "100%",
        border: "7px #8BB6D8  solid",
        borderRadius: "30px",
        backgroundColor: customColor.whiteBackGround,
      }}
      display={"flex"}
      flexDirection={"column"}
      justifyContent={"space-evenly"}
    >
        <Typography variant={"h3"} fontWeight={"800"} paddingBottom={'5px'}>
          New Dog
        </Typography>
      <Box>
        <Typography variant={"h5"} fontWeight={"800"}>
          Name:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"800"}>
          Breed:
        </Typography>
      </Box>
      <Box>
        <TextField
          fullWidth
          size={"small"}
          sx={{
            backgroundColor: theme.palette.secondary.main,
          }}
        />
      </Box>
      <Box>
        <Typography variant={"h5"} fontWeight={"800"}>
          Location:
        </Typography>
      </Box>
      <Box>
        <Select
          fullWidth="true"
          sx={{backgroundColor: theme.palette.secondary.main, height: "45px"}}
        >
          {/* TODO: replace names */}
          <MenuItem value={'Centre 1'}>Centre 1</MenuItem>
          <MenuItem value={'Centre 2'}>Centre 2</MenuItem>
          <MenuItem value={'Centre 3'}>Centre 3</MenuItem>
        </Select>
      </Box>
      <Box display={"flex"} justifyContent={"space-between"} paddingTop={'30px'}>
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
                // TODO: add onClick to create dog
            >
                CREATE
            </Button>
      </Box>
    </Box>
  );
};

export default LoginForm;
