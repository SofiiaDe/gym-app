import { Typography, Grid, Avatar, Box } from '@mui/material';

export default function AboutUs() {

    return (
        <Box sx={{ flexGrow: 1, p: 2 }} >
            <Typography variant="h3" sx={{ textAlign: "center", fontWeight: "bolder", m: 2 }}>
                About Us
            </Typography>
            <Typography variant="body2" sx={{ textAlign: "center", m: 2 }} >
                Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur.
            </Typography>
            <Box sx={{ display: "flex", justifyContent: "center", mb: 4 }}>
                <img src="https://via.placeholder.com/900x450" alt="big horizontal picture" />
            </Box>
            <Grid container justifyContent="center">
                <Grid item xs={12} sm={2} sx={{ m: 1 }}>
                    <Typography variant="h5" sx={{ fontWeight: "bolder", m: 2 }} >Our Team</Typography>
                    <Typography variant="body2" sx={{ m: 2 }} >
                        Temporibus autem quibusdam et aut officiis debitis aut
                    </Typography>
                </Grid>
                <Grid item xs={12} sm={2} sx={{ backgroundColor: "#f9f9f9", m: 1 }}>
                    <Avatar
                        alt="John Doe"
                        src="https://via.placeholder.com/100x100"
                        sx={{ width: 100, height: 100, m: 2 }}
                    />
                    <Typography variant="body1" sx={{ fontWeight: "bolder", m: 1 }}>John Doe</Typography>
                    <Typography variant="body2" sx={{ color: "blue", m: 1 }}>Professional Title</Typography>
                    <Typography sx={{ fontSize: 12, m: 1 }}>
                        Ut enim ad minima veniam, quis nostrum
                    </Typography>
                </Grid>
                <Grid item xs={12} sm={2} sx={{ backgroundColor: "#f9f9f9", m: 1 }}>
                    <Avatar
                        alt="Jane Doe"
                        src="https://via.placeholder.com/100x100"
                        sx={{ width: 100, height: 100, m: 2 }}
                    />
                    <Typography variant="body1" sx={{ fontWeight: "bolder", m: 1 }}>Jane Doe</Typography>
                    <Typography variant="body2" sx={{ color: "blue", m: 1 }} >Professional Title</Typography>
                    <Typography sx={{ fontSize: 12, m: 1 }}>
                        Nam libero tempore, cum soluta
                    </Typography>
                </Grid>
                <Grid item xs={12} sm={2} sx={{ backgroundColor: "#f9f9f9", m: 1 }}>
                    <Avatar
                        alt="Jack Doe"
                        src="https://via.placeholder.com/100x100"
                        sx={{ width: 100, height: 100, m: 2 }}
                    />
                    <Typography variant="body1" sx={{ fontWeight: "bolder", m: 1 }} >Jack Doe</Typography>
                    <Typography variant="body2" sx={{ color: "blue", m: 1 }}>Professional Title</Typography>
                    <Typography sx={{ fontSize: 12, m: 1 }}>
                        Excepteur sint occaecat cupidatat non proident
                    </Typography>
                </Grid>
            </Grid>
        </Box>
    );
};