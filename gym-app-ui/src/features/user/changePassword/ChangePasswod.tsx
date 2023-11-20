import { Visibility, VisibilityOff } from "@mui/icons-material"
import LockIcon from "@mui/icons-material/Lock"
import {
  Box,
  Button,
  FormControl,
  FormHelperText,
  IconButton,
  InputAdornment,
  InputLabel,
  OutlinedInput,
  Snackbar,
  Typography,
} from "@mui/material"
import { useState } from "react"
import styles from "../../../features/user/register/Register.module.css"
import { getUsernameCookie } from "../../../utils"
import ChangePasswodConfirmation from "./ChangePasswordConfirmation"
import { useChangePasswordMutation } from "./changePasswordAPI"

const validationErrorMessage = (
  <Typography variant="h6" color="red">
    Please enter all the fields
  </Typography>
)

export default function ChangePassword() {
  const [oldPassword, setOldPassword] = useState("")
  const [newPassword, setNewPassword] = useState("")
  const [confirmNewPassword, setConfirmNewPassword] = useState("")
  const [validationError, setValidationError] = useState(false)
  const [showSnackbar, setShowSnackbar] = useState(false)
  const [onClose, setOnClose] = useState(false)
  const [showPassword, setShowPassword] = useState(false)
  const passwordsDoNotMatchError = validationError && newPassword !== confirmNewPassword

  const username = getUsernameCookie()

  const [changePassword, { isSuccess }] = useChangePasswordMutation()

  const handleOldPassword = (e: any) => setOldPassword(e.target.value)
  const handleNewPassword = (e: any) => setNewPassword(e.target.value)
  const handleConfirmNewPassword = (e: any) =>
    setConfirmNewPassword(e.target.value)
  const handleClickShowPassword = () => setShowPassword((show) => !show)

  const handleMouseDownPassword = (
    event: React.MouseEvent<HTMLButtonElement>,
  ) => {
    event.preventDefault()
  }

  const handleSubmit = (e: any) => {
    e.preventDefault()

    if (!oldPassword || !newPassword || !confirmNewPassword || !passwordsDoNotMatchError) {
      setValidationError(true)
      return
    }

    validationError && setValidationError(false)

    changePassword({
      username,
      oldPassword,
      newPassword,
    }).then((response: any) => {
      if (response.error) {
        setShowSnackbar(true)
      }
    })
  }

  const handleChangePasswordClose = () => setOnClose(false)
  const handleSnackbarClose = () => setShowSnackbar(false)

  return (
    <div>
      <Typography variant="h4" sx={{ mt: 2, mb: 1 }}>
        Security
      </Typography>
      <Box sx={{ display: "flex", flexDirection: "row" }}>
        <IconButton color="inherit">
          <LockIcon />
        </IconButton>
        <Typography variant="h5" sx={{ mt: 2, mb: 1 }}>
          Change Password
        </Typography>
      </Box>

      <div>{validationError ? validationErrorMessage : null}</div>
      {isSuccess ? <ChangePasswodConfirmation /> : null}

      {!isSuccess && (
        <div className={styles.formParent}>
          <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <FormControl sx={{ m: 1, width: "45ch" }} variant="outlined">
              <InputLabel htmlFor="outlined-adornment-password">
                Current password
              </InputLabel>
              <OutlinedInput
                id="filled-adornment-password0"
                type={showPassword ? "text" : "password"}
                label="Current password"
                onChange={handleOldPassword}
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={handleClickShowPassword}
                      onMouseDown={handleMouseDownPassword}
                      edge="end"
                    >
                      {showPassword ? <Visibility /> : <VisibilityOff />}
                    </IconButton>
                  </InputAdornment>
                }
              />
            </FormControl>

            <FormControl sx={{ m: 1, width: "45ch" }} variant="outlined">
              <InputLabel htmlFor="outlined-adornment-password">
                New password
              </InputLabel>
              <OutlinedInput
                id="filled-adornment-password1"
                type={showPassword ? "text" : "password"}
                label="New password"
                onChange={handleNewPassword}
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={handleClickShowPassword}
                      onMouseDown={handleMouseDownPassword}
                      edge="end"
                    >
                      {showPassword ? <Visibility /> : <VisibilityOff />}
                    </IconButton>
                  </InputAdornment>
                }
              />
            </FormControl>

            <FormControl sx={{ m: 1, width: "45ch" }} variant="outlined">
              <InputLabel htmlFor="outlined-adornment-password">
                Confirm new password
              </InputLabel>
              <OutlinedInput
                id="filled-adornment-password2"
                type={showPassword ? "text" : "password"}
                label="New password"
                onChange={handleConfirmNewPassword}
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={handleClickShowPassword}
                      onMouseDown={handleMouseDownPassword}
                      edge="end"
                    >
                      {showPassword ? <Visibility /> : <VisibilityOff />}
                    </IconButton>
                  </InputAdornment>
                }
              />
              {passwordsDoNotMatchError && (
                <FormHelperText error id="passwordsDoNotMatch-error">
                  Passwords do not match
                </FormHelperText>
              )}
            </FormControl>

            <Box
              sx={{
                width: "100%",
                display: "flex",
                flexDirection: "row",
                justifyContent: "right",
              }}
            >
              <Button onClick={handleChangePasswordClose}>Cancel</Button>
              <Button sx={{ m: 2 }} onClick={handleSubmit} variant="contained">
                Submit
              </Button>
            </Box>
          </Box>
        </div>
      )}

      <Snackbar
        open={showSnackbar}
        autoHideDuration={5000}
        message="Failed to change password. Please, try again later"
        onClose={handleSnackbarClose}
      />
    </div>
  )
}
