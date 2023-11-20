export interface TrainingSearchResults {
  trainingDate: string
  trainingName: string
  trainingType: string
  traineeName: string
  trainingDuration: number
}

export interface TrainingSearchRequest extends TrainerRequestBase {
  traineeName?: string
  periodFrom?: string
  periodTo?: string
}

export interface TrainerRequestBase {
  username: string
}
