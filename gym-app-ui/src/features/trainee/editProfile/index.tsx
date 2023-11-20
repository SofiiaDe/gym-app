import {
  Avatar,
  Box,
  Button,
  Container,
  FormControlLabel,
  Grid,
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
  useEditTraineeMutation,
  useGetTraineeProfileQuery,
} from "../traineeProfileAPI"
import { selectTraineeProfile } from "../traineeSlice"
import { setUsernameCookie } from "../../../utils"

export default function EditProfile() {
  const navigate = useNavigate()
  const profile = useSelector(selectTraineeProfile)
  const usernameFromState = useSelector(selectUsername)

  const { isLoading, isError } = useGetTraineeProfileQuery({
    username: usernameFromState,
  })

  const [editTrainee] = useEditTraineeMutation()

  const [firstName, setFirstName] = useState(profile?.firstName || "")
  const [lastName, setLastName] = useState(profile?.lastName || "")
  const [username, setUsername] = useState(usernameFromState || "")
  const [address, setAddress] = useState(profile?.address || "")
  const [birthDate, setBirthDate] = useState(profile?.birthDate || "")
  const [active, setActive] = useState(profile?.active || false)

  const dispatch = useDispatch()

  useEffect(() => {
    if (!profile) return
    setFirstName(profile.firstName)
    setLastName(profile.lastName)
    setUsername(usernameFromState)
    setAddress(profile.address || "")
    setBirthDate(profile.birthDate || "")
    setActive(profile.active)
  }, [profile])

  const [showSnackbar, setShowSnackbar] = useState(false)
  const handleSnackbarClose = () => setShowSnackbar(false)

  if (!profile || isLoading) return <Loader />
  if (isError) return <div>Something went wrong</div>

  const handleSaveClick = () => {
    editTrainee({
      firstName,
      lastName,
      username,
      address,
      birthDate,
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
        <Grid item xs={12}>
          <TextField
            required
            fullWidth
            label="First name"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            required
            fullWidth
            label="Last name"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            required
            fullWidth
            label="User name"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            required
            fullWidth
            label="Address"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            required
            fullWidth
            label="Date of birth"
            type="date"
            InputLabelProps={{ shrink: true }}
            value={birthDate}
            onChange={(e) => setBirthDate(e.target.value)}
          />
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
          <Button variant="text" onClick={() => navigate("/trainee/profile")}>
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
