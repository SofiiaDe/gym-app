import { Backdrop, CircularProgress } from "@mui/material"

const Loader = () => {
  return (
    <Backdrop
      sx={{ color: "#fff", zIndex: (t) => t.zIndex.drawer + 1 }}
      open={true}
    >
      <CircularProgress color="inherit" />
    </Backdrop>
  )
}

export default Loader
