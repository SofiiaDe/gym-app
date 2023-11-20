import { MoreHoriz } from "@mui/icons-material"
import AccountCircleIcon from "@mui/icons-material/AccountCircle"
import CloseIcon from "@mui/icons-material/Close"
import ExitToAppIcon from "@mui/icons-material/ExitToApp"
import {
  AppBar,
  Avatar,
  Box,
  Button,
  Container,
  Divider,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  ThemeProvider,
  Toolbar,
  Typography,
} from "@mui/material"
import { useTheme } from "@mui/material/styles"
import useMediaQuery from "@mui/material/useMediaQuery"
import React, { useState } from "react"
import { useSelector } from "react-redux"
import {
  Link,
  Route,
  Routes,
  matchPath,
  useLocation,
  useNavigate,
} from "react-router-dom"
import "./App.css"
import Footer from "./Footer"
import AccountHeader from "./app/AccountHeader"
import { NavigationItem } from "./app/types"
import { HomePage } from "./features/home"
import NotFoundPage from "./features/notFound"
import { RequireAuth } from "./features/requireAuth/RequireAuth"
import EditTraineeProfile from "./features/trainee/editProfile"
import TraineeProfile from "./features/trainee/profile/TraineeProfile"
import TraineeTrainings from "./features/trainee/trainings"
import TrainerProfile from "./features/trainer/profile/TrainerProfile"
import EditTrainerProfile from "./features/trainer/editProfile"
import TrainerTrainings from "./features/trainer/trainings"
import ChangePassword from "./features/user/changePassword/ChangePasswod"
import Login from "./features/user/login/Login"
import { selectUsername } from "./features/user/login/loginSlice"
import Register from "./features/user/register/Register"
import gymLogo from "./gym_logo.png"
import AboutUs from "./staticPages/AboutUs"
import DeletedProfile from "./staticPages/DeletedProfile"
import Pricing from "./staticPages/Pricing"
import { ThemeMode, darkTheme, lightTheme } from "./theme"
import AddTraining from "./features/trainee/addTraining"
import Features from "./staticPages/Features"

const navigation: NavigationItem[] = [
  { label: "Pricing", link: "/price" },
  { label: "About Us", link: "/aboutUs" },
]

const accountNavigationItems: NavigationItem[] = [
  { label: "Sign In", link: "/login", icon: <AccountCircleIcon /> },
  { label: "Join", link: "/register", icon: <ExitToAppIcon /> },
]

