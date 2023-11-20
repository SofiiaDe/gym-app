import { useNavigate } from "react-router-dom";
import { isAuthorized } from "../../utils";
import SignedInHomePage from "./SignedInHomePage";
import DefaultHomePage from "./DefaultHomePage";

export default function HomePage() {

    const navigate = useNavigate()

    // const handleLoginClick = () => navigate("/login")
    // const handleRegisterClick = () => navigate("/register")

    return <>
        {isAuthorized() ? <SignedInHomePage /> : <DefaultHomePage />
        }
    </>
}