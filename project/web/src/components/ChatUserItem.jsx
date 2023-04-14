import { Box, Divider, IconButton, Typography } from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications.js";

const ChatUserItem = ({ name, selectUser, setSelectUser, type, index }) => {
  return (
    <Box sx={{ textTransform: "capitalize" }}>
      <Box
        p={1.2}
        position={"relative"}
        onClick={() => {
          console.log(name);
          setSelectUser(index);
        }}
        sx={{
          cursor: "pointer",
          backgroundColor: selectUser === index ? "#CADDEC" : "transparent",
          "&:hover": {
            backgroundColor: "#e8effa",
          },
        }}
      >
        <Typography variant={"h5"} fontWeight={"600"}>
          {name}
        </Typography>
        <Typography variant={"body1"}>{type}</Typography>
        <IconButton
          sx={{
            color: "#fa3e3e",
            position: "absolute",
            right: 4,
            top: 4,
            zIndex: 999,
          }}
        >
          <NotificationsIcon />
        </IconButton>
      </Box>
      <Divider />
    </Box>
  );
};

export default ChatUserItem;
