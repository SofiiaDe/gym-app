import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { API_URL } from "../../../app/constants"
import { ChangePasswordModel } from "./types"
import { getAccessToken } from "../../../utils"

export const changePasswordApi = createApi({
  reducerPath: "changePassword",
  baseQuery: fetchBaseQuery({
    baseUrl: API_URL,
    prepareHeaders: (headers) => {
      headers.set("authorization", `Bearer ${getAccessToken()}`)
      return headers
    },
  }),
  endpoints: (builder) => ({
    changePassword: builder.mutation<string, ChangePasswordModel>({
      query: (user) => ({
        url: "/auth/change-password",
        method: "PUT",
        body: {
          ...user,
        },
      }),
    }),
  }),
})

export const { useChangePasswordMutation, reducer } = changePasswordApi
