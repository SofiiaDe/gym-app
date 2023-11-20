import { Avatar, Box, Button, Grid, Link, Typography } from '@mui/material';
import { useSelector } from 'react-redux';
import { getUsernameCookie } from '../../utils';
import { selectUsername } from '../user/login/loginSlice';

export default function SignedInHomePage() {

  const username = useSelector(selectUsername)


  return (
    <Box sx={{ flexGrow: 1, p: 2 }} >
      <Typography variant="h3" sx={{ textAlign: "center", mb: 2, fontWeight: "bolder", }}>
        Hi, {username}!
      </Typography>
      <Typography variant="body1" sx={{ textAlign: "center", mb: 10 }}>
        At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.
      </Typography>
      <Typography variant="h4" sx={{ textAlign: "center", mx: 2, fontWeight: "bolder" }}>
        What's new?
      </Typography>
      <Typography variant="body1" sx={{ textAlign: "center", mb: 5 }}>
        Corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.
      </Typography>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={4}>
          <img
            src="https://via.placeholder.com/300x200"
            alt="placeholder"
            style={{ width: "100%", height: "auto" }}
          />
          <Typography variant="subtitle1" sx={{ color: "blue" }}>
            Quis autem vel
          </Typography>
          <Typography variant="h6" sx={{ fontWeight: "bolder" }}>Ut enim ad minima veniam, quis nostrum exercitationem ullam
          </Typography>
          <Box sx={{ display: "flex", flexDirection: "row", justifyContent: "space-between" }}>
            <Typography variant="subtitle2">Dec 24, 2022</Typography>
            <Typography variant="subtitle2" sx={{ display: "flex", justifyContent: "right" }} >
              5 mins read
            </Typography>
          </Box>
        </Grid>
        <Grid item xs={12} sm={4}>
          <img
            src="https://via.placeholder.com/300x200"
            alt="placeholder"
            style={{ width: "100%", height: "auto" }}
          />
          <Typography variant="subtitle1" sx={{ color: "blue" }}>
            Itaque earum
          </Typography>
          <Typography variant="h6" sx={{ fontWeight: "bolder" }}>Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut</Typography>
          <Box sx={{ display: "flex", flexDirection: "row", justifyContent: "space-between" }}>
            <Typography variant="subtitle2">Dec 24, 2022</Typography>
            <Typography variant="subtitle2" sx={{ display: "flex", justifyContent: "right" }}>
              5 mins read
            </Typography>
          </Box>
        </Grid>
        <Grid item xs={12} sm={4}>
          <img
            src="https://via.placeholder.com/300x200"
            alt="placeholder"
            style={{ width: "100%", height: "auto" }}
          />
          <Typography variant="subtitle1" sx={{ color: "blue" }}>
            Temporibus autem
          </Typography>
          <Typography variant="h6" sx={{ fontWeight: "bolder" }}>Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia</Typography>
          <Box sx={{ display: "flex", flexDirection: "row", justifyContent: "space-between" }}>
            <Typography variant="subtitle2">Dec 24, 2022</Typography>
            <Typography variant="subtitle2" sx={{ display: "flex", justifyContent: "right" }}>
              5 mins read
            </Typography>
          </Box>
        </Grid>
      </Grid>
      <Button variant="contained" sx={{ m: "auto", mt: 8, display: "block" }}>
        Read more articles
      </Button>
      <Box sx={{ float: "right" }}>
        <Avatar src="{avatarUrl}" sx={{ mr: 1 }} />
        <Typography variant="subtitle1">{username}</Typography>
        <Link href="#" sx={{ mr: 1 }}>
          My account
        </Link>
        <Link href="#" sx={{ mr: 1 }}>
          Sign out
        </Link>
      </Box>
    </Box >
  );
};