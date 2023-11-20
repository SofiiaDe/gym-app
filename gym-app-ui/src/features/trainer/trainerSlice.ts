import { createSlice } from "@reduxjs/toolkit"
import { RootState } from "../../app/store"
import { Trainer } from "./types"
import { trainerProfileApi } from "./trainerProfileAPI"

interface TrainerState {
  profile?: Trainer
}

const initialState: TrainerState = {
  profile: undefined,
}

export const trainerSlice = createSlice({
  name: "trainer",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addMatcher(
      trainerProfileApi.endpoints.getTrainerProfile.matchFulfilled,
      (state, { payload }) => {
        state.profile = payload
      },
    )
  },
})

export default trainerSlice.reducer

export const selectTrainerProfile = (state: RootState) => state.trainer.profile

// export const { logout } = loginSlice.actions
