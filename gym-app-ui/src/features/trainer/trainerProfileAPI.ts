import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { API_URL } from "../../app/constants"
import { getAccessToken } from "../../utils"
import { TrainerTrainingInfo } from "./profile/types"
import { Trainer } from "./types"
import { GymAppRequestParams } from "../../app/types"
import { AddTraining } from "../shared/types"
import {
  TrainerRequestBase,
  TrainingSearchRequest,
  TrainingSearchResults,
} from "./trainings/types"

export const trainerProfileApi = createApi({
  reducerPath: "trainerProfileApi",
  baseQuery: fetchBaseQuery({
    baseUrl: API_URL,
    prepareHeaders: (headers) => {
      headers.set("authorization", `Bearer ${getAccessToken()}`)
      return headers
    },
  }),
  endpoints: (builder) => ({
    getTrainerProfile: builder.query<Trainer, GymAppRequestParams>({
      query(requestParams) {
        return {
          url: "/trainers/profile",
          params: requestParams,
        }
      },
    }),
    editTrainer: builder.mutation<Trainer, Partial<Trainer>>({
      query: (training) => ({
        url: "/trainers",
        method: "PUT",
        body: {
          ...training,
        },
      }),
    }),
    addTraining: builder.mutation<void, AddTraining>({
      query: (training) => ({
        url: "/trainings/add",
        method: "POST",
        body: {
          ...training,
        },
      }),
    }),
    searchTrainerTrainings: builder.mutation<
      TrainingSearchResults[],
      TrainingSearchRequest
    >({
      query: (training) => ({
        url: "/trainings/trainer/list",
        method: "POST",
        body: {
          ...training,
        },
      }),
    }),
  }),
})

export const { 
  useGetTrainerProfileQuery, 
  useEditTrainerMutation,
  useSearchTrainerTrainingsMutation,
  useAddTrainingMutation,
  // useGetTraineesInfoQuery,
  reducer } = trainerProfileApi
