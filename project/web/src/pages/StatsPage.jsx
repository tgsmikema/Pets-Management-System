import {
  Box,
  Button,
  Typography,
  Menu,
  MenuItem,
  Stack,
  IconButton,
} from "@mui/material";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useEffect, useState } from "react";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import KeyboardArrowLeftIcon from "@mui/icons-material/KeyboardArrowLeft";
import KeyboardArrowRightIcon from "@mui/icons-material/KeyboardArrowRight";
import PieChart from "../components/PieChart.jsx";
import LineChart from "../components/LineChart.jsx";

const StatsPage = () => {
  const { setSelected } = useUtilProvider();
  useEffect(() => {
    setSelected("Stats");
  });

  const [value, setValue] = useState("All Centers");
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  //all the fake data
  const allCenters = ["all centres", "centre1", "centre2", "centre3"];

  const data = [
    {
      id: "Weighted",
      label: "Weighted",
      value: 152,
    },
    {
      id: "Unweighted",
      label: "Unweighted",
      value: 32,
    },
  ];

  const dataForLine = [
    {
      id: "weighted",
      data: [
        {
          x: "Monday",
          y: 14,
        },
        {
          x: "Tuesday",
          y: 56,
        },
        {
          x: "Wednesday",
          y: 104,
        },
        {
          x: "Thursday",
          y: 164,
        },
        {
          x: "Friday",
          y: 154,
        },
        {
          x: "Saturday",
          y: 234,
        },
        {
          x: "Sunday",
          y: 13,
        },
      ],
    },
    {
      id: "unweighed",
      data: [
        {
          x: "Monday",
          y: 234,
        },
        {
          x: "Tuesday",
          y: 176,
        },
        {
          x: "Wednesday",
          y: 56,
        },
        {
          x: "Thursday",
          y: 104,
        },
        {
          x: "Friday",
          y: 164,
        },
        {
          x: "Saturday",
          y: 84,
        },
        {
          x: "Sunday",
          y: 145,
        },
      ],
    },
  ];

  return (
    <Box
      height={"100%"}
      width={"100%"}
      display={"flex"}
      flexDirection={"column"}
      alignItems={"center"}
    >
      <Box width={"95%"} p={1}>
        <Button
          variant={"text"}
          onClick={handleClick}
          aria-controls={open ? "basic-menu" : undefined}
          aria-haspopup="true"
          aria-expanded={open ? "true" : undefined}
          endIcon={
            <KeyboardArrowDownIcon
              sx={{
                color: "#000",
              }}
            />
          }
        >
          <Typography variant={"h4"} color={"#000"} fontWeight={"650"}>
            {value}
          </Typography>
        </Button>

        <Menu
          id="basic-menu"
          anchorEl={anchorEl}
          open={open}
          onClose={handleClose}
          MenuListProps={{
            "aria-labelledby": "basic-button",
          }}
        >
          {allCenters.map((it) => (
            <MenuItem
              value={it}
              onClick={() => {
                handleClose();
                setValue(it);
              }}
            >
              {it}
            </MenuItem>
          ))}
        </Menu>
      </Box>

      <Box
        height={"85%"}
        width={"95%"}
        display={"flex"}
        justifyContent={"space-between"}
      >
        <Box
          height={"100%"}
          width={"25%"}
          display={"flex"}
          flexDirection={"column"}
          alignItems={"center"}
        >
          <Box mb={1}>
            <Typography
              variant={"body2"}
              fontWeight={"500"}
              fontSize={"1rem"}
              textAlign={"center"}
            >
              This Week
            </Typography>
          </Box>

          <Box
            height={"90%"}
            width={"100%"}
            display={"flex"}
            flexDirection={"column"}
            sx={{
              backgroundColor: "#fff",
              borderRadius: "13px",
              boxShadow: 3,
            }}
          >
            <Box
              height={"45%"}
              display={"flex"}
              flexDirection={"column"}
              justifyContent={"space-evenly"}
            >
              <Typography variant={"body1"} textAlign={"center"}>
                Weighted
              </Typography>
              <Typography variant={"h5"} fontWeight={600} textAlign={"center"}>
                152
              </Typography>
              <Typography variant={"body1"} textAlign={"center"}>
                Unweighted
              </Typography>
              <Typography variant={"h5"} fontWeight={600} textAlign={"center"}>
                45
              </Typography>
            </Box>
            <Box height={"55%"} width={"100%"}>
              {/*pie chart*/}
              <PieChart data={data} />
            </Box>
          </Box>
        </Box>

        <Box height={"93%"} width={"70%"}>
          <Box display={"flex"} justifyContent={"space-between"} mb={1}>
            <Stack direction={"row"} spacing={1}>
              <IconButton>
                <KeyboardArrowLeftIcon />
              </IconButton>
              <Typography
                variant={"body2"}
                display={"flex"}
                alignItems={"center"}
              >
                4.17 - 4.25
              </Typography>
              <IconButton>
                <KeyboardArrowRightIcon />
              </IconButton>
            </Stack>
            <Stack spacing={2} direction={"row"}>
              <Button
                variant={"contained"}
                size={"small"}
                sx={{
                  "&:hover": {
                    color: "#fff",
                  },
                }}
              >
                month
              </Button>
              <Button
                variant={"contained"}
                size={"small"}
                sx={{
                  "&:hover": {
                    color: "#fff",
                  },
                }}
              >
                week
              </Button>
            </Stack>
          </Box>
          <Box
            height={"92%"}
            width={"100%"}
            sx={{
              backgroundColor: "#fff",
              borderRadius: "13px",
              boxShadow: 3,
            }}
          >
            <LineChart data={dataForLine} />
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default StatsPage;
