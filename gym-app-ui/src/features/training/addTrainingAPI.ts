import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { API_URL } from "../../app/constants"
import { getAccessToken, getAuthHeader } from "../../utils"
import { AddTrainingModel } from "./types"

export const addTrainingApi = createApi({
  reducerPath: "profile",
  baseQuery: fetchBaseQuery({
    baseUrl: API_URL,
    prepareHeaders: (headers) => {
      headers.set("authorization", `Bearer ${getAccessToken()}`)
      return headers
    },
  }),
  endpoints: (builder) => ({
    addTraining: builder.mutation<string, AddTrainingModel>({
      query: (training) => ({
        url: "/trainings/add",
        method: "POST",
        body: {
          ...training,
        },
        responseHandler: "text",
      }),
    }),
  }),
})

export const { useAddTrainingMutation } = addTrainingApi
