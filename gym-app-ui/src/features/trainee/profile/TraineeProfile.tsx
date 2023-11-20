import { Box, Button, Grid, Typography } from "@mui/material"
import { useTheme } from "@mui/material/styles"
import { useState } from "react"
import { useSelector } from "react-redux"
import { useNavigate } from "react-router-dom"
import Loader from "../../../app/components/Loader"
import { deleteAuthCookies } from "../../../utils"
import { selectUsername } from "../../user/login/loginSlice"
import {
  useDeleteTraineeMutation,
  useGetTraineeProfileQuery,
} from "../traineeProfileAPI"
import { selectTraineeProfile } from "../traineeSlice"
import DeleteDialog from "./DeleteDialog"
import MyTrainers from "./MyTrainers"

const headerSx = { fontWeight: "bolder" }

export default function TraineeProfile() {
  const username = useSelector(selectUsername)
  const { isLoading, isError } = useGetTraineeProfileQuery({
    username,
  })

  const [deleteTrainee] = useDeleteTraineeMutation()
  const profile = useSelector(selectTraineeProfile)
  const navigate = useNavigate()

  const handleEditTraineeClick = () => navigate("/trainee/profile/edit")
  const handleChangePasswordClick = () => navigate("/changepassword")
  const handleDeleteProfileClick = () => setShowDeleteDialog(true)
  const handleTraineeTrainingsClick = () => navigate("/trainee/trainings")

  const theme = useTheme()

  const [showDeleteDialog, setShowDeleteDialog] = useState(false)

  if (isLoading) return <Loader />

  if (!profile || isError) {
    return <Typography variant="h6">Error loading profile</Typography>
  }

  const handleDeleteProfileConfirmClick = () => {
    deleteTrainee({
      username,
    }).then(() => {
      deleteAuthCookies()
      navigate("/deleted")
    })
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
            Date of Birth
          </Typography>
          <Typography variant="body1">{profile.birthDate}</Typography>
          <Typography variant="h6" sx={headerSx}>
            Address
          </Typography>
          <Typography variant="body1">{profile.address}</Typography>
          <Box
            sx={{
              display: "flex",
              flexDirection: "row",
              alignItems: "center",
              justifyContent: "center",
              marginTop: 2,
            }}
          >
            <Button
              variant="contained"
              color="primary"
              sx={{ m: 1 }}
              onClick={handleEditTraineeClick}
            >
              Edit Profile
            </Button>
            <Button
              variant="contained"
              color="primary"
              sx={{ m: 1 }}
              onClick={handleChangePasswordClick}
            >
              Change Password
            </Button>
            <Button
              variant="contained"
              color="error"
              sx={{ m: 1 }}
              onClick={handleDeleteProfileClick}
            >
              Delete Profile
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
          <MyTrainers trainers={profile.trainers} />
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
          onClick={handleTraineeTrainingsClick}
        >
          View Trainings
        </Button>
      </Box>
      <DeleteDialog
        open={showDeleteDialog}
        onClose={() => setShowDeleteDialog(false)}
        onConfirm={() => handleDeleteProfileConfirmClick()}
      />
    </Box>
  )
}
