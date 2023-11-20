import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from "@mui/material"
import { TrainingSearchResults } from "./types"

interface Props {
  trainings?: TrainingSearchResults[]
}

const TrainingList = (props: Props) => {
  const { trainings } = props

  if (!trainings) {
    return
  }

  if (trainings.length === 0) {
    return (
      <Typography variant="body1" sx={{ mt: 3 }}>
        No trainings
      </Typography>
    )
  }

  return (
    <>
      <Typography variant="h6" sx={{ mt: 5 }}>
        My trainings
      </Typography>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Date</TableCell>
            <TableCell>Training Name</TableCell>
            <TableCell>Type</TableCell>
            <TableCell>Trainee Name</TableCell>
            <TableCell>Duration</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {trainings.map((training) => {
            return (
              <TableRow
                key={training.traineeName + "_" + training.trainingDate}
              >
                <TableCell>{training.trainingDate}</TableCell>
                <TableCell>{training.trainingName}</TableCell>
                <TableCell>{training.trainingType}</TableCell>
                <TableCell>{training.traineeName}</TableCell>
                <TableCell>{training.trainingDuration}</TableCell>
              </TableRow>
            )
          })}
        </TableBody>
      </Table>
    </>
  )
}

export default TrainingList
