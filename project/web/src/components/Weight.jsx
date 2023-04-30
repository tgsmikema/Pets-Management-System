import {
  Box,
  Button,
  TextField,
  Typography,
  useTheme,
  Select,
  MenuItem,
} from "@mui/material";
import { useState } from "react";
import { customColor } from "../theme.js";


const Weight = ({ onClose, id }) => {
  const [screen, setScreen] = useState("init");
  const scales_id = null
  const theme = useTheme();
  //TODO: replace hardcoded values with db
  const name = "Oliver";

  const handleCancel = () => {
    onClose();
  };

  const init_screen = (
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
          value={scales_id}
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
          onClick={() => setScreen("weight")}
        >
          WEIGH
        </Button>
      </Box>
    </Box>
  );

  const weight_screen = (
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
        Place {name} on {scales_id}
      </Typography>
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
          SAVE
        </Button>
      </Box>
    </Box>
  );

  return screen === "init" ? init_screen : weight_screen;


  // return (
  //   <Box
  //     p={4}
  //     sx={{
  //       height: "100%",
  //       border: "5px #8BB6D8  solid",
  //       borderRadius: "20px",
  //       backgroundColor: customColor.whiteBackGround,
  //     }}
  //     display={"flex"}
  //     flexDirection={"column"}
  //     justifyContent={"space-evenly"}
  //   >
  //     <Typography variant={"h3"} fontWeight={"700"} paddingBottom={"5px"}>
  //       Add Weight
  //     </Typography>
  //     <Typography variant={"h5"} fontWeight={"700"}>
  //       Place {name} on the scales
  //     </Typography>
  //     <Box>
  //       <Select
  //         fullWidth="true"
  //         value={scales_id}
  //         sx={{ backgroundColor: theme.palette.secondary.main, height: "45px" }}
  //       >
  //         <MenuItem value={"a"}>a</MenuItem>
  //         <MenuItem value={"b"}>b</MenuItem>
  //         <MenuItem value={"c"}>c</MenuItem>
  //       </Select>
  //     </Box>
      
  //     <Box
  //       display={"flex"}
  //       justifyContent={"space-between"}
  //       paddingTop={"30px"}
  //     >
  //       <Button
  //         variant={"contained"}
  //         sx={{
  //           fontWeight: "700",
  //           fontSize: "0.8rem",
  //           "&:hover": {
  //             backgroundColor: theme.palette.primary.main,
  //           },
  //         }}
  //         onClick={handleCancel}
  //       >
  //         CANCEL
  //       </Button>
  //       <Button
  //         variant={"contained"}
  //         sx={{
  //           fontWeight: "700",
  //           fontSize: "0.8rem",
  //           "&:hover": {
  //             backgroundColor: theme.palette.primary.main,
  //           },
  //         }}
  //         onClick={handleNextClick}
  //       >
  //         WEIGH
  //       </Button>
  //     </Box>
  //     {/* Add weight modal */}
  //     <Dialog
  //         open={openNext}
  //         onClose={handleNextClose}
  //         maxWidth="xs"
  //         PaperProps={{ sx: { borderRadius: "20px", height: "70%" } }}
  //       >
  //         <Weight onClose={handleNextClose} id={id} scales_id={scales_id} style={{zIndex: 9999}}/>
  //     </Dialog>
  //   </Box>
  // );
};

export default Weight;