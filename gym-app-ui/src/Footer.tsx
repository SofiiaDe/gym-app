import imgUrl from "./gym_logo.png"
import { Facebook, Instagram, Twitter } from "@mui/icons-material"
import {
  Box,
  Button,
  Container,
  Grid,
  IconButton,
  TextField,
  Typography,
} from "@mui/material"
import { useNavigate } from "react-router-dom"

function Footer() {
  const navigate = useNavigate()

  return (
    <Box
      sx={(theme) => ({
        backgroundColor: theme.palette.grey[200],
        padding: theme.spacing(6),
        marginTop: "auto",
      })}
    >
      <Container maxWidth="lg">
        <Grid container spacing={4}>
          <Grid item xs={12} md={2}>
            <img src={imgUrl} alt="Logo" style={{ width: 80 }} />
            <Typography variant="h6">Gym App</Typography>
          </Grid>
          <Grid item xs={12} md={2} sx={{ mb: 2 }}>
            <Typography variant="h6">Product</Typography>
            <Box>
              <Button
                color="inherit"
                sx={{ mr: 2 }}
                onClick={() => navigate("/features")}
              >
                Features
              </Button>
              <Button
                color="inherit"
                sx={{ mr: 2 }}
                onClick={() => navigate("/price")}
              >
                Pricing
              </Button>
            </Box>
          </Grid>
          <Grid item xs={12} md={2} sx={{ mb: 2 }}>
            <Typography variant="h6">Resources</Typography>
            <Box>
              <Button color="inherit" sx={{ mr: 2 }}>
                Userguides
              </Button>
              <Button color="inherit" sx={{ mr: 2 }}>
                Contact
              </Button>
            </Box>
          </Grid>
          <Grid item xs={12} md={2} sx={{ mb: 2 }}>
            <Typography variant="h6">Company</Typography>
            <Box>
              <Button
                color="inherit"
                sx={{ mr: 2 }}
                onClick={() => navigate("/aboutUs")}
              >
                About us
              </Button>
              <Button color="inherit" sx={{ mr: 2 }}>
                Contact us
              </Button>
            </Box>
          </Grid>
          <Grid item xs={12} md={4}>
            <Typography variant="h6">Subscribe</Typography>
            <Typography variant="body2" color="text.secondary">
              Subscribe to our newsletter for the latest updates.
            </Typography>
            <TextField
              label="Email"
              variant="outlined"
              fullWidth
              margin="normal"
            />
            <Button variant="contained" color="primary">
              Subscribe
            </Button>
          </Grid>
        </Grid>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 2 }}>
          ©2023 Gym App · Privacy · Terms
        </Typography>
        <Box sx={{ mt: 2 }}>
          <IconButton color="inherit" sx={{ mr: 1 }}>
            <Facebook />
          </IconButton>
          <IconButton color="inherit" sx={{ mr: 1 }}>
            <Twitter />
          </IconButton>
          <IconButton color="inherit" sx={{ mr: 1 }}>
            <Instagram />
          </IconButton>
        </Box>
      </Container>
    </Box>
  )
}

export default Footer
