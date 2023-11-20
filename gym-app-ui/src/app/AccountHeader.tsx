import { Button, Typography } from "@mui/material"
import { useDispatch, useSelector } from "react-redux"
import { useNavigate } from "react-router-dom"
import { logout, selectUsername } from "../features/user/login/loginSlice"
import { ThemeMode } from "../theme"
import { getCookie, getUserTypeCookie, getUsernameCookie } from "../utils"
import AccountMenu from "./AccountMenu"
import { NavigationItem } from "./types"

interface Props {
  accountNavigationItems: NavigationItem[]
  onThemeModeChange: (mode: ThemeMode) => void
}

export default function AccountHeader(props: Props) {
  const { accountNavigationItems, onThemeModeChange } = props

  const navigate = useNavigate()
  const login = useSelector(selectUsername)
  const dispatch = useDispatch()

  if (!login) {
    return accountNavigationItems.map((navItem) => (
      <Button
        color="inherit"
        key={navItem.link}
        onClick={() => navigate(navItem.link)}
      >
        {navItem.label}
      </Button>
    ))
  }

  const handleLogoutClick = () => {
    dispatch(logout())
    navigate("/")
  }

  const handleOnProfileClick = () => {
    navigate(
      getUserTypeCookie() === "trainer"
        ? "/trainer/profile"
        : "/trainee/profile",
    )
  }

  return (
    <>
      <Typography variant="subtitle1">{login}</Typography>
      <AccountMenu
        userName={login}
        onThemeModeChange={onThemeModeChange}
        onLogout={handleLogoutClick}
        onMyProfileClick={handleOnProfileClick}
      />
    </>
  )
}
