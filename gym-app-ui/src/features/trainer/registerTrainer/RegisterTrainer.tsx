import { Typography } from "@mui/material"
import { useState } from "react"
import Loader from "../../../app/components/Loader"
import { UserLoginData } from "../../../features/user/register/UserLoginData"
import Form from "./Form"
import { useRegisterTrainerMutation } from "./registerTrainerAPI"
import { RegisterTrainerModel } from "./types"

const style = { mt: 2, mb: 1 }
export default function RegisterTrainer() {
  const [registerTrainer, { isSuccess, isLoading, data }] =
    useRegisterTrainerMutation()

  const [firstName, setFirstName] = useState<string | undefined>(undefined)

  const handleSubmit = (data: RegisterTrainerModel) => {
    registerTrainer(data).then((res) => {
      setFirstName(data.firstName)
    })
  }

  return (
    <>
      {isLoading && <Loader />}
      <Typography variant="h4" sx={style}>
        Registration
      </Typography>
      <Typography variant="h5" sx={style}>
        Trainer
      </Typography>

      {isSuccess && data ? (
        <UserLoginData
          username={data.username}
          password={data.password}
          firstName={firstName || ""}
        />
      ) : null}

      {!isSuccess && <Form onSubmit={handleSubmit} />}
    </>
  )
}
