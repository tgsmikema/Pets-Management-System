import {
  Box,
  Button,
  Typography,
  Menu,
  MenuItem,
  Stack,
  IconButton,
  useTheme,
} from "@mui/material";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useCallback, useEffect, useState } from "react";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import KeyboardArrowLeftIcon from "@mui/icons-material/KeyboardArrowLeft";
import KeyboardArrowRightIcon from "@mui/icons-material/KeyboardArrowRight";
import PieChart from "../components/PieChart.jsx";
import LineChart from "../components/LineChart.jsx";
import { useWebService } from "../providers/WebServiceProvider.jsx";
import { useAuth } from "../providers/AuthProvider.jsx";
import axios from "axios";
import { constants } from "../constants.js";
import ProcessLoading from "../components/ProcessLoading.jsx";
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";

const StatsPage = () => {
  const theme = useTheme();
  //will use for highlight the router
  const { setSelected } = useUtilProvider();
  const { user } = useAuth();
  //fetch all centres for the user, this action done when the app start
  const { allCentres, centreLoading } = useWebService();
  //this value is used to display the centre name
  const [centreValue, setCentreValue] = useState("");

  const { languageMap } = useLanguageProvider();

  //below is used to show and close the centre menu
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };

  //setLoading for line chart
  const [loading, setLoading] = useState(false);

  //whether show week data, if it is false, we will show month data
  const [isWeek, setWeek] = useState(true);

  //process the timestamp for the week and month
  //get current timeStamp in second
  const now = new Date();
  const timestampInSeconds = Math.floor(now.getTime());

  //process the week date for weekly data used for line chart

  const [weekTimeStamp, setWeekTimeStamp] = useState(timestampInSeconds);

  const getPreviousWeekTimeStamp = (currentTimeStamp) => {
    return new Date(currentTimeStamp - 7 * 24 * 60 * 60 * 1000).getTime();
  };

  const getNextWeekTimeStamp = (currentTimeStamp) => {
    return new Date(currentTimeStamp + 7 * 24 * 60 * 60 * 1000).getTime();
  };

  //given a timestamp, we will return the day in this form 4.17
  const getStringOfDayFromTimeStamp = (timeStamp) => {
    let date = new Date(timeStamp);
    return `${date.getMonth() + 1}.${date.getDate()}`;
  };

  const getDay = (timeStamp) => {
    let date = new Date(timeStamp);
    return date.getDay();
  };

  //process the month date for monthly data used for the line chart

  const getFirstDayOfMonth = (currentTimeStamp) => {
    const curMonth = new Date(currentTimeStamp);
    curMonth.setDate(1);
    curMonth.setHours(0, 0, 0, 0);
    return curMonth.getTime();
  };

  const getLastDayOfMonth = (currentTimeStamp) => {
    const curMonth = new Date(currentTimeStamp);
    curMonth.setMonth(curMonth.getMonth() + 1);
    curMonth.setDate(0);
    curMonth.setHours(23, 59, 59, 999);
    return curMonth.getTime();
  };

  const getMidDayOfMonth = (timeStamp) => {
    const curMonth = new Date(timeStamp);
    curMonth.setDate(15);
    curMonth.setHours(0, 0, 0, 0);
    return curMonth.getTime();
  };

  const getPreviousMonthTimeStamp = (currentTimeStamp) => {
    return getMidDayOfMonth(
      new Date(currentTimeStamp - 30 * 24 * 60 * 60 * 1000).getTime()
    );
  };

  const getNextMonthTimeStamp = (currentTimeStamp) => {
    return getMidDayOfMonth(
      new Date(currentTimeStamp + 30 * 24 * 60 * 60 * 1000).getTime()
    );
  };

  const [monthTimeStamp, setMonthTimeStamp] = useState(
    getPreviousMonthTimeStamp(getMidDayOfMonth(timestampInSeconds))
  );

  const getMonthFromTimeStamp = (timeStamp) => {
    let date = new Date(timeStamp);
    return (
      date.toLocaleString("default", { month: "long" }) +
      " - " +
      date.getFullYear()
    );
  };

  // week button click
  const onClickForWeekButton = async () => {
    setWeek(true);
    await fetchWeekData();
  };

  //month button click
  const onClickForMonthButton = async () => {
    setWeek(false);
    await fetchMonthData();
  };

  //fetch the data from the backend;

  //1. fetch this week weighted and unweighted data
  const [weekTotalData, setWeekTotalData] = useState([]);
  const data = [
    {
      id: "Weighed",
      label: languageMap.Weighed,
      value: weekTotalData.noOfDogsWeighted,
    },
    {
      id: "UnWeighed",
      label: languageMap.Unweighed,
      value: weekTotalData.noOfDogsUnweighted,
    },
  ];
  //this centreIdx is used for the admin user, as it will list all the centres,
  //the centreIdx will match the centre id, such as when centreIdx = 0 , it represents the all centre
  //you can use this centreIdx to send a get request to fetch the all centres data
  const [centreIdx, setCentreIdx] = useState(0);

  //when the allCentres length is equal to 1, that means it does not admin userType, we only
  //fetch the data of his centre, then we can get the centre id from the user.centreId attribute
  const fetchAllCentreData = useCallback(async () => {
    const res = await axios.get(
      `${constants.backend}/util/thisWeekStats?centerId=${
        user.userType !== "admin" ? user.centreId : centreIdx
      }`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
    const data = res.data;
    setWeekTotalData(data);
  }, [centreValue]);

  //2. fetch one week's data according to the timestamp for line chart
  const [weekWeightData, setWeekWeightData] = useState([]);
  const [weekUnWeightData, setWeekUnWeightData] = useState([]);

  const weekArray = [
    languageMap.Monday,
    languageMap.Tuesday,
    languageMap.Wednesday,
    languageMap.Thursday,
    languageMap.Friday,
    languageMap.Saturday,
    languageMap.Sunday,
  ];

  const fetchWeekData = useCallback(async () => {
    // setLoading(true);
    const res = await axios.post(
      `${constants.backend}/util/weeklyStats`,
      {
        centerId: user.userType !== "admin" ? user.centreId : centreIdx,
        minTimestamp: Math.floor(
          getPreviousWeekTimeStamp(weekTimeStamp) / 1000
        ).toString(),
        maxTimestamp: Math.floor(weekTimeStamp / 1000).toString(),
      },
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
    setWeekWeightData(
      res.data.map((it, index) => ({
        x: weekArray[(index + getDay(weekTimeStamp)) % 7],
        y: it.noOfDogsWeighted,
      }))
    );
    setWeekUnWeightData(
      res.data.map((it, index) => ({
        x: weekArray[(index + getDay(weekTimeStamp)) % 7],
        y: it.noOfDogsUnweighted,
      }))
    );
    setLoading(false);
  }, [weekTimeStamp, centreIdx]);

  const weekDataForLine = [
    {
      id: languageMap.Weighed,
      data: weekWeightData,
    },
    // {
    //   id: languageMap.Unweighed,
    //   data: weekUnWeightData,
    // },
  ];

  //3. fetch one month's data for line chart
  const [monthWeightData, setMonthWeightData] = useState([]);
  const [monthUnWeightData, setMonthUnWeightData] = useState([]);

  const fetchMonthData = useCallback(async () => {
    // setLoading(true);
    const res = await axios.post(
      `${constants.backend}/util/monthlyStatus`,
      {
        centerId: user.userType !== "admin" ? user.centreId : centreIdx,
        minTimestamp: Math.floor(
          getFirstDayOfMonth(monthTimeStamp) / 1000
        ).toString(),
        maxTimestamp: Math.floor(
          getLastDayOfMonth(monthTimeStamp) / 1000
        ).toString(),
      },
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );

    setMonthWeightData(
      res.data.map((it) => ({
        x: getStringOfDayFromTimeStamp(it.timeStamp * 1000),
        y: it.noOfDogsWeighted,
      }))
    );

    setMonthUnWeightData(
      res.data.map((it) => ({
        x: getStringOfDayFromTimeStamp(it.timeStamp * 1000),
        y: it.noOfDogsUnweighted,
      }))
    );
    setLoading(false);
  }, [monthTimeStamp, centreIdx]);

  const monthDataForLine = [
    {
      id: "weighed",
      data: monthWeightData,
    },
    // {
    //   id: "unWeighed",
    //   data: monthUnWeightData,
    // },
  ];

  //back button click
  const onClickForBack = () => {
    if (isWeek) {
      setWeekTimeStamp(getPreviousWeekTimeStamp(weekTimeStamp));
      fetchWeekData();
    } else {
      setMonthTimeStamp(
        getMidDayOfMonth(getPreviousMonthTimeStamp(monthTimeStamp))
      );
      fetchMonthData();
    }
  };

  //forward button click
  const onClickForForward = async () => {
    if (isWeek) {
      if (getNextWeekTimeStamp(weekTimeStamp) >= timestampInSeconds) {
        setWeekTimeStamp(timestampInSeconds);
        await fetchWeekData();
      } else {
        setWeekTimeStamp(getNextWeekTimeStamp(weekTimeStamp));
        await fetchWeekData();
      }
    } else {
      if (
        getNextMonthTimeStamp(monthTimeStamp) >=
        getPreviousMonthTimeStamp(getMidDayOfMonth(timestampInSeconds))
      ) {
        setMonthTimeStamp(
          getMidDayOfMonth(
            getPreviousMonthTimeStamp(getMidDayOfMonth(timestampInSeconds))
          )
        );
        await fetchMonthData();
      } else {
        setMonthTimeStamp(getNextMonthTimeStamp(monthTimeStamp));
        await fetchMonthData();
      }
    }
  };

  useEffect(() => {
    setSelected(languageMap.Stats);
    setCentreValue(allCentres[centreIdx]);
    fetchAllCentreData();
    fetchWeekData();
    fetchMonthData();
  }, [user, centreIdx, monthTimeStamp, weekTimeStamp, centreLoading]);

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
      <Box width={"95%"} p={1.3}>
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
            {centreLoading ? <ProcessLoading /> : centreValue}
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
          {allCentres.map((it, index) => (
            <MenuItem
              key={index}
              value={it}
              onClick={() => {
                handleClose();
                setCentreValue(it);
                setCentreIdx(index);
              }}
            >
              {it}
            </MenuItem>
          ))}
        </Menu>
      </Box>

      <Box
        height={"88%"}
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
          <Box mb={1.5}>
            <Typography
              variant={"body1"}
              fontWeight={"550"}
              fontSize={"1rem"}
              display={"flex"}
              alignItems={"center"}
              justifyContent={"center"}
            >
              {languageMap.ThisWeek}
            </Typography>
          </Box>

          <Box
            height={"85%"}
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
                {languageMap.Weighed}
              </Typography>
              <Typography variant={"h5"} fontWeight={600} textAlign={"center"}>
                {weekTotalData.noOfDogsWeighted}
              </Typography>
              <Typography variant={"body1"} textAlign={"center"}>
                {languageMap.Unweighed}
              </Typography>
              <Typography variant={"h5"} fontWeight={600} textAlign={"center"}>
                {weekTotalData.noOfDogsUnweighted}
              </Typography>
            </Box>
            <Box height={"55%"} width={"100%"}>
              {/*pie chart*/}
              <PieChart data={data} />
            </Box>
          </Box>
        </Box>

        <Box height={"100%"} width={"72%"}>
          <Box display={"flex"} justifyContent={"space-between"}>
            <Stack direction={"row"} spacing={1}>
              <IconButton onClick={onClickForBack}>
                <KeyboardArrowLeftIcon />
              </IconButton>
              <Typography
                variant={"body2"}
                display={"flex"}
                alignItems={"center"}
              >
                {isWeek
                  ? `${getStringOfDayFromTimeStamp(
                      getPreviousWeekTimeStamp(weekTimeStamp)
                    )} - ${getStringOfDayFromTimeStamp(weekTimeStamp)}`
                  : `${getMonthFromTimeStamp(monthTimeStamp)}`}
              </Typography>
              <IconButton onClick={onClickForForward}>
                <KeyboardArrowRightIcon />
              </IconButton>
            </Stack>
            <Stack spacing={2} direction={"row"}>
              <Button
                variant={"contained"}
                size={"small"}
                onClick={onClickForWeekButton}
                sx={{
                  height: "80%",
                  color: isWeek ? "#fff" : "black",
                  backgroundColor: isWeek
                    ? theme.palette.primary.dark
                    : theme.palette.primary.light,
                  "&:hover": {
                    color: "#fff",
                  },
                }}
              >
                {languageMap.Week}
              </Button>
              <Button
                variant={"contained"}
                size={"small"}
                onClick={onClickForMonthButton}
                sx={{
                  height: "80%",
                  color: !isWeek ? "#fff" : "black",
                  backgroundColor: !isWeek
                    ? theme.palette.primary.dark
                    : theme.palette.primary.light,
                  "&:hover": {
                    color: "#fff",
                  },
                }}
              >
                {languageMap.Month}
              </Button>
            </Stack>
          </Box>
          <Box
            height={"85%"}
            width={"100%"}
            sx={{
              backgroundColor: "#fff",
              borderRadius: "13px",
              boxShadow: 3,
            }}
          >
            {loading ? (
              <ProcessLoading />
            ) : isWeek ? (
              weekWeightData.length && (
                <LineChart
                  data={weekDataForLine}
                  rowLabel={languageMap.Date}
                  columnLabel={languageMap.Count}
                />
              )
            ) : (
              monthWeightData.length && (
                <LineChart
                  data={monthDataForLine}
                  rowLabel={languageMap.Date}
                  columnLabel={languageMap.Count}
                />
              )
            )}
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default StatsPage;
