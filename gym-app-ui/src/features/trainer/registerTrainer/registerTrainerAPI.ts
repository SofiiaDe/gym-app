import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { API_URL } from "../../../app/constants"
import { RegisterTrainerModel } from "./types"
import { RegisterUserResponse } from "../../user/register/types"

export const registerTrainerApi = createApi({
  reducerPath: 'register',
  baseQuery: fetchBaseQuery({ baseUrl: API_URL }),
  endpoints: (builder) => ({
    registerTrainer: builder.mutation<RegisterUserResponse, RegisterTrainerModel>({
      query: (user) => ({
        url: "/trainers",
        method: "POST",
        body: {
          ...user
        },
      }),
    }),
  }),
})


export const {
  useRegisterTrainerMutation
} = registerTrainerApi