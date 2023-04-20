import { Box, useTheme, Stack } from "@mui/material";
import Logo from "../assets/logo.png";
import NavItem from "./NavItem.jsx";
import HomeIcon from "@mui/icons-material/Home";
import BarChartIcon from "@mui/icons-material/BarChart";
import ChatIcon from "@mui/icons-material/Chat";
import PersonIcon from "@mui/icons-material/Person";
import { useUtilProvider } from "../providers/UtilProvider.jsx";

//this is the top, we set the navigation item in it, if this is login page, these navigation item will hidden
const TopBar = ({ isLogin }) => {
  const theme = useTheme();
  const { selected, setSelected } = useUtilProvider();
  return (
    <Box
      height={"8%"}
      width={"100%"}
      display={"flex"}
      justifyContent={"space-between"}
      sx={{
        backgroundColor: `${theme.palette.primary.light}`,
      }}
    >
      <Box height={"100%"} ml={2} marginBottom={1} marginTop={1}>
        <img src={Logo} style={{ height: "80%" }} />
      </Box>

      {!isLogin && (
        <Stack
          direction={"row"}
          height={"100%"}
          mr={3}
          display={"flex"}
          alignItems={"center"}
          spacing={3}
        >
          <NavItem
            icon={<HomeIcon />}
            name={"Home"}
            to={"/home"}
            selected={selected}
            setSelected={setSelected}
          />
          <NavItem
            icon={<BarChartIcon />}
            name={"Stats"}
            to={"/stats"}
            selected={selected}
            setSelected={setSelected}
          />
          <NavItem
            icon={<ChatIcon />}
            name={"Chat"}
            to={"/chat"}
            selected={selected}
            setSelected={setSelected}
          />
          <NavItem
            icon={<PersonIcon />}
            name={"Profile"}
            to={"/profile"}
            selected={selected}
            setSelected={setSelected}
          />
        </Stack>
      )}
    </Box>
  );
};

export default TopBar;
