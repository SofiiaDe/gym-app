import {
  Box,
  Container,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material"
import { styled } from "@mui/material/styles"
import { TraineeShortInfo as TraineeShortInfo } from "../../shared/types"

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  "&:nth-of-type(odd)": {
    backgroundColor: theme.palette.action.hover,
  },
  // hide last border
  "&:last-child td, &:last-child th": {
    border: 0,
  },
}))

const TableCellHeaderSx = { fontWeight: "bolder", textTransform: "uppercase" }

interface Props {
  trainees: TraineeShortInfo[]
}

export default function MyTrainees(props: Props) {
  const { trainees: trainees } = props
  return (
    <Container maxWidth="md">
      <Box
        sx={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <h2>My trainees</h2>
      </Box>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 130 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <TableCell sx={TableCellHeaderSx}>Name</TableCell>
              <TableCell align="left" sx={TableCellHeaderSx}>
                Status
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {trainees?.map((trainee) => (
              <StyledTableRow key={trainee.firstName + "_" + trainee.lastName}>
                <TableCell
                  component="th"
                  scope="row"
                  sx={{ fontWeight: "bolder" }}
                >
                  {trainee.firstName + " " + trainee.lastName}
                </TableCell>
                <TableCell align="left">{trainee.isActive ? "Active" : "Inactive"}</TableCell>
              </StyledTableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  )
}
