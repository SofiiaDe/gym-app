import { Typography, Button, Box, Avatar } from '@mui/material';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import { useNavigate } from 'react-router-dom';

export default function ChangePasswodConfirmation() {
    const navigate = useNavigate()
    const handleLoginClick = () => navigate("/login")

    return (
        <Box sx={{ display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center", height: "100%" }}>
            <Avatar sx={{ m: 1, backgroundColor: "theme.palette.success.main" }}>
                <CheckCircleOutlineIcon />
            </Avatar>
            <Typography variant="h5" align="center">
                Password changed
            </Typography>
            <Typography variant="body1" align="center">
                Please proceed sign in with the new password
            </Typography>
            <Button
                variant="contained"
                color="primary"
                sx={{ mt: 2 }}
                onClick={handleLoginClick}
            >
                Sign in
            </Button>
        </Box>
    );
}