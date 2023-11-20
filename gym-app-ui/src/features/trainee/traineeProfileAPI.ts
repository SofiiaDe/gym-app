import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { API_URL } from "../../app/constants"
import { GymAppRequestParams } from "../../app/types"
import { getAccessToken } from "../../utils"
import { AddTraining, TrainerShortInfo } from "../shared/types"
import {
  TraineeRequestBase,
  TrainingSearchRequest,
  TrainingSearchResults,
} from "./trainings/types"
import { Trainee } from "./types"

export const traineeProfileApi = createApi({
  reducerPath: "traineeProfileApi",
  baseQuery: fetchBaseQuery({
    baseUrl: API_URL,
    prepareHeaders: (headers) => {
      headers.set("authorization", `Bearer ${getAccessToken()}`)
      return headers
    },
  }),
  endpoints: (builder) => ({
    getTraineeProfile: builder.query<Trainee, GymAppRequestParams>({
      query(requestParams) {
        return {
          url: "/trainees/profile",
          params: requestParams,
        }
      },
    }),
    getTrainersInfo: builder.query<TrainerShortInfo[], GymAppRequestParams>({
      query(requestParams) {
        return {
          url: "/trainers/info",
          params: requestParams,
        }
      },
    }),
    editTrainee: builder.mutation<Trainee, Partial<Trainee>>({
      query: (trainee) => ({
        url: "/trainees",
        method: "PUT",
        body: {
          ...trainee,
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
    searchTraineeTrainings: builder.mutation<
      TrainingSearchResults[],
      TrainingSearchRequest
    >({
      query: (training) => ({
        url: "/trainings/trainee/list",
        method: "POST",
        body: {
          ...training,
        },
      }),
    }),
    deleteTrainee: builder.mutation<string, TraineeRequestBase>({
      query: (data) => ({
        url: "/trainees",
        method: "DELETE",
        body: {
          ...data,
        },
      }),
    }),
  }),
})

export const {
  useGetTraineeProfileQuery,
  useEditTraineeMutation,
  useSearchTraineeTrainingsMutation,
  useDeleteTraineeMutation,
  useAddTrainingMutation,
  useGetTrainersInfoQuery,
  reducer,
} = traineeProfileApi
