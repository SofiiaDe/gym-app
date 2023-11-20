import { Close } from "@mui/icons-material"
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
} from "@mui/material"

interface Props {
  open: boolean
  onClose: () => void
  onConfirm: () => void
}

export default function DeleteDialog(props: Props) {
  const { open, onClose, onConfirm } = props
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>
        Delete Profile
        <IconButton
          aria-label="close"
          onClick={onClose}
          sx={{ position: "absolute", right: 8, top: 8 }}
        >
          <Close />
        </IconButton>
      </DialogTitle>
      <DialogContent>
        <p>
          Are you sure you want to delete your profile? Lorem ipsum dolor sit
          amet, consectetur adipiscing elit. Sed in quam eleifend, varius mauris
          vitae, porttitor tellus. Aenean luctus, purus at tincidunt cursus,
          enim eros varius arcu, vitae tempor massa sapien sed lorem. Praesent
          vel sagittis diam. Curabitur commodo lobortis elit, at porttitor elit
          condimentum vitae. Fusce tincidunt, ante id viverra porttitor, dui
          libero pulvinar elit, in gravida quam lorem nec odio. Aliquam
          tincidunt massa quam, vel fringilla metus porttitor vel.
        </p>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button onClick={onConfirm} variant="contained" color="error">
          Confirm
        </Button>
      </DialogActions>
    </Dialog>
  )
}
