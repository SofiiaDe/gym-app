import CheckIcon from "@mui/icons-material/Check"
import { Box, Button, Snackbar, Typography } from "@mui/material"
import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"

export function UserLoginData(props: {
  firstName: string
  username: string
  password: string
}) {
  const { firstName, username, password } = props

  const navigate = useNavigate()
  const [showSnackbar, setShowSnackbar] = useState(true)

  useEffect(() => {
    setTimeout(() => setShowSnackbar(false), 1500)
  }, [])

  const handleSnackbarClose = () => setShowSnackbar(false)

  const handleLoginClick = () => {
    navigate("/login")
  }

  return (
    <Box
      sx={{
        width: "100%",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        flexDirection: "column",
      }}
    >
      <div
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          width: "100px",
          height: "100px",
          borderRadius: "50%",
          backgroundColor: "green",
        }}
      >
        <CheckIcon sx={{ color: "white" }} />
      </div>

      <Typography
        variant="body2"
        sx={{ mt: 2, width: 450, textAlign: "center" }}
      >
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. In sit amet
        venenatis neque, vel faucibus purus. Integer molestie sapien erat, ut
        blandit nulla dapibus non. Proin pulvinar ante non sem sodales rutrum.
      </Typography>

      <Typography variant="body1" sx={{ mt: 2 }}>
        {" "}
        Your login: {username}
      </Typography>
      <br />
      <Typography variant="body2" sx={{ mt: 2 }}>
        {" "}
        Your password: {password}
      </Typography>
      <br />
      <Button onClick={handleLoginClick} variant="contained" sx={{ mb: 3 }}>
        Login
      </Button>

      <Snackbar
        open={showSnackbar}
        autoHideDuration={1500}
        message={<>User {firstName} successfully registered</>}
        onClose={handleSnackbarClose}
      />
    </Box>
  )
}
