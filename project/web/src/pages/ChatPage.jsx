import { Box, Button, Divider, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { useUtilProvider } from "../providers/UtilProvider.jsx";

const ChatPage = () => {
  const { setSelected } = useUtilProvider();
  useEffect(() => {
    setSelected("Chat");
  });

  const [selectUser, setSelectUser] = useState();

  const useList = [
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

  return (
    <Box height={"100%"} width={"100%"} display={"flex"}>
      <Box
        height={"100%"}
        width={"25%"}
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

        {useList.map((it, index) => (
          <Box sx={{ textTransform: "capitalize" }}>
            <Box
              p={1.2}
              onClick={() => {
                console.log(it.name);
                setSelectUser(index);
              }}
              sx={{
                cursor: "pointer",
                backgroundColor:
                  selectUser === index ? "#CADDEC" : "transparent",
                "&:hover": {
                  backgroundColor: "#e8effa",
                },
              }}
            >
              <Typography variant={"h5"} fontWeight={"600"}>
                {it.name}
              </Typography>
              <Typography variant={"body1"}>{it.type}</Typography>
            </Box>
            <Divider />
          </Box>
        ))}
      </Box>
    </Box>
  );
};

export default ChatPage;
