import { Box, Button, Snackbar, TextField, Typography } from "@mui/material"
import { useState } from "react"
import { UserLoginData } from "../../../features/user/register/UserLoginData"
import { useRegisterTraineeMutation } from "./registerTraineeAPI"
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers"
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns"
import Loader from "../../../app/components/Loader"

export default function RegisterTrainee() {
  const [firstName, setFirstName] = useState("")
  const [lastName, setLastName] = useState("")
  const [birthDate, setBirthDate] = useState<Date | null>(
    new Date("2000-01-15"),
  )
  const [address, setAddress] = useState("")
  const [validationError, setValidationError] = useState(false)
  const [showSnackbar, setShowSnackbar] = useState(false)

  const [registerTrainee, { isSuccess, isLoading, data }] =
    useRegisterTraineeMutation()

  const handleFirstName = (e: any) => setFirstName(e.target.value)
  const handleLastName = (e: any) => setLastName(e.target.value)
  const handleBirthDate = (birthDate: Date | null) => {
    setBirthDate(birthDate)
  }
  const handleAddress = (e: any) => setAddress(e.target.value)

  const handleSubmit = (e: any) => {
    e.preventDefault()

    if (!firstName || !lastName) {
      setValidationError(true)
      return
    }

    validationError && setValidationError(false)

    registerTrainee({
      firstName,
      lastName,
      birthDate: birthDate?.toISOString().slice(0, 10) || "",
      address,
    }).catch(() => {
      setShowSnackbar(true)
    })
  }

  const handleSnackbarClose = () => setShowSnackbar(false)

  const showFirstNameError = validationError && !firstName
  const showLastNameError = validationError && !lastName
  return (
    <>
      {isLoading ? <Loader /> : null}
      <Typography variant="h4" sx={{ mt: 2, mb: 1 }}>
        Registration
      </Typography>
      <Typography variant="h5" sx={{ mt: 2, mb: 1 }}>
        Trainee
      </Typography>
      {isSuccess && data ? (
        <UserLoginData
          firstName={firstName}
          username={data.username}
          password={data.password}
        />
      ) : null}

      {!isSuccess ? (
        <Box sx={{ width: "100%", display: "flex", justifyContent: "center" }}>
          <img src="https://via.placeholder.com/300x450" alt="placeholder" />
          <Box sx={{ width: 360, ml: 4 }}>
            <TextField
              sx={{ mt: 2 }}
              label="First Name"
              variant="outlined"
              onChange={handleFirstName}
              fullWidth
              error={showFirstNameError}
              helperText={showFirstNameError && "First name is required"}
            />
            <TextField
              sx={{ mt: 2 }}
              label="Last Name"
              variant="outlined"
              onChange={handleLastName}
              fullWidth
              error={validationError && !lastName}
              helperText={showLastNameError && "Last name is required"}
            />
            <LocalizationProvider dateAdapter={AdapterDateFns}>
              <DatePicker
                sx={{ mt: 2, width: "100%" }}
                views={["year", "month", "day"]}
                label="Date of Birth"
                format="yyyy-MM-dd"
                onChange={handleBirthDate}
                value={birthDate}
                maxDate={new Date()}
              />
            </LocalizationProvider>
            <TextField
              sx={{ mt: 2 }}
              label="Address"
              variant="outlined"
              onChange={handleAddress}
              fullWidth
            />
            <Button
              sx={{ mt: 2 }}
              onClick={handleSubmit}
              variant="contained"
              fullWidth
            >
              Submit
            </Button>
          </Box>
        </Box>
      ) : null}

      <Snackbar
        open={showSnackbar}
        autoHideDuration={5000}
        message="Failed to register. Please, try again later"
        onClose={handleSnackbarClose}
      />
    </>
  )
}
