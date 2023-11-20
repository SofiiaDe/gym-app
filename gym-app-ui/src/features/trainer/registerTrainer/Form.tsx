import {
  Box,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
  Snackbar,
} from "@mui/material"
import { useState } from "react"
import { RegisterTrainerModel } from "./types"

interface Props {
  onSubmit: (data: RegisterTrainerModel) => void
}

export default function Form(props: Props) {
  const { onSubmit } = props

  const [firstName, setFirstName] = useState("")
  const [lastName, setLastName] = useState("")
  const [specialization, setSpecialization] = useState<number | "">("")

  const handleFirstName = (e: any) => setFirstName(e.target.value)
  const handleLastName = (e: any) => setLastName(e.target.value)
  const handleSpecialization = (e: any) => setSpecialization(e.target.value)

  const [validationError, setValidationError] = useState(false)
  const [showSnackbar, setShowErrorSnackbar] = useState(false)
  const handleSnackbarClose = () => setShowErrorSnackbar(false)

  const handleSubmit = (e: any) => {
    e.preventDefault()

    if (!firstName || !lastName || !specialization) {
      setValidationError(true)
      return
    }

    validationError && setValidationError(false)

    onSubmit({
      firstName,
      lastName,
      specialization,
    })
  }

  return (
    <>
      <Box sx={{ width: "100%", display: "flex", justifyContent: "center" }}>
        <img src="https://via.placeholder.com/300x450" alt="placeholder" />
        <Box sx={{ width: 360, ml: 4 }}>
          <TextField
            sx={{ mt: 4 }}
            label="First Name"
            variant="outlined"
            onChange={handleFirstName}
            fullWidth
            required
            error={validationError}
            helperText={validationError ? "This field is required" : ""}
          />
          <TextField
            sx={{ mt: 4 }}
            label="Last Name"
            variant="outlined"
            onChange={handleLastName}
            fullWidth
            required
            error={validationError}
            helperText={validationError ? "This field is required" : ""}
          />
          <FormControl fullWidth sx={{ mt: 4 }}>
            <InputLabel id="demo-simple-select-label" required>
              Specialization
            </InputLabel>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={specialization}
              label="Specialization"
              onChange={handleSpecialization}
              error={validationError}
            >
              <MenuItem value={1}>Fitness</MenuItem>
              <MenuItem value={2}>Yoga</MenuItem>
              <MenuItem value={3}>Zumba</MenuItem>
              <MenuItem value={4}>Stretching</MenuItem>
              <MenuItem value={5}>Resistance</MenuItem>
            </Select>
          </FormControl>
          <Button
            sx={{ mt: 4 }}
            fullWidth
            onClick={handleSubmit}
            variant="contained"
          >
            Submit
          </Button>
        </Box>
      </Box>
      <Snackbar
        open={showSnackbar}
        autoHideDuration={5000}
        message="Failed to register. Please, try again later"
        onClose={handleSnackbarClose}
      />
    </>
  )
}
