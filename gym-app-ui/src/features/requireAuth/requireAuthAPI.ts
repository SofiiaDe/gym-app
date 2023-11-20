import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react"
import { API_URL } from "../../app/constants"
import { getAuthHeader } from "../../utils"

export const requireAuthApi = createApi({
  reducerPath: 'requireAuth',
  baseQuery: fetchBaseQuery({ baseUrl: API_URL }),
  endpoints: (builder) => ({
    logout: builder.mutation<string, void>({
      query: () => ({
        url: "/auth/logout",
        method: "POST",
        headers: {
          ...getAuthHeader()
        },
        responseHandler: "text",
      }),
    }),
  }),
})


export const {
  useLogoutMutation
} = requireAuthApi