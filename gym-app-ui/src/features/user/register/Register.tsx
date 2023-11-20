import { Button, Paper, Typography } from "@mui/material"
import { useState } from "react"
import RegisterTrainee from "../../trainee/registerTrainee/RegisterTrainee"
import RegisterTrainer from "../../trainer/registerTrainer/RegisterTrainer"
import styles from "./Register.module.css"

type RegistrationType = "trainer" | "trainee"

export default function Register() {
  const [type, setType] = useState<RegistrationType | undefined>(undefined)

  if (type === "trainer") return <RegisterTrainer />
  if (type === "trainee") return <RegisterTrainee />

  const handleRegisterTrainerClick = () => {
    setType("trainer")
  }
  const handleRegisterTraineeClick = () => {
    setType("trainee")
  }

  return (
    <>
      <Typography className={styles.title} variant="h3" sx={{ mt: 2, mb: 1 }}>
        Join Us
      </Typography>
      <Paper className={styles.root}>
        <div className={styles.section}>
          <div className={styles.content}>
            <Typography variant="h4" sx={{ m: 4 }}>
              Register as Trainer
            </Typography>
            <Typography variant="h6" sx={{ m: 4 }}>
              Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
              eiusmod tempor incididunt ut labore et dolore magna aliqua.
            </Typography>
            <Button
              sx={{ m: 2 }}
              onClick={handleRegisterTrainerClick}
              variant="contained"
            >
              Join us
            </Button>
          </div>
        </div>
        <div className={styles.section}>
          <img
            className={styles.image}
            src="https://via.placeholder.com/150"
            alt="TrainerPhoto"
          />
        </div>
      </Paper>

      <Paper className={styles.root}>
        <div className={styles.section}>
          <div className={styles.content}>
            <Typography variant="h4" sx={{ m: 4 }}>
              Register as Trainee
            </Typography>
            <Typography variant="h6" sx={{ m: 4 }}>
              Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
              eiusmod tempor incididunt ut labore et dolore magna aliqua.
            </Typography>
            <Button
              sx={{ m: 2 }}
              onClick={handleRegisterTraineeClick}
              variant="contained"
            >
              Join us
            </Button>
          </div>
        </div>
        <div className={styles.section}>
          <img
            className={styles.image}
            src="https://via.placeholder.com/150"
            alt="TraineePhoto"
          />
        </div>
      </Paper>
    </>
  )
}
