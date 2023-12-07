import { Box, Button, Grid, Typography } from "@mui/material"
import { useSelector } from "react-redux"
import Loader from "../../../app/components/Loader"
import { selectUsername } from "../../user/login/loginSlice"
import { useGetTrainerProfileQuery } from "../trainerProfileAPI"
import { selectTrainerProfile } from "../trainerSlice"
import MyTrainees from "./MyTrainees"
import { useNavigate } from "react-router-dom"
import { useTheme } from "@mui/material/styles"

const headerSx = { fontWeight: "bolder" }

export default function TrainerProfile() {
  const username = useSelector(selectUsername)
  const { isLoading, isError } = useGetTrainerProfileQuery({
    username: username,
  })
  const profile = useSelector(selectTrainerProfile)

  const navigate = useNavigate()

  const handleEditTrainerClick = () => navigate("/trainer/profile/edit")
  const handleChangePasswordClick = () => navigate("/changepassword")
  const handleTrainerTrainingsClick = () => navigate("/trainer/trainings")

  const theme = useTheme()

  if (isLoading) {
    return <Loader />
  }

  if (!profile || isError) {
    return <Typography variant="h6">Error loading profile</Typography>
  }

  return (
    <Box sx={{ flexGrow: 1, p: 2 }}>
      <Grid container spacing={2}>
        <Grid
          item
          xs={12}
          sm={4}
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <img
            src="https://via.placeholder.com/300"
            alt="Profile"
            style={{ width: "100%", height: "auto", borderRadius: "15%" }}
          />

          <Typography variant="subtitle1" sx={headerSx}>
            Status
          </Typography>
          <Typography variant="body1">
            {profile.isActive ? "Active" : "Inactive"}
          </Typography>
          <Typography variant="subtitle1" sx={headerSx}>
            First Name
          </Typography>
          <Typography variant="body1">{profile.firstName}</Typography>
          <Typography variant="h6" sx={headerSx}>
            Last Name
          </Typography>
          <Typography variant="body1">{profile.lastName}</Typography>
          <Typography variant="h6" sx={headerSx}>
            Username
          </Typography>
          <Typography variant="body1">{username}</Typography>
          <Typography variant="h6" sx={headerSx}>
            Specialization
          </Typography>
          <Typography variant="body1">{profile.specialization}</Typography>
          <Box
            sx={{
              display: "flex",
              flexDirection: "row",
              alignItems: "center",
              justifyContent: "center",
              marginTop: 2,
            }}
          >
            <Button variant="contained" color="primary" sx={{ m: 1 }} onClick={handleEditTrainerClick}>
              Edit Profile
            </Button>
            <Button variant="contained" color="primary" sx={{ m: 1 }} onClick={handleChangePasswordClick}>
              Change Password
            </Button>
          </Box>
        </Grid>
        <Grid
          item
          xs={12}
          sm={8}
          sx={{
            display: "flex",
            flexDirection: "column",
          }}
        >
          <MyTrainees trainees={profile.trainees} />
        </Grid>
      </Grid>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Typography variant="h4">My Trainings</Typography>
        <Typography
          variant="body1"
          sx={{
            textAlign: "center",
            mt: 2,
            [theme.breakpoints.up("md")]: {
              width: 680,
            },
          }}
        >
          Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed
          malesuada, sapien vel bibendum luctus, velit sapien aliquet sapien,
          vel bibendum luctus, velit sapien aliquet sapien.
        </Typography>
        <Button
          variant="contained"
          color="primary"
          sx={{ mt: 2 }}
          onClick={handleTrainerTrainingsClick}
        >
          View Trainings
        </Button>
      </Box>
    </Box>
  )
}
