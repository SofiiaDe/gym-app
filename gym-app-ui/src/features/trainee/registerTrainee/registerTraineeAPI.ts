import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { API_URL } from "../../../app/constants"
import { RegisterTraineeModel } from "./types"
import { RegisterUserResponse } from "../../user/register/types"

export const registerTraineeApi = createApi({
  reducerPath: 'register',
  baseQuery: fetchBaseQuery({ baseUrl: API_URL }),
  endpoints: (builder) => ({
    registerTrainee: builder.mutation<RegisterUserResponse, RegisterTraineeModel>({
      query: (user) => ({
        url: "/trainees",
        method: "POST",
        body: {
          ...user
        },
      }),
    }),
  }),
})


export const {
  useRegisterTraineeMutation
} = registerTraineeApi