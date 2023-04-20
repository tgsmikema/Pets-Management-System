import { Box, Button, Dialog } from "@mui/material";
import { useParams } from "react-router-dom";
import { useUtilProvider } from "../providers/UtilProvider.jsx";
import { useEffect, useState } from "react";
import Typography from "@mui/material/Typography";
import EditIcon from '@mui/icons-material/Edit';
import FlagCircleIcon from "@mui/icons-material/FlagCircle";
import ErrorIcon from "@mui/icons-material/Error";
import AddIcon from "@mui/icons-material/Add";
import LineChart from "../components/LineChart.jsx";
import { DataGrid } from "@mui/x-data-grid";
import EditDog from "../components/EditDog.jsx";
import Weight from "../components/Weight.jsx";


const styles = {
  boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
  padding: '2% 2% 1% 2%',
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  height: '100%',
};

//TODO: replace hardcoded values with db
const rows = [
  {
    id: "6",
    last: "2021/10/05",
    weight: "14.8",
  },
  {
    id: "5",
    last: "2021/10/05",
    weight: "14.8",
  },
  {
    id: "4",
    last: "2021/10/05",
    weight: "14.8",
  },
  {
    id: "3",
    last: "2021/10/05",
    weight: "14.8",
  },
  {
    id: "2",
    last: "2021/10/05",
    weight: "14.8",
  },
  {
    id: "1",
    last: "2021/10/05",
    weight: "14.8",
  },
];

const columns = [
  { field: "last", headerName: "last check-in", flex: 1.5 },
  { field: "weight", headerName: "weight(kg)", flex: 1 },
];

  const dataForLine = [
    {
      id: "weight",
      data: [
        {
          x: "Dec",
          y: 7,
        },
        {
          x: "Jan",
          y: 12,
        },
        {
          x: "Feb",
          y: 14,
        },
        {
          x: "Mar",
          y: 15,
        },
        {
          x: "Apr",
          y: 17,
        },
      ],
    },
  ];



function DogPage () {
    const {id} = useParams();
    const { setSelected } = useUtilProvider();
  useEffect(() => {
    setSelected(null);
  });

  // control flag button
  //TODO: set/update based on db
  const [isFlag, setIsFlag] = useState(false);
  const handleFlagClick = () => {
    setIsFlag(!isFlag);
  };
  const flagColor = isFlag ? "#E34B28" : "#000000";

  // control alert button
  //TODO: set/update based on db
  const [isAlert, setIsAlert] = useState(false);
  const handleAlertClick = () => {
    setIsAlert(!isAlert);
  };
  const alertColor = isAlert ? "#E34B28" : "#000000";

  //control edit button
  const [openEditDog, setOpenEditDog] = useState(false);
  const handleEditDogClick = () => {
    setOpenEditDog(true);
  };
  const handleEditDogClose = () => {
    setOpenEditDog(false);
  };

  //control add button
  const [openWeight, setOpenWeight] = useState(false);
  const handleWeightClick = () => {
    setOpenWeight(true);
  };
  const handleWeightClose = () => {
    setOpenWeight(false);
  };

  return (
    <Box>
        {/* Top banner */}
      <Box sx={styles} >
        <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', marginRight: '1%'}}>
            {/* Add button */}
            <Box display="flex" justifyContent="flex-end" alignItems="center">
                <Button
                onClick={handleWeightClick}
                variant="contained"
                color="primary"
                sx={{
                    borderRadius: "50%",
                    minWidth: 0,
                    width: 48,
                    height: 48,
                }}
                >
                <AddIcon sx={{ fontSize: 45, color: "#000" }} />
                </Button>
            </Box>
            <Button onClick={handleEditDogClick} style={{marginTop: '10%'}}>
                <EditIcon sx={{ fontSize: 45, color: "#000" }} />
            </Button>
        </div>
        <div style={{ flex: '1 1 0%' }}>
          <Typography variant={"h6"} fontWeight={"300"} color={"#000"}>
           8093
          </Typography>
          <Typography variant={"h3"} fontWeight={"600"} color={"#000"}>
            Oliver
          </Typography>
          <Typography variant={"h5"} fontWeight={"500"} color={"#000"}>
            Basset Hound
          </Typography>
          <Typography variant={"h5"} fontWeight={"500"} color={"#000"}>
            Māngere
          </Typography>
        </div>
        <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'flex-end', marginRight: '1%'}}>
            <div style={{display: 'flex', alignItems: 'baseline'}}>
                <Typography variant={"h3"} fontWeight={"500"} color={"#000"}>
                    14.8
                </Typography>
                <Typography variant={"h5"} fontWeight={"300"} color={"#000"}>
                    kg
                </Typography>
            </div>
            <Typography variant={"h6"} fontWeight={"400"} color={"#000"}>
                08/03/23
            </Typography>
        </div>
        <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'flex-end'}}>
            <Button onClick={handleFlagClick}>
                <FlagCircleIcon sx={{ fontSize: 60, color: flagColor }} />
            </Button>
            <Button onClick={handleAlertClick}>
                <ErrorIcon sx={{ fontSize: 60, color: alertColor }} />
            </Button>
        </div>
      </Box>
      
      <div style={{display: 'flex'}}>
        <Box
            height={"500px"}
            width={"80%"}
            sx={{
              backgroundColor: "#fff",
              borderRadius: "13px",
              boxShadow: 3,
              margin: '2%',
            }}
          >
            <LineChart data={dataForLine} />
        </Box>
      <Box
        height={"500px"}
        width={"30%"}
        sx={{
          backgroundColor: "#fff",
          borderRadius: "13px",
          boxShadow: 3,
          margin: '2%',
        }}
      >
        <DataGrid
          rows={rows}
          columns={columns}
          style={{ fontSize: '16px' }}
        />
      </Box>

      </div>
      {/* Edit dog modal */}
        <Dialog
          open={openEditDog}
          onClose={handleEditDogClose}
          maxWidth="xs"
          PaperProps={{ sx: { borderRadius: "20px", height: "70%" } }}
        >
          <EditDog onClose={handleEditDogClose} id={id} />
      </Dialog>
      {/* Add weight modal */}
      <Dialog
          open={openWeight}
          onClose={handleWeightClose}
          maxWidth="xs"
          PaperProps={{ sx: { borderRadius: "20px", height: "70%" } }}
        >
          <Weight onClose={handleWeightClose} id={id} />
      </Dialog>
        
    </Box>
    );
};

export default DogPage;