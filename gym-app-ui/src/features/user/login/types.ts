import { UserType } from "../types"

export interface LoginModel {
  username: string
  password: string
  captchaValue: string
}

export interface LoginResponse {
  accessToken: string
  refreshToken: string
  userType: UserType
}
