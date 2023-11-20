import { TrainingType } from "../../training/types"

export interface TraineeTrainingInfo {
    trainingName: string
    trainingDate: string
    trainingType: TrainingType
    trainingDuration: number
    trainerName: string
}
