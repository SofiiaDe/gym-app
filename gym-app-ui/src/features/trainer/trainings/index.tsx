import { Box, Button, Grid, TextField, Typography } from "@mui/material"
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers"
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns"
import { useState } from "react"
import { useSelector } from "react-redux"
import { useNavigate } from "react-router-dom"
import { selectUsername } from "../../user/login/loginSlice"
import { useSearchTrainerTrainingsMutation } from "../trainerProfileAPI"
import TrainingList from "./TrainingList"
import { formatDate } from "../../../utils"

const Trainings = () => {
  const [search, { data }] = useSearchTrainerTrainingsMutation()
  const username = useSelector(selectUsername)
  const navigate = useNavigate()

  const [traineeName, setTraineeName] = useState("")
  const [fromDate, setFromDate] = useState<Date | null>(null)
  const [toDate, setToDate] = useState<Date | null>(null)

  const handleSearch = () => {
    search({
      username,
      traineeName,
      periodFrom: fromDate ? formatDate(fromDate) : undefined,
      periodTo: toDate ? formatDate(toDate) : undefined,
    })
  }

  return (
    <>
      <Typography variant="h3" sx={{ textAlign: "center" }}>
        Trainings
      </Typography>
      {/* <Button
        variant="contained"
        sx={{ mt: 3 }}
        onClick={() => navigate("/trainer/addtraining")}
      >
        Add training
      </Button> */}
      <Typography variant="h6" sx={{ mt: 3 }}>
        Search Filters
      </Typography>
      <Grid container spacing={2} sx={{ mt: 1 }}>
        <Grid item xs={6}>
          <Box sx={{ display: "flex", flexDirection: "row" }}>
            <TextField
              label="Trainee Name"
              value={traineeName}
              onChange={(e) => setTraineeName(e.target.value)}
              fullWidth
            />
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
