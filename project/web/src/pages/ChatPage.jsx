import { Box } from "@mui/material";
import { useEffect } from "react";
import { useUtilProvider } from "../providers/UtilProvider.jsx";

const ChatPage = () => {
  const { setSelected } = useUtilProvider();
  useEffect(() => {
    setSelected("Chat");
  });
  return <Box>this is chat page</Box>;
};

export default ChatPage;
