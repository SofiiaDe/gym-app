import { Button, TextField } from "@mui/material"
import { ChangeEvent, useState } from "react"
import { useAddTrainingMutation } from "./addTrainingAPI"
import { TrainingType } from "./types"
import { DatePicker } from "@mui/x-date-pickers"
import styles from "./Training.module.css"


interface Props {
    onSubmit: () => void
}
export default function AddTraining(props: Props) {
    const { onSubmit } = props

    const [traineeUsername, setTraineeUsername] = useState('')
    const [trainerUsername, setTrainerUsername] = useState('')
    const [name, setName] = useState('')
    const [type, setType] = useState<TrainingType>(TrainingType.FITNESS)
    const [date, setDate] = useState<Date | null>(new Date('2024-01-15'))
    const [duration, setDuration] = useState(60)

    const [addTraining, { isError, error }] = useAddTrainingMutation()
    const handleSubmit = () => {
        addTraining({
            traineeUsername: traineeUsername,
            trainerUsername: trainerUsername,
            trainingName: name,
            trainingType: type,
            trainingDate: date?.toLocaleDateString() || '',
            trainingDuration: duration
        }).then(() => {
            onSubmit()
        })
    }

    const handleTraineeUsernameChange = (event: ChangeEvent<HTMLInputElement>) => setTraineeUsername(event.target.value)
    const handleTrainerUsernameChange = (event: ChangeEvent<HTMLInputElement>) => setTrainerUsername(event.target.value)
    const handleNameChange = (event: ChangeEvent<HTMLInputElement>) => setName(event.target.value)
    const handleTypeChange = (event: ChangeEvent<HTMLInputElement>) => setType(event.target.value.toLowerCase() as TrainingType)
    const handleDateChange = (date: Date | null) => { setDate(date) };
    const handleDurationChange = (event: ChangeEvent<HTMLInputElement>) => setDuration(parseInt(event.target.value))


    return (<div className={styles.formParent}>
        <div className={styles.form}>
            <TextField sx={{ m: 2 }} label="Trainee Username" variant="outlined" onChange={handleTraineeUsernameChange} />
            <TextField sx={{ m: 2 }} label="Trainer Username" variant="outlined" onChange={handleTrainerUsernameChange} />
            <TextField sx={{ m: 2 }} label="Training name" variant="outlined" onChange={handleNameChange} />
            <TextField sx={{ m: 2 }} label="Training type" variant="outlined" onChange={handleTypeChange} />
            <DatePicker sx={{ m: 2 }} label="Training date" format="yyyy-MM-dd" onChange={handleDateChange} value={date} minDate={new Date()} />
            <TextField sx={{ m: 2 }} label="Training duration" type="number" variant="outlined" onChange={handleDurationChange} />
            <Button onClick={handleSubmit} variant="text" size="small">Submit</Button>

            {isError ? "Failed to add training: " + JSON.stringify(error) : null}
        </div>
    </div>
    )
}