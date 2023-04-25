import {
  Dialog,
  DialogTitle,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
} from "@mui/material";
import ChatUserItem from "./ChatUserItem.jsx";

const UnChattedUserDialog = ({
  open,
  handleClose,
  unChattedUserList,
  handleItemClick,
}) => {
  return (
    <Dialog
      onClose={handleClose}
      open={open}
      sx={{
        overflow: "auto",
      }}
    >
      <DialogTitle>Select a new user to chat</DialogTitle>
      <List sx={{ pt: 0 }}>
        {unChattedUserList.map((it, index) => (
          <ListItem disableGutters>
            <ListItemButton
              key={index}
              onClick={() => {
                handleItemClick(index);
              }}
            >
              <ListItemText primary={`${it.name} (${it.type})`} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Dialog>
  );
};

export default UnChattedUserDialog;
