import { TrainerShortInfo } from "../shared/types"

export interface Trainee {
  firstName: string
  lastName: string
  username: string
  birthDate?: string // todo Date?
  address?: string
  email?: string
  trainers: TrainerShortInfo[]
  active: boolean
}
