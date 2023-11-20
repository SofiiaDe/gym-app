import { DataGrid, GridColDef } from "@mui/x-data-grid"
import styles from "./Profile.module.css"
import { TrainerShortInfo } from "../../shared/types"

const columns: GridColDef[] = [
  { field: "username", headerName: "Username", width: 300 },
  { field: "firstName", headerName: "First name", width: 300 },
  { field: "lastName", headerName: "Last name", width: 300 },
  { field: "specialization", headerName: "Specialization", width: 300 },
]

interface Props {
  unassignedTrainers: TrainerShortInfo[]
}

export default function UnassignedTrainerList(props: Props) {
  const { unassignedTrainers: unassignedTrainers } = props

  if (!unassignedTrainers || unassignedTrainers.length <= 0) return null

  return (
    <div className={styles.grid}>
      <DataGrid
        rows={unassignedTrainers}
        columns={columns}
        initialState={{
          pagination: {
            paginationModel: { page: 0, pageSize: 5 },
          },
        }}
        pageSizeOptions={[5, 10, 25]}
      />
    </div>
  )
}
