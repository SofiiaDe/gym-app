import { useEffect, useState } from "react"
import { useSelector } from "react-redux"
import { Navigate, useLocation } from "react-router-dom"
import { deleteAuthCookies, isAuthorized } from "../../utils"
import { selectUsername } from "../user/login/loginSlice"
import { useLogoutMutation } from "./requireAuthAPI"

// todo cleanup
export function RequireAuth({ children }: { children: JSX.Element }) {
  const usernameFromState = useSelector(selectUsername)
  let isLoggedIn = isAuthorized()
  const location = useLocation()
  const [toLogout, setToLogout] = useState(false)
  const [logout] = useLogoutMutation()

  useEffect(() => {
    // to follow stata changes, if user was signed out
  }, [isLoggedIn, usernameFromState])

  useEffect(() => {
    if (toLogout) {
      logout().then(() => {
        setToLogout(false)
      })

      deleteAuthCookies()
      isLoggedIn = false
    }
  }, [toLogout])

  if (!isLoggedIn) return <Navigate to="/login" state={{ from: location }} />

  return <>{children}</>
}
