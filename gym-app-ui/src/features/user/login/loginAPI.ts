import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { LoginModel, LoginResponse } from "./types"
import { API_URL } from "../../../app/constants"

export const loginApi = createApi({
  baseQuery: fetchBaseQuery({ baseUrl: API_URL }),
  endpoints: (builder) => ({
    login: builder.mutation<LoginResponse, LoginModel>({
      query: (user) => ({
        url: "/auth/login",
        method: "POST",
        body: {
          ...user,
        },
      }),
    }),
  }),
})

export const { useLoginMutation, reducer } = loginApi
