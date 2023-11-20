export interface Training {
  id: number
  traineeUsername: string
  trainerUsername: string
  trainingName: string
  trainingType: TrainingType
  trainingDate: string
  trainingDuration: number
}

export interface AddTrainingModel extends Omit<Training, "id"> {}

export enum TrainingType {
  FITNESS = "fitness",
  YOGA = "yoga",
  ZUMBA = "zumba",
  STRETCHING = "stretching",
  RESISTANCE = "resistance",
}
