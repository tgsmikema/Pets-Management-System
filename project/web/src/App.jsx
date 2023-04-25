import { useAuth } from "./providers/AuthProvider.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import DashBoard from "./pages/DashBoard.jsx";

function App() {
  const { user } = useAuth();
  //if the user is null, we will show the login pages, otherwise we will redirect to dashboard page
  return user === null ? <LoginPage /> : <DashBoard />;
}

export default App;
