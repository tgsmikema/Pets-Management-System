import { Box } from "@mui/material";

const DogPage = () => {
    const {id} = useParams();
  return (
    <Box>
        <Typography variant={"h3"} fontWeight={"800"} paddingBottom={'5px'}>
            Dog {id}
        </Typography>
    </Box>
    );
};

export default DogPage;