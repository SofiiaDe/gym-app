import { createSlice } from "@reduxjs/toolkit"
import { RootState } from "../../app/store"
import { Trainee } from "./types"
import { traineeProfileApi } from "./traineeProfileAPI"

interface TraineeState {
  profile?: Trainee
}

const initialState: TraineeState = {
  profile: undefined,
}

export const traineeSlice = createSlice({
  name: "trainee",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addMatcher(
        traineeProfileApi.endpoints.getTraineeProfile.matchFulfilled,
        (state, { payload }) => {
          state.profile = payload
        },
      )
      .addMatcher(
        traineeProfileApi.endpoints.editTrainee.matchFulfilled,
        (state, { payload }) => {
          state.profile = payload
        },
      )
  },
})

export default traineeSlice.reducer

export const selectTraineeProfile = (state: RootState) => state.trainee.profile

// export const { logout } = loginSlice.actions
