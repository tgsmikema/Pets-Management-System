import {
  Dialog,
  DialogTitle,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
} from "@mui/material";

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
        {unChattedUserList?.map((it, index) => (
          <ListItem disableGutters key={index}>
            <ListItemButton
              onClick={() => {
                handleItemClick(index);
              }}
            >
              <ListItemText
                primary={`${it.firstName} ${it.lastName} (${it.userType})`}
              />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Dialog>
  );
};

export default UnChattedUserDialog;
