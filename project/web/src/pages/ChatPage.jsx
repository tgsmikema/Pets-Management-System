import {
  Box,
  Button,
  Divider,
  TextField,
  Typography,
  InputAdornment,
} from "@mui/material";
import { useCallback, useEffect, useState } from "react";
import moment from "moment";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import SendIcon from "@mui/icons-material/Send";
import { useAuth } from "../providers/AuthProvider.jsx";
import ChatUserItem from "../components/ChatUserItem.jsx";
import UnChattedUserDialog from "../components/UnChattedUserDialog.jsx";

const userList = [
  {
    name: "Mike Ma",
    type: "admin",
  },
  {
    name: "Anna Verdi",
    type: "vet",
  },
  {
    name: "John Doe",
    type: "volunteer",
  },
  {
    name: "Qingyang LI",
    type: "admin",
  },
  {
    name: "Hanako Yamada",
    type: "admin",
  },
  {
    name: "Oliver",
    type: "admin",
  },
  {
    name: "Mike Ma",
    type: "admin",
  },
  {
    name: "Mike Ma",
    type: "admin",
  },
  {
    name: "Mike Ma",
    type: "admin",
  },
  {
    name: "Mike Ma",
    type: "admin",
  },
  {
    name: "Mike Ma",
    type: "admin",
  },
  {
    name: "Mike Ma",
    type: "admin",
  },
  {
    name: "Mike Ma",
    type: "admin",
  },
];

const unChattedUserList = [
  {
    name: "Mike Ma",
    type: "admin",
  },
  {
    name: "Anna Verdi",
    type: "vet",
  },
  {
    name: "John Doe",
    type: "volunteer",
  },
  {
    name: "Qingyang LI",
    type: "admin",
  },
  {
    name: "Hanako Yamada",
    type: "admin",
  },
  {
    name: "Oliver",
    type: "admin",
  },
];

const chatHistory = [
  {
    id: 1,
    date: "04/09/2022 19:34",
    messageContent: "Hello mike, how are you?",
    ToUser: { id: 1, name: "mike" },
    FromUser: { id: 2, name: "admin" },
  },

  {
    id: 2,
    date: "04/09/2022 19:35",
    messageContent: "I am fine.Thank you ,and you?",
    ToUser: { id: 2, name: "admin" },
    FromUser: { id: 1, name: "mike" },
  },

  {
    id: 1,
    date: "04/09/2022 19:34",
    messageContent: "Hello mike, how are you?",
    ToUser: { id: 1, name: "mike" },
    FromUser: { id: 2, name: "admin" },
  },

  {
    id: 2,
    date: "04/09/2022 19:35",
    messageContent: "I am fine.Thank you ,and you?",
    ToUser: { id: 2, name: "admin" },
    FromUser: { id: 1, name: "mike" },
  },

  {
    id: 1,
    date: "04/09/2022 19:34",
    messageContent: "Hello mike, how are you?",
    ToUser: { id: 1, name: "mike" },
    FromUser: { id: 2, name: "admin" },
  },

  {
    id: 2,
    date: "04/09/2022 19:35",
    messageContent: "I am fine.Thank you ,and you?",
    ToUser: { id: 2, name: "admin" },
    FromUser: { id: 1, name: "mike" },
  },

  {
    id: 1,
    date: "04/09/2022 19:34",
    messageContent: "Hello mike, how are you?",
    ToUser: { id: 1, name: "mike" },
    FromUser: { id: 2, name: "admin" },
  },
  {
    id: 1,
    date: "04/09/2022 19:34",
    messageContent: "Hello mike, how are you?",
    ToUser: { id: 1, name: "mike" },
    FromUser: { id: 2, name: "admin" },
  },
  {
    id: 2,
    date: "04/09/2022 19:35",
    messageContent: "I am fine.Thank you ,and you?",
    ToUser: { id: 2, name: "admin" },
    FromUser: { id: 1, name: "mike" },
  },

  {
    id: 2,
    date: "04/09/2022 19:35",
    messageContent:
      "perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos ",
    ToUser: { id: 2, name: "admin" },
    FromUser: { id: 1, name: "mike" },
  },
  {
    id: 1,
    date: "04/09/2022 19:36",
    messageContent:
      "perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos ",
    ToUser: { id: 1, name: "mike" },
    FromUser: { id: 2, name: "admin" },
  },

  {
    id: 1,
    date: `${moment(parseInt(1672141137 * 1000)).format(
      "YYYY-MM-DD HH:mm:ss"
    )}`,
    messageContent:
      "Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omniss",
    ToUser: { id: 1, name: "mike" },
    FromUser: { id: 2, name: "admin" },
  },
];

const ChatPage = () => {
  const { setSelected } = useUtilProvider();
  useEffect(() => {
    setSelected("Chat");
    console.log(user.userName);
    console.log(
      moment(parseInt(1672141137 * 1000)).format("YYYY-MM-DD HH:mm:ss")
    );
  }, [userList]);

  const { user } = useAuth();
  const [selectUser, setSelectUser] = useState(-1);

  const [open, setOpen] = useState(false);
  const handleClose = useCallback(() => {
    setOpen(false);
  }, [open, setOpen]);

  const handleItemClick = useCallback(
    (index) => {
      userList.unshift(unChattedUserList[index]);
      unChattedUserList.splice(index, 1);
      handleClose();
      setSelectUser(0);
    },
    [unChattedUserList]
  );

  return (
    <Box height={"100%"} width={"100%"} display={"flex"}>
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
            <Typography>+ New Message</Typography>
          </Button>
        </Box>
        <Divider />

        {userList.map((it, index) => (
          <ChatUserItem
            name={it.name}
            type={it.type}
            index={index}
            selectUser={selectUser}
            setSelectUser={setSelectUser}
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
                {userList[selectUser].name}
              </Typography>
              <Typography variant={"body1"}>
                {userList[selectUser].type}
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
            {chatHistory.map((it, index) => (
              <Box
                display={"flex"}
                flexDirection={"column"}
                maxWidth={"60%"}
                alignSelf={
                  it.FromUser.name === user.userName ? "flex-end" : "flex-start"
                }
                px={7}
                py={1.5}
              >
                <Box
                  alignSelf={
                    it.FromUser.name === user.userName
                      ? "flex-end"
                      : "flex-start"
                  }
                >
                  {it.date}
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
            ))}
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
                      <SendIcon />
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
