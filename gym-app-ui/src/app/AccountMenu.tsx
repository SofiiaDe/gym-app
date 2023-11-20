import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined"
import Logout from "@mui/icons-material/Logout"
import ModeNightOutlinedIcon from "@mui/icons-material/ModeNightOutlined"
import { Switch, Typography } from "@mui/material"
import Avatar from "@mui/material/Avatar"
import Box from "@mui/material/Box"
import Divider from "@mui/material/Divider"
import IconButton from "@mui/material/IconButton"
import ListItemIcon from "@mui/material/ListItemIcon"
import Menu from "@mui/material/Menu"
import MenuItem from "@mui/material/MenuItem"
import Tooltip from "@mui/material/Tooltip"
import * as React from "react"
import { useState } from "react"
import { ThemeMode } from "../theme"

interface Props {
  userName: string
  onThemeModeChange: (mode: ThemeMode) => void
  onLogout: () => void
  onMyProfileClick: () => void
}
export default function AccountMenu(props: Props) {
  const { userName, onThemeModeChange, onLogout, onMyProfileClick } = props
  const [darkMode, setDarkMode] = useState(false)

  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null)
  const open = Boolean(anchorEl)
  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget)
  }
  const handleClose = () => {
    setAnchorEl(null)
  }

  const handleModeChange = (e: any) => {
    e.preventDefault()
    e.stopPropagation()
    setDarkMode(!darkMode)
    onThemeModeChange(!darkMode ? "dark" : "light")
  }

  return (
    <React.Fragment>
      <Box sx={{ display: "flex", alignItems: "center", textAlign: "center" }}>
        <Tooltip title="Account settings">
          <IconButton
            onClick={handleClick}
            size="small"
            sx={{ ml: 2 }}
            aria-controls={open ? "account-menu" : undefined}
            aria-haspopup="true"
            aria-expanded={open ? "true" : undefined}
          >
            <Avatar sx={{ width: 32, height: 32 }}>M</Avatar>
          </IconButton>
        </Tooltip>
      </Box>
      <Menu
        anchorEl={anchorEl}
        id="account-menu"
        open={open}
        onClose={handleClose}
        onClick={handleClose}
        PaperProps={{
          elevation: 0,
          sx: {
            overflow: "visible",
            filter: "drop-shadow(0px 2px 8px rgba(0,0,0,0.32))",
            mt: 1.5,
            "& .MuiAvatar-root": {
              width: 32,
              height: 32,
              ml: -0.5,
              mr: 1,
            },
            "&:before": {
              content: '""',
              display: "block",
              position: "absolute",
              top: 0,
              right: 14,
              width: 10,
              height: 10,
              bgcolor: "background.paper",
              transform: "translateY(-50%) rotate(45deg)",
              zIndex: 0,
            },
          },
        }}
        transformOrigin={{ horizontal: "right", vertical: "top" }}
        anchorOrigin={{ horizontal: "right", vertical: "bottom" }}
      >
        <MenuItem onClick={onMyProfileClick}>
          <Avatar />{" "}
          <Box>
            <Typography variant="subtitle1">{userName}</Typography>
          </Box>
        </MenuItem>
        <MenuItem onClick={onMyProfileClick}>
          <ListItemIcon>
            <AccountCircleOutlinedIcon />
          </ListItemIcon>
          My account
        </MenuItem>
        <MenuItem onClick={handleModeChange}>
          <ListItemIcon>
            <ModeNightOutlinedIcon />
          </ListItemIcon>
          Night mode
          <Switch
            checked={darkMode}
            onChange={handleModeChange}
            inputProps={{ "aria-label": "controlled" }}
          />
        </MenuItem>

        <Divider />
        <MenuItem onClick={() => onLogout()}>
          <ListItemIcon>
            <Logout fontSize="small" />
          </ListItemIcon>
          Sign out
        </MenuItem>
      </Menu>
    </React.Fragment>
  )
}
