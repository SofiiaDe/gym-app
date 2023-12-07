import { TraineeShortInfo } from "../shared/types"

export interface Trainer {
  firstName: string
  lastName: string
  username: string
  specialization: string
  trainees: TraineeShortInfo[]
  isActive: boolean
}
