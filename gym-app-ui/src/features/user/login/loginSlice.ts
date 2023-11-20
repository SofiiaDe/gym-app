import { PayloadAction, createSlice } from "@reduxjs/toolkit"
import { RootState } from "../../../app/store"
import { deleteAuthCookies, getUsernameCookie } from "../../../utils"
import { UserType } from "../types"
import { loginApi } from "./loginAPI"

interface LoginState {
  username?: string
  userType?: UserType
}

const initialState: LoginState = {}

export const loginSlice = createSlice({
  name: "login",
  initialState,
  reducers: {
    updateUsername: (state, action: PayloadAction<string>) => {
      state.username = action.payload
      return state
    },
    logout: (state) => {
      deleteAuthCookies()
      state = {}
      return state
    },
  },
  extraReducers: (builder) => {
    builder.addMatcher(
      loginApi.endpoints.login.matchFulfilled,
      (state, { payload, meta }) => {
        state.userType = payload.userType
        state.username = meta.arg.originalArgs.username
      },
    )
  },
})

export default loginSlice.reducer

export const selectUsername = (state: RootState) =>
  state.login.username || getUsernameCookie() || ""

export const { logout, updateUsername } = loginSlice.actions
