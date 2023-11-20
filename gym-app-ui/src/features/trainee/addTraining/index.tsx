import { useEffect, useState } from "react"

import {
  Box,
  Button,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  Snackbar,
  TextField,
  Typography,
} from "@mui/material"
import { useSelector } from "react-redux"
import { selectUsername } from "../../user/login/loginSlice"
import {
  useAddTrainingMutation,
  useGetTrainersInfoQuery,
} from "../traineeProfileAPI"
import { useNavigate } from "react-router-dom"
import { TrainingType } from "../../training/types"
import { TrainerShortInfo } from "../../shared/types"

export default function AddTraining() {
  const [name, setName] = useState("")
  const [trainingDate, setTrainingDate] = useState("")
  const [duration, setDuration] = useState(0)
  const [trainingType, setTrainingType] = useState("")
  const [trainer, setTrainer] = useState("")
  const [isTrainerSelected, setIsTrainerSelected] = useState(false)
  const [filteredTrainerUserNames, setFilteredTrainerUserNames] = useState<
    TrainerShortInfo[] | undefined
  >(undefined)

  useEffect(() => {
    if (!trainer) {
      setIsTrainerSelected(false)
    } else {
      setIsTrainerSelected(true)
      if (trainers) {
        const selectedTrainer = trainers.find((t) => t.username === trainer)

        selectedTrainer && setTrainingType(selectedTrainer.specialization)
      }
    }
  }, [trainer])

  useEffect(() => {
    if (trainer) return
    if (trainingType) {
      if (trainers) {
        const trainersOfThisTrainingType = trainers.filter(
          (t) => t.specialization === trainingType,
        )
        setFilteredTrainerUserNames(trainersOfThisTrainingType)
      }
    } else {
      setFilteredTrainerUserNames(undefined)
    }
  }, [trainingType])

  const [addTraining] = useAddTrainingMutation()
  const username: string = useSelector(selectUsername)
  const { data: trainers } = useGetTrainersInfoQuery({ username })

  const [showSnackbar, setShowSnackbar] = useState(false)
  const navigate = useNavigate()

  const handleSubmit = () => {
    addTraining({
      traineeUsername: username,
      trainingDate,
      trainingName: name,
      trainingDuration: duration,
      trainingType,
      trainerUsername: trainer,
    }).then((res) => {
      if (!("error" in res)) {
        setName("")
        setTrainingDate("")
        setDuration(0)
        setTrainingType("")
        setTrainer("")
        setShowSnackbar(true)
      }
    })
  }

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      <Typography variant="h3" sx={{ textAlign: "center" }}>
        Add training
      </Typography>

      <Grid container spacing={2} sx={{ mt: 3 }}>
        <Grid item xs={12} sm={6}>
          <TextField
            variant="outlined"
            required
            fullWidth
            id="name"
            label="Name"
            name="name"
            autoComplete="name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <TextField
            sx={{ mt: 2 }}
            variant="outlined"
            required
            fullWidth
            id="trainingDate"
            label="Training Date"
            name="trainingDate"
            type="date"
            InputLabelProps={{
              shrink: true,
            }}
            inputProps={{
              min: new Date().toISOString().split("T")[0],
            }}
            value={trainingDate}
            onChange={(e) => setTrainingDate(e.target.value)}
          />
          <TextField
            sx={{ mt: 2 }}
            variant="outlined"
            required
            fullWidth
            id="duration"
            label="Duration"
            name="duration"
            type="number"
            value={duration}
            onChange={(e) => setDuration(Number(e.target.value))}
          />
          <FormControl
            variant="outlined"
            required
            fullWidth
            sx={{ mt: 2 }}
            disabled={isTrainerSelected}
          >
            <InputLabel id="trainingTypeLabel">Training Type</InputLabel>
            <Select
              labelId="trainingTypeLabel"
              id="trainingType"
              label="Training Type"
              name="trainingType"
              value={trainingType}
              onChange={(e) => setTrainingType(e.target.value)}
            >
              {Object.values(TrainingType).map((key) => (
                <MenuItem value={key} key={key}>
                  {key}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <Box
            sx={{
              mt: 2,
              width: "100%",
              display: "flex",
              justifyContent: "end",
            }}
          >
            <Button
              type="submit"
              variant="text"
              color="primary"
              onClick={() => navigate("/trainee/trainings")}
            >
              Cancel
            </Button>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              onClick={handleSubmit}
            >
              Add Training
            </Button>
          </Box>
        </Grid>
        <Grid item xs={12} sm={6}>
          <FormControl variant="outlined" required fullWidth>
            <InputLabel id="trainerLabel">Trainer</InputLabel>
            <Select
              labelId="trainerLabel"
              label="Trainer"
              value={trainer}
              onChange={(e) => setTrainer(e.target.value)}
            >
              <MenuItem value="">none</MenuItem>
              {(filteredTrainerUserNames || trainers)?.map((trainer) => (
                <MenuItem value={trainer.username} key={trainer.username}>
                  {trainer.firstName + " " + trainer.lastName}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>
      </Grid>
      <Snackbar
        open={showSnackbar}
        autoHideDuration={5000}
        message="Successfully added"
        onClose={() => setShowSnackbar(false)}
      />
    </Box>
  )
}
