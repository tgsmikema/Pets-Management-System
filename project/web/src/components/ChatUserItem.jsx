import { Box, Divider, IconButton, Typography } from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications.js";

const ChatUserItem = ({ name, selectUser, setSelectUser, type, index }) => {
  return (
    <Box sx={{ textTransform: "capitalize" }}>
      <Box
        p={1.3}
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
      </Box>
      <Divider />
    </Box>
  );
};

export default ChatUserItem;
