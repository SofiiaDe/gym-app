import {
  Backdrop,
  Box,
  Button,
  CircularProgress,
  Snackbar,
  TextField,
  Typography,
} from "@mui/material"
import { useEffect, useState } from "react"
import ReCAPTCHA from "react-google-recaptcha"
import { useNavigate } from "react-router-dom"
import { setAuthCookies, setCookieByName } from "../../../utils"
import { useLoginMutation } from "./loginAPI"
import Loader from "../../../app/components/Loader"

type ValidationErrorType =
  | "emptyLogin"
  | "emptyPassword"
  | "unauthorized"
  | "catchaNotValid"

function getErrorMessage(errorType: ValidationErrorType | undefined) {
  if (!errorType) return null

  if (errorType === "emptyLogin" || errorType === "emptyPassword")
    return "Please enter all the fields"
  else return "Login or password is incorrect"
}

export default function Login() {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [errorType, setErrorType] = useState<ValidationErrorType | undefined>(
    undefined,
  )
  const [showFailedLoginSnackbar, setShowFailedLoginSnackbar] = useState(false)
  const [captchaValue, setCatchaValue] = useState<string | undefined>(undefined)
  const [backdropOpen, setBackdropOpen] = useState(false)

  const [login, { data, isError, isSuccess }] = useLoginMutation()

  useEffect(() => {
    if (isSuccess && data) {
      setAuthCookies(data.accessToken, data.refreshToken)
      setCookieByName("username", username)
      setCookieByName("userType", data.userType)

      data.userType === "trainee"
        ? navigate("/trainee/profile")
        : navigate("/trainer/profile")
    }
  }, [isSuccess, data])

  const navigate = useNavigate()

  const handleUsername = (e: any) => setUsername(e.target.value)
  const handlePassword = (e: any) => setPassword(e.target.value)

  const handleSubmit = (e: any) => {
    setErrorType(undefined)

    e.preventDefault()
    if (!captchaValue) {
      setErrorType("catchaNotValid")
      return
    }

    if (!username) {
      setErrorType("emptyLogin")
      return
    }
    if (!password) {
      setErrorType("emptyPassword")
      return
    }

    setBackdropOpen(true)
    login({
      username,
      password,
      captchaValue: captchaValue || "",
    }).then((response: any) => {
      setBackdropOpen(false)
      if (response.error) {
        setShowFailedLoginSnackbar(true)
      }
    })
  }

  const handleFailedLoginSnackbarClose = () => setShowFailedLoginSnackbar(false)

  const isPasswordError =
    errorType === "emptyPassword" || errorType === "unauthorized"
  const isLoginError = errorType === "emptyLogin"

  const onCaptchaChange = (value: any) => {
    if (errorType === "catchaNotValid") setErrorType(undefined)
    setCatchaValue(value)
  }

  return (
    <>
      <Typography variant="h4" sx={{ mt: 2, mb: 1 }}>
        User Login
      </Typography>
      <Box sx={{ width: "100%", display: "flex", justifyContent: "center" }}>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            width: 340,
            justifyContent: "center",
          }}
        >
          {backdropOpen ? <Loader /> : null}
          <TextField
            sx={{ m: 2 }}
            label="Username"
            variant="outlined"
            error={isLoginError}
            helperText={isLoginError && getErrorMessage(errorType)}
            onChange={handleUsername}
          />
          <TextField
            sx={{ m: 2 }}
            label="Password"
            type="password"
            variant="outlined"
            onChange={handlePassword}
            error={isPasswordError}
            helperText={isPasswordError && getErrorMessage(errorType)}
          />
          <Button sx={{ m: 2 }} onClick={handleSubmit} variant="contained">
            Submit
          </Button>
          <Box
            sx={{
              alignSelf: "center",
            }}
          >
            <ReCAPTCHA
              sitekey="6LcqvgwpAAAAAI6hXt3xAAiXUz6y50NRGlWsCSfX"
              onChange={onCaptchaChange}
            />
            {errorType === "catchaNotValid" ? (
              <Typography variant="subtitle2" color={"error"}>
                Please use captcha
              </Typography>
            ) : null}
          </Box>
        </Box>
      </Box>
      <Snackbar
        open={showFailedLoginSnackbar}
        autoHideDuration={5000}
        message="Login or password is incorrect"
        onClose={handleFailedLoginSnackbarClose}
      />
    </>
  )
}
