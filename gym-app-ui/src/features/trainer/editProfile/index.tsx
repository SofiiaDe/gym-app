import {
  Avatar,
  Box,
  Button,
  Container,
  FormControl,
  FormControlLabel,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  Snackbar,
  Switch,
  TextField,
  Typography,
} from "@mui/material"
import { useEffect, useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { useNavigate } from "react-router-dom"
import Loader from "../../../app/components/Loader"
import { selectUsername, updateUsername } from "../../user/login/loginSlice"
import {
  useEditTrainerMutation,
  useGetTrainerProfileQuery,
} from "../trainerProfileAPI"
import { selectTrainerProfile } from "../trainerSlice"
import { setUsernameCookie } from "../../../utils"
import { TrainingType } from "../../training/types"

export default function EditProfile() {
  const navigate = useNavigate()
  const profile = useSelector(selectTrainerProfile)
  const usernameFromState = useSelector(selectUsername)

  const { isLoading, isError } = useGetTrainerProfileQuery({
    username: usernameFromState,
  })

  const [editTrainer] = useEditTrainerMutation()

  const [firstName, setFirstName] = useState(profile?.firstName || "")
  const [lastName, setLastName] = useState(profile?.lastName || "")
  const [username, setUsername] = useState(usernameFromState || "")
  const [specialization, setSpecialization] = useState(
    profile?.specialization || "",
  )
  const [active, setActive] = useState(profile?.active || false)

  useEffect(() => {
    if (!profile) return
    setFirstName(profile.firstName)
    setLastName(profile.lastName)
    setUsername(usernameFromState)
    setSpecialization(profile.specialization || "")
    setActive(profile.active)
  }, [profile])

  const [showSnackbar, setShowSnackbar] = useState(false)
  const handleSnackbarClose = () => setShowSnackbar(false)

  const dispatch = useDispatch()

  if (!profile || isLoading) return <Loader />
  if (isError) return <div>Something went wrong</div>

  const handleSaveClick = () => {
    editTrainer({
      firstName,
      lastName,
      username,
      specialization,
      active,
    }).then((response) => {
      if (!("error" in response)) {
        setUsernameCookie(username)
        dispatch(updateUsername(username))
        setShowSnackbar(true)
      }
    })
  }

  return (
    <Container maxWidth="md" sx={{ mt: 8 }}>
      <Box
        sx={{
          display: "flex",
          width: "100%",
          flexDirection: "row",
        }}
      >
        <Avatar sx={{ width: 250, height: 250 }} />
        <Box sx={{ height: "100%", alignSelf: "center", ml: 3 }}>
          <Typography variant="h6">Upload your photo</Typography>
          <Typography>Your photo should be in PNG or JPG format</Typography>
          <Box sx={{ mt: 2 }}>
            <Button variant="contained" color="primary">
              Choose image
            </Button>
            <Button variant="text" sx={{ ml: 3 }}>
              Remove
            </Button>
          </Box>
        </Box>
      </Box>
      <Grid container spacing={2} sx={{ mt: 2 }}>
        <Grid item xs={6}>
          <TextField
            required
            fullWidth
            label="First name"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />

          <TextField
            required
            sx={{ mt: 2 }}
            fullWidth
            label="Last name"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
          />

          <TextField
            required
            fullWidth
            label="User name"
            sx={{ mt: 2 }}
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </Grid>
        <Grid item xs={6}>
          <FormControl fullWidth sx={{ ml: 1 }}>
            <InputLabel id="specialization-label">Specialization</InputLabel>
            <Select
              labelId="specialization-label"
              value={specialization}
              onChange={(e) => setSpecialization(e.target.value)}
            >
              <MenuItem value={""}>none</MenuItem>
              {Object.values(TrainingType).map((key) => {
                return (
                  <MenuItem value={key} key={key}>
                    {key}
                  </MenuItem>
                )
              })}
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={12}>
          <FormControlLabel
            control={
              <Switch checked={active} onChange={() => setActive(!active)} />
            }
            label="Active"
          />
        </Grid>
      </Grid>
      <Grid container spacing={2} sx={{ mt: 2 }}>
        <Grid item xs={12} sx={{ justifyContent: "center", display: "flex" }}>
          <Button variant="text" onClick={() => navigate("/trainer/profile")}>
            Cancel
          </Button>
          <Button variant="contained" color="primary" onClick={handleSaveClick}>
            Save changes
          </Button>
        </Grid>
      </Grid>
      <Snackbar
        open={showSnackbar}
        autoHideDuration={5000}
        message="Successfully updated"
        onClose={handleSnackbarClose}
      />
    </Container>
  )
}