function App() {
  const theme = useTheme()

  const [themeMode, setThemeMode] = useState<ThemeMode>("light")
  const { pathname } = useLocation()
  const navigate = useNavigate()

  const isMobile = useMediaQuery(theme.breakpoints.down("sm"))

  const [open, setOpen] = React.useState(false)
  const handleDrawerOpen = () => setOpen(true)
  const handleDrawerClose = () => setOpen(false)

  const username = useSelector(selectUsername)

  return (
    <ThemeProvider theme={themeMode === "dark" ? darkTheme : lightTheme}>
      <Box sx={{ flexGrow: 1, height: "max-content" }}>
        <AppBar position="static">
          <Toolbar>
            {isMobile ? (
              <IconButton
                edge="start"
                sx={{
                  mr: 2,
                }}
                color="inherit"
                aria-label="menu"
                onClick={handleDrawerOpen}
              >
                <MoreHoriz />
              </IconButton>
            ) : null}
            <Box
              sx={{
                display: "flex",
                margin: isMobile ? "auto" : null,
                flexGrow: !isMobile ? 1 : null,
                alignItems: "center",
              }}
            >
              <Link
                to={"/"}
                style={{
                  textDecoration: "none",
                  display: "flex",
                  alignItems: "center",
                  color: "white",
                }}
              >
                <img src={gymLogo} style={{ height: 60 }} />
                <Typography variant="h6">GYM APP</Typography>
              </Link>
              {!isMobile ? (
                <>
                  {navigation.map((navItem) => {
                    return (
                      <Button
                        color="inherit"
                        key={navItem.label}
                        onClick={() => navigate(navItem.link)}
                      >
                        {navItem.label}
                      </Button>
                    )
                  })}
                </>
              ) : null}
            </Box>
            {!isMobile ? (
              <AccountHeader
                accountNavigationItems={accountNavigationItems}
                onThemeModeChange={setThemeMode}
              />
            ) : null}
          </Toolbar>
        </AppBar>
        <Drawer
          anchor="left"
          open={open}
          onClose={handleDrawerClose}
          sx={{ width: 240 }}
        >
          <List>
            <ListItem button>
              <ListItemIcon>
                <Avatar alt="User" src="/broken-image.jpg" sx={{ mr: 1 }} />
              </ListItemIcon>
              <ListItemText primary={username} />
              <IconButton
                color="inherit"
                aria-label="close menu"
                onClick={(e: any) => {
                  e.stopPropagation()
                  setOpen(false)
                }}
              >
                <CloseIcon />
              </IconButton>
            </ListItem>
            <Divider />
            {navigation.map((navItem) => {
              return (
                <ListItemButton
                  key={"m_" + navItem.label}
                  selected={!!matchPath(pathname, navItem.link)}
                  onClick={() => {
                    setOpen(false)
                    navigate(navItem.link)
                  }}
                >
                  <ListItemText primary={navItem.label} />
                </ListItemButton>
              )
            })}
            <Divider />
            {!username &&
              accountNavigationItems.map((navItem) => (
                <ListItemButton
                  key={"m_" + navItem.link}
                  onClick={() => {
                    setOpen(false)
                    navigate(navItem.link)
                  }}
                >
                  <ListItemIcon>{navItem.icon}</ListItemIcon>
                  <ListItemText primary={navItem.label} />
                </ListItemButton>
              ))}
          </List>
        </Drawer>
        <Box
          sx={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}
        >
          <Container sx={{ py: 8 }} maxWidth="lg">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/register" element={<Register />} />
              <Route path="/login" element={<Login />} />
              <Route path="/aboutUs" element={<AboutUs />} />
              <Route path="/features" element={<Features />} />
              <Route path="/price" element={<Pricing />} />
              <Route
                path="/trainee/profile"
                element={
                  <RequireAuth>
                    <TraineeProfile />
                  </RequireAuth>
                }
              />
              <Route
                path="/trainee/profile/edit"
                element={
                  <RequireAuth>
                    <EditTraineeProfile />
                  </RequireAuth>
                }
              />
              <Route
                path="/trainer/profile"
                element={
                  <RequireAuth>
                    <TrainerProfile />
                  </RequireAuth>
                }
              />
              <Route
                path="/trainer/profile/edit"
                element={
                  <RequireAuth>
                    <EditTrainerProfile />
                  </RequireAuth>
                }
              />
              <Route
                path="/changepassword"
                element={
                  <RequireAuth>
                    <ChangePassword />
                  </RequireAuth>
                }
              />
              {/* <Route
              path="/home"
              element={
                <RequireAuth>
                  <SignedInHomePage />
                </RequireAuth>
              }
            /> */}
              <Route path="/deleted" element={<DeletedProfile />} />
              <Route
                path="/trainee/trainings"
                element={
                  <RequireAuth>
                    <TraineeTrainings />
                  </RequireAuth>
                }
              />
              <Route
                path="/trainee/addtraining"
                element={
                  <RequireAuth>
                    <AddTraining />
                  </RequireAuth>
                }
              />
              <Route
                path="/trainer/trainings"
                element={
                  <RequireAuth>
                    <TrainerTrainings />
                  </RequireAuth>
                }
              />
              <Route
                path="/trainer/addtraining"
                element={
                  <RequireAuth>
                    <AddTraining />
                  </RequireAuth>
                }
              />
              <Route path="*" element={<NotFoundPage />} />
            </Routes>
          </Container>
          <Footer />
        </Box>
      </Box>
    </ThemeProvider>
  )
}

export default App
