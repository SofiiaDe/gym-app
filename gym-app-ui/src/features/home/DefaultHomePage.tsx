import { Box, Button, Container, Grid, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

export default function DefaultHomePage() {
    const navigate = useNavigate()
    const handleJoinUsClick = () => navigate("/register")

    return (
        <Box sx={{ flexGrow: 1, backgroundColor: "theme.palette.background.paper", pt: 8, pb: 6 }}>
            <Container maxWidth="md">
                <Typography variant="h1" sx={{ fontSize: "3rem", fontWeight: "bolder", textAlign: "center", mb: "2rem" }}>
                    Let's start training
                </Typography>
                <Typography variant="h4" sx={{ fontSize: "1.5rem", textAlign: "center", mb: 8 }}>
                    Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.
                </Typography>
                <Grid container spacing={4}>
                    <Grid item xs={12} sx={{ display: "flex", justifyContent: "center" }}>
                        <iframe width="560" height="315"
                            src="https://www.youtube.com/embed/1SV9saENd0U?si=0ql9RqOVcHfaPmmw&amp;start=164"
                            title="YouTube video player"
                            frameBorder="0"
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                            allowFullScreen
                        >
                        </iframe>
                    </Grid>
                </Grid>
            </Container>
            <Box sx={{
                backgroundColor: "white", pt: 8, pb: 6,
                backgroundSize: "cover", backgroundPosition: "center"
            }}>
                <Container maxWidth="md">
                    <Typography variant="h1" sx={{
                        color: "blue",
                        fontSize: "3rem",
                        fontWeight: "bolder",
                        textAlign: "center",
                        mb: "2rem",
                        mt: 10
                    }}>
                        Join us
                    </Typography>
                    <Typography variant="h4" sx={{
                        color: "#000",
                        fontSize: "1.5rem",
                        textAlign: "center",
                        marginBottom: "2rem"
                    }}>
                        Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.
                    </Typography>
                    <Button variant="contained" sx={{ m: "auto", mt: 8, fontSize: "1.5rem", display: "block", padding: "1rem 2rem" }}
                        onClick={handleJoinUsClick} >
                        Join us
                    </Button>
                </Container>
            </Box>
        </Box >
    );
}
