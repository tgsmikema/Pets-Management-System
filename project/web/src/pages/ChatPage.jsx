import {
  Box,
  Button,
  Divider,
  TextField,
  Typography,
  InputAdornment,
  useTheme,
} from "@mui/material";
import { useCallback, useEffect, useState } from "react";
import moment from "moment";
import { constants } from "../constants.js";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import SendIcon from "@mui/icons-material/Send";
import { useAuth } from "../providers/AuthProvider.jsx";
import ChatUserItem from "../components/ChatUserItem.jsx";
import UnChattedUserDialog from "../components/UnChattedUserDialog.jsx";
import axios from "axios";
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";

const ChatPage = () => {
  const { setSelected } = useUtilProvider();
  const theme = useTheme();

  const { languageMap } = useLanguageProvider();

  const { user } = useAuth();
  const [selectUser, setSelectUser] = useState(-1);

  const [open, setOpen] = useState(false);
  const handleClose = useCallback(() => {
    setOpen(false);
  }, [open, setOpen]);

  const timeStampToDate = (timeStamp) => {
    return moment(parseInt(timeStamp * 1000)).format("YYYY-MM-DD HH:mm:ss");
  };

  //fetch the backend data

  //1. fetch chatted user list
  const [chattedUserList, setChattedUserList] = useState([]);
  const fetchChattedUser = useCallback(async () => {
    const res = await axios.get(
      `${constants.backend}/chat/getAlreadyMessagedPeopleList?currentUserId=${user.id}`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
    setChattedUserList(res.data);
  }, []);

  //2. fetch unchatted user List
  const [unChattedUserList, setUnChattedUserList] = useState([]);
  const fetchUnChattedUser = useCallback(async () => {
    const res = await axios.get(
      `${constants.backend}/chat/getNeverMessagedPeopleList?currentUserId=${user.id}`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
    setUnChattedUserList(res.data);
  }, []);

  //3.fetch chat history
  const [chatHistory, setChatHistory] = useState([]);
  const fetchChatHistory = useCallback(async (friendId) => {
    const res = await axios.get(
      `${constants.backend}/chat/getChatHistory?currentUserId=${user.id}&chatToUserId=${friendId}`,
      {
        headers: {
          Authorization: "Basic " + user.token,
        },
      }
    );
    setChatHistory(res.data);
  }, []);

  //4. send the message
  const [message, setMessage] = useState("");
  const sendMessage = useCallback(
    async (fromUserId, toUserId, messageContent) => {
      const res = await axios.post(
        `${constants.backend}/chat/send`,
        {
          fromUserId: fromUserId,
          toUserId: toUserId,
          messageContent: messageContent,
        },
        {
          headers: {
            Authorization: "Basic " + user.token,
          },
        }
      );
    },
    []
  );

  const handleItemClick = useCallback(
    async (index) => {
      const friend = unChattedUserList[index];
      chattedUserList.unshift(friend);
      await fetchChatHistory(friend.id);
      unChattedUserList.splice(index, 1);
      handleClose();
      setSelectUser(0);
    },
    [unChattedUserList]
  );

  useEffect(() => {
    setSelected(languageMap.Chat);
    fetchChattedUser();
    fetchUnChattedUser();
  }, []);
  return (
    <Box
      height={"84vh"}
      width={"100%"}
      display={"flex"}
      sx={{
        backgroundColor: theme.palette.secondary.main,
      }}
    >
      <UnChattedUserDialog
        open={open}
        handleClose={handleClose}
        unChattedUserList={unChattedUserList}
        handleItemClick={handleItemClick}
      />
      {/* left sidebar */}
      <Box
        height={"100%"}
        width={"22%"}
        boxShadow={4}
        display={"flex"}
        flexDirection={"column"}
        sx={{
          backgroundColor: "#fff",
          overflowY: "auto",
        }}
      >
        <Box p={1.3}>
          <Button
            variant={"contained"}
            fullWidth={true}
            onClick={() => {
              setOpen(true);
            }}
            sx={{
              borderRadius: "20px",
              "&:hover": {
                color: "#fff",
              },
            }}
          >
            <Typography>+ {languageMap.NewMessage}</Typography>
          </Button>
        </Box>
        <Divider />

        {chattedUserList?.map((it, index) => (
          <ChatUserItem
            key={index}
            name={`${it.firstName} ${it.lastName}`}
            type={it.userType}
            index={index}
            selectUser={selectUser}
            setSelectUser={setSelectUser}
            onClick={async () => {
              await fetchChatHistory(it.id);
            }}
          />
        ))}
      </Box>
      {/* the chat message */}
      {selectUser !== -1 && (
        <Box
          height={"100%"}
          width={"78%"}
          display={"flex"}
          flexDirection={"column"}
        >
          <Box
            height={"12%"}
            width={"100%"}
            display={"flex"}
            alignItems={"center"}
            sx={{
              boxShadow: 4,
              textTransform: "capitalize",
            }}
          >
            <Box
              display={"flex"}
              flexDirection={"column"}
              justifyContent={"center"}
              ml={1.5}
            >
              <Typography variant={"h5"} fontWeight={600}>
                {`${chattedUserList[selectUser].firstName} ${chattedUserList[selectUser].lastName}`}
              </Typography>
              <Typography variant={"body1"}>
                {chattedUserList[selectUser].userType}
              </Typography>
            </Box>
          </Box>
          {/*  chat message*/}
          <Box
            height={"80%"}
            width={"100%"}
            display={"flex"}
            flexDirection={"column"}
            alignItems={"flex-start"}
            sx={{
              overflowY: "auto",
            }}
          >
            {chatHistory.length === 0 ? (
              <Typography
                variant={"h4"}
                alignSelf={"center"}
                color={"#000"}
                mt={10}
              >
                No Chat History
              </Typography>
            ) : (
              chatHistory.map((it, index) => (
                <Box
                  key={index}
                  display={"flex"}
                  flexDirection={"column"}
                  maxWidth={"60%"}
                  alignSelf={
                    it.fromUserId === user.id ? "flex-end" : "flex-start"
                  }
                  px={7}
                  py={1.5}
                >
                  <Box
                    alignSelf={
                      it.fromUserId === user.id ? "flex-end" : "flex-start"
                    }
                  >
                    {timeStampToDate(it.timestamp)}
                  </Box>
                  <Box
                    p={2}
                    sx={{
                      borderRadius: "13px",
                      backgroundColor: "#fff",
                    }}
                  >
                    {it.messageContent}
                  </Box>
                </Box>
              ))
            )}
          </Box>
          <Box
            height={"8%"}
            width={"100%"}
            display={"flex"}
            alignItems={"center"}
            sx={{
              boxShadow: 4,
            }}
          >
            <Box width={"95%"} ml={2}>
              <TextField
                size={"small"}
                fullWidth={true}
                value={message}
                onChange={(e) => {
                  setMessage(e.target.value);
                }}
                InputProps={{
                  endAdornment: (
                    <InputAdornment
                      position="end"
                      sx={{
                        color: "#000",
                        "&:hover": {
                          cursor: "pointer",
                          color: "#8BB6D8",
                        },
                      }}
                    >
                      <SendIcon
                        onClick={async () => {
                          const friend = chattedUserList[selectUser];
                          const mess = message;
                          setMessage("");
                          setChatHistory([
                            ...chatHistory,
                            {
                              fromUserId: user.id,
                              toUserId: friend.id,
                              messageContent: mess,
                              timestamp: Date.now() / 1000,
                            },
                          ]);
                          await sendMessage(user.id, friend.id, mess);
                        }}
                      />
                    </InputAdornment>
                  ),
                }}
                sx={{
                  backgroundColor: "#fff",
                }}
              />
            </Box>
          </Box>
        </Box>
      )}
    </Box>
  );
};

export default ChatPage;
