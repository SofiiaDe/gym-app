export interface TrainingSearchResults {
  trainingDate: string
  trainingName: string
  trainingType: string
  trainerName: string
  trainingDuration: number
}

export interface TrainingSearchRequest extends TraineeRequestBase {
  trainerName?: string
  periodFrom?: string
  periodTo?: string
  trainingType?: string | null
}

export interface TraineeRequestBase {
  username: string
}
