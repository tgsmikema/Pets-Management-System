import { Box, useTheme, Stack, Typography } from "@mui/material";
import Logo from "../assets/logo.png";
import NavItem from "./NavItem.jsx";
import HomeIcon from "@mui/icons-material/Home";
import BarChartIcon from "@mui/icons-material/BarChart";
import ChatIcon from "@mui/icons-material/Chat";
import PersonIcon from "@mui/icons-material/Person";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";
import LanguageMenu from "./LanguageMenu.jsx";

//this is the top, we set the navigation item in it, if this is login page, these navigation item will hidden
const TopBar = ({ isLogin }) => {
  const theme = useTheme();
  const { selected, setSelected } = useUtilProvider();
  const { languageMap } = useLanguageProvider();
  return (
    <Box
      height={"9%"}
      width={"100%"}
      display={"flex"}
      justifyContent={"space-between"}
      sx={{
        backgroundColor: `${theme.palette.primary.light}`,
      }}
    >
      <Box height={"100%"} ml={2}>
        <img src={Logo} style={{ height: "100%" }} />
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
            name={languageMap.Home}
            to={"/home"}
            selected={selected}
            setSelected={setSelected}
          />
          <NavItem
            icon={<BarChartIcon />}
            name={languageMap.Stats}
            to={"/stats"}
            selected={selected}
            setSelected={setSelected}
          />
          <NavItem
            icon={<ChatIcon />}
            name={languageMap.Chat}
            to={"/chat"}
            selected={selected}
            setSelected={setSelected}
          />
          <NavItem
            icon={<PersonIcon />}
            name={languageMap.Profile}
            to={"/profile"}
            selected={selected}
            setSelected={setSelected}
          />

          <LanguageMenu />
        </Stack>
      )}
    </Box>
  );
};

export default TopBar;
