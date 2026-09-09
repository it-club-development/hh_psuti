import { useState } from "react";
import AuthPage from "./pages/AuthPage";
import CompanyProfile from "./pages/CompanyProfile";

const App = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const handleLogin = () => setIsAuthenticated(true);
  const handleRegister = () => setIsAuthenticated(true);
  const handleLogout = () => setIsAuthenticated(false);

  return (
      <>
        {isAuthenticated ? (
            <CompanyProfile onLogout={handleLogout} />
        ) : (
            <AuthPage onLogin={handleLogin} onRegister={handleRegister} />
        )}
      </>
  );
};

export default App;