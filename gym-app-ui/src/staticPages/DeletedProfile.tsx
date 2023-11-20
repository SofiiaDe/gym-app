import { Typography } from "@mui/material"
import { useEffect } from "react"
import { useLogoutMutation } from "../features/requireAuth/requireAuthAPI"
import { useAppDispatch } from "../app/hooks"
import { logout as logoutAction } from "../features/user/login/loginSlice"

export default function DeletedProfile() {
  const dispatch = useAppDispatch()

  useEffect(() => {
    dispatch(logoutAction())
  }, [])

  return (
    <div>
      <Typography variant="h4">Profile Successfully Deleted</Typography>
    </div>
  )
}
