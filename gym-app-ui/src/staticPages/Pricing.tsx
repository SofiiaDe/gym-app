import { Email as EmailIcon } from "@mui/icons-material"
import ExpandMoreIcon from "@mui/icons-material/ExpandMore"
import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Badge,
  Box,
  Button,
  Container,
  Grid,
  Typography,
} from "@mui/material"

export default function Pricing() {
  return (
    <Container>
      <Typography
        variant="h3"
        sx={{ fontWeight: "bolder", display: "flex", justifyContent: "center" }}
        gutterBottom
      >
        Pricing
      </Typography>
      <Typography variant="body1" sx={{ textAlign: "center", mb: 10 }}>
        Sed ut perspiciatis unde omnis iste natus error sit voluptatem
        accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab
        illo inventore veritatis et quasi architecto beatae vitae dicta sunt
        explicabo.
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12} sm={4}>
          <Typography variant="h4" sx={{ fontWeight: "bolder" }}>
            Group
          </Typography>
          <Typography variant="body2">Perfect for hobby trainings</Typography>
          <Box sx={{ display: "flex", displayDirection: "row" }}>
            <Box>
              <Typography variant="h3" sx={{ fontWeight: "bolder" }}>
                $50
              </Typography>
            </Box>
            <Box>
              <Typography variant="h6">/month</Typography>
            </Box>
          </Box>
          <ul style={{ listStyle: "none" }}>
            <li>✅ Up to 5 users</li>
            <li>✅ Collaboration features</li>
            <li>❌ Smart analytics</li>
            <li>❌ 30-day free trial</li>
          </ul>
          <Button variant="contained" color="inherit">
            Upgrade
          </Button>
        </Grid>

        <Grid item xs={12} sm={4}>
          <Box sx={{ display: "flex", flexDirection: "row" }}>
            <Box sx={{ mr: 10 }}>
              <Typography
                variant="h4"
                sx={{ fontWeight: "bolder", color: "blue" }}
              >
                Personal
              </Typography>
            </Box>
            <Box>
              <Badge color="primary" badgeContent="Popular"></Badge>
            </Box>
          </Box>
          <Typography variant="body2">Perfect for small teams</Typography>
          <Box sx={{ display: "flex", displayDirection: "row" }}>
            <Box>
              <Typography variant="h3" sx={{ fontWeight: "bolder" }}>
                $100
              </Typography>
            </Box>
            <Box>
              <Typography variant="h6">/team/month</Typography>
            </Box>
          </Box>
          <ul style={{ listStyle: "none" }}>
            <li>✅ Up to 50 users</li>
            <li>✅ Collaboration features</li>
            <li>✅ Smart analytics</li>
            <li>✅ 30-day free trial</li>
          </ul>
          <Button variant="contained" color="primary">
            Upgrade
          </Button>
        </Grid>

        <Grid item xs={12} sm={4}>
          <Typography variant="h4" sx={{ fontWeight: "bolder" }}>
            Organization
          </Typography>
          <Typography variant="body2">Perfect for organizations</Typography>
          <Box sx={{ display: "flex", displayDirection: "row" }}>
            <Box>
              <Typography variant="h3" sx={{ fontWeight: "bolder" }}>
                $150
              </Typography>
            </Box>
            <Box>
              <Typography variant="h6">/user/month</Typography>
            </Box>
          </Box>
          <ul style={{ listStyle: "none" }}>
            <li>✅ Unlimited users</li>
            <li>✅ Collaboration features</li>
            <li>✅ Smart analytics</li>
            <li>✅ 30-day free trial</li>
          </ul>
          <Button variant="contained" color="inherit">
            <EmailIcon />
            Contact Sale
          </Button>
        </Grid>
      </Grid>

      <Typography
        variant="h3"
        sx={{
          fontWeight: "bolder",
          display: "flex",
          justifyContent: "center",
          mt: 10,
        }}
        gutterBottom
      >
        Frequently Asked Questions
      </Typography>
      <Typography variant="body1" sx={{ textAlign: "center", mb: 8 }}>
        Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis
      </Typography>
      <Box>
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel1a-content"
            id="panel1a-header"
          >
            <Typography variant="subtitle1" sx={{ fontWeight: "bolder" }}>
              Unde omnis iste natus error sit voluptatem
            </Typography>
          </AccordionSummary>
          <AccordionDetails>
            <Typography variant="body2">
              Sed ut perspiciatis unde omnis iste natus error sit voluptatem
              accusantium doloremque laudantium, totam rem aperiam, eaque ipsa
              quae ab illo inventore veritatis et quasi architecto beatae vitae
              dicta sunt explicabo.
            </Typography>
          </AccordionDetails>
        </Accordion>
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel2a-content"
            id="panel2a-header"
          >
            <Typography variant="subtitle1" sx={{ fontWeight: "bolder" }}>
              Nam libero tempore cum soluta nobis
            </Typography>
          </AccordionSummary>
          <AccordionDetails>
            <Typography variant="body2">
              Et harum quidem rerum facilis est et expedita distinctio. Nam
              libero tempore, cum soluta nobis est eligendi optio cumque nihil
              impedit quo minus id quod maxime placeat facere possimus, omnis
              voluptas assumenda est, omnis dolor repellendus.
            </Typography>
          </AccordionDetails>
        </Accordion>
        <Accordion>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls="panel3a-content"
            id="panel3a-header"
          >
            <Typography variant="subtitle1" sx={{ fontWeight: "bolder" }}>
              Ullam corporis suscipit laboriosam
            </Typography>
          </AccordionSummary>
          <AccordionDetails>
            <Typography variant="body2">
              Ut enim ad minima veniam, quis nostrum exercitationem ullam
              corporis suscipit laboriosam, nisi ut aliquid ex ea commodi
              consequatur.
            </Typography>
          </AccordionDetails>
        </Accordion>
      </Box>
    </Container>
  )
}
