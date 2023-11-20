import { Action, ThunkAction, configureStore } from "@reduxjs/toolkit"
import { requireAuthApi } from "../features/requireAuth/requireAuthAPI"
import { traineeProfileApi } from "../features/trainee/traineeProfileAPI"
import { registerTraineeApi } from "../features/trainee/registerTrainee/registerTraineeAPI"
import { traineeSlice } from "../features/trainee/traineeSlice"
import { trainerSlice } from "../features/trainer/trainerSlice"
import { trainerProfileApi } from "../features/trainer/trainerProfileAPI"
import { registerTrainerApi } from "../features/trainer/registerTrainer/registerTrainerAPI"
import { changePasswordApi } from "../features/user/changePassword/changePasswordAPI"
import { loginApi } from "../features/user/login/loginAPI"
import { loginSlice } from "../features/user/login/loginSlice"

export const store = configureStore({
  reducer: {
    [loginApi.reducerPath]: loginApi.reducer,
    login: loginSlice.reducer,

    [traineeProfileApi.reducerPath]: traineeProfileApi.reducer,
    trainee: traineeSlice.reducer,

    [trainerProfileApi.reducerPath]: trainerProfileApi.reducer,
    trainer: trainerSlice.reducer,

    [registerTraineeApi.reducerPath]: registerTraineeApi.reducer,
    [registerTrainerApi.reducerPath]: registerTrainerApi.reducer,

    [trainerProfileApi.reducerPath]: trainerProfileApi.reducer,
    [requireAuthApi.reducerPath]: requireAuthApi.reducer,
    [changePasswordApi.reducerPath]: changePasswordApi.reducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware()
      .concat(loginApi.middleware)
      // .concat(registerTraineeApi.middleware)
      .concat(registerTrainerApi.middleware)
      .concat(traineeProfileApi.middleware)
      .concat(trainerProfileApi.middleware)
      .concat(requireAuthApi.middleware)
      .concat(changePasswordApi.middleware),
})

export type AppDispatch = typeof store.dispatch
export type RootState = ReturnType<typeof store.getState>
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>
