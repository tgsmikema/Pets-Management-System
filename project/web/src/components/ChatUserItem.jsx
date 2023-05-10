import { Box, Divider, Typography } from "@mui/material";

const ChatUserItem = ({
  name,
  selectUser,
  setSelectUser,
  type,
  index,
  onClick,
}) => {
  return (
    <Box sx={{ textTransform: "capitalize" }}>
      <Box
        p={1.3}
        onClick={() => {
          onClick();
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
