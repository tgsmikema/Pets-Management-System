import {Box, useTheme} from "@mui/material";
import {useAuth} from "./providers/AuthProvider.jsx";
import {customColor} from "./theme.js";

function App() {
    const {user} = useAuth()
    const theme = useTheme()
    const colors = customColor
    return (
        user === null ?
            (
                <Box
                    sx={{
                        color: colors.grey
                    }}
                >
                    The user has not logged in
                </Box>
            ) :
            (
                <Box>
                    The user has logged in
                </Box>
            )
    )
}

export default App
