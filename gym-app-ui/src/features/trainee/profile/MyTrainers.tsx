import {
  Box,
  Button,
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
import { TrainerShortInfo } from "../../shared/types"

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
  trainers: TrainerShortInfo[]
}

export default function MyTrainers(props: Props) {
  const { trainers } = props
  return (
    <Container maxWidth="md">
      <Box
        sx={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <h2>My trainers</h2>
      </Box>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 130 }} aria-label="customized table">
          <TableHead>
            <TableRow>
              <TableCell sx={TableCellHeaderSx}>Name</TableCell>
              <TableCell align="left" sx={TableCellHeaderSx}>
                Specialization
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {trainers?.map((trainer) => (
              <StyledTableRow key={trainer.firstName + "_" + trainer.lastName}>
                <TableCell
                  component="th"
                  scope="row"
                  sx={{ fontWeight: "bolder" }}
                >
                  {trainer.firstName + " " + trainer.lastName}
                </TableCell>
                <TableCell align="left">{trainer.specialization}</TableCell>
              </StyledTableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Container>
  )
}
