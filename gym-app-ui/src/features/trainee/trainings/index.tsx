import {
  Box,
  Button,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
} from "@mui/material"
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers"
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns"
import { useState } from "react"
import { useSelector } from "react-redux"
import { useNavigate } from "react-router-dom"
import { formatDate } from "../../../utils"
import { TrainingType } from "../../training/types"
import { selectUsername } from "../../user/login/loginSlice"
import { useSearchTraineeTrainingsMutation } from "../traineeProfileAPI"
import TrainingList from "./TrainingList"

const Trainings = () => {
  const [search, { data }] = useSearchTraineeTrainingsMutation()
  const username = useSelector(selectUsername)
  const navigate = useNavigate()

  const [trainerName, setTrainerName] = useState("")
  const [specialization, setSpecialization] = useState("")
  const [fromDate, setFromDate] = useState<Date | null>(null)
  const [toDate, setToDate] = useState<Date | null>(null)

  const handleSearch = () => {
    search({
      username,
      trainerName,
      trainingType: specialization === "" ? null : specialization,
      periodFrom: fromDate ? formatDate(fromDate) : undefined,
      periodTo: toDate ? formatDate(toDate) : undefined,
    })
  }

  return (
    <>
      <Typography variant="h3" sx={{ textAlign: "center" }}>
        Trainings
      </Typography>
      <Button
        variant="contained"
        sx={{ mt: 3 }}
        onClick={() => navigate("/trainee/addtraining")}
      >
        Add training
      </Button>
      <Typography variant="h6" sx={{ mt: 3 }}>
        Search Filters
      </Typography>
      <Grid container spacing={2} sx={{ mt: 1 }}>
        <Grid item xs={6}>
          <Box sx={{ display: "flex", flexDirection: "row" }}>
            <TextField
              label="Trainer Name"
              value={trainerName}
              onChange={(e) => setTrainerName(e.target.value)}
              fullWidth
            />
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
          </Box>
          <Button variant="contained" onClick={handleSearch} sx={{ mt: 3 }}>
            Search
          </Button>
        </Grid>
        <Grid item xs={6}>
          <LocalizationProvider dateAdapter={AdapterDateFns}>
            <DatePicker
              label="From"
              value={fromDate}
              onChange={(date) => setFromDate(date)}
              format="yyyy-MM-dd"
              sx={{ mr: 1 }}
            />
            <DatePicker
              label="To"
              value={toDate}
              onChange={(date) => setToDate(date)}
              format="yyyy-MM-dd"
            />
          </LocalizationProvider>
        </Grid>
      </Grid>

      <TrainingList trainings={data} />
    </>
  )
}

export default Trainings
