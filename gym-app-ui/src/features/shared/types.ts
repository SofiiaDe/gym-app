export interface TrainerShortInfo {
  username: string
  firstName: string
  lastName: string
  specialization: string
}

export interface TraineeShortInfo {
  username: string
  firstName: string
  lastName: string
  isActive: boolean
}

export interface AddTraining {
  traineeUsername: string
  trainerUsername: string
  trainingName: string
  trainingType: string
  trainingDate: string
  trainingDuration: number
}
