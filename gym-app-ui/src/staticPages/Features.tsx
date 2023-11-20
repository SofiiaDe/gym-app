import React from "react"
import { Typography, Grid, Paper } from "@mui/material"

export default function Features() {
  return (
    <div>
      <Typography variant="h4" component="h2" gutterBottom>
        Features
      </Typography>
      <Grid container spacing={2}>
        <Grid
          item
          xs={12}
          sm={6}
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
          }}
        >
          <Typography variant="h6" component="h3" gutterBottom>
            Feature 1
          </Typography>
          <Typography variant="body1" gutterBottom>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla
            facilisi. Sed euismod, tortor id aliquet tincidunt, nunc elit
            tincidunt urna, sed tincidunt nisl nunc id nunc.
          </Typography>
        </Grid>
        <Grid item xs={12} sm={6}>
          <img
            src="https://via.placeholder.com/300"
            alt="Placeholder"
            style={{ height: "auto" }}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <img
            src="https://via.placeholder.com/300"
            alt="Placeholder"
            style={{ height: "auto" }}
          />
        </Grid>
        <Grid
          item
          xs={12}
          sm={6}
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
          }}
        >
          <Typography variant="h6" component="h3" gutterBottom>
            Feature 2
          </Typography>
          <Typography variant="body1" gutterBottom>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla
            facilisi. Sed euismod, tortor id aliquet tincidunt, nunc elit
            tincidunt urna, sed tincidunt nisl nunc id nunc.
          </Typography>
        </Grid>
      </Grid>
    </div>
  )
}
