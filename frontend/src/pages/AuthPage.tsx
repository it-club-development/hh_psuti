import { useState } from "react";
import AuthHeader from "../components/auth/AuthHeader";
import LoginForm from "../components/auth/LoginForm";
import RegisterForm from "../components/auth/RegisterForm";
import SocialButtons from "../components/auth/SocialButtons";
import AuthFooter from "../components/auth/AuthFooter";

interface AuthPageProps {
    onLogin: () => void;
    onRegister: () => void;
}

const AuthPage = ({ onLogin, onRegister }: AuthPageProps) => {
    const [isLogin, setIsLogin] = useState(true);

    const toggleMode = () => {
        setIsLogin(!isLogin);
    };

    return (
        <div className="min-h-screen bg-white p-4 md:p-8 font-sans flex items-center justify-center">
            <div className="max-w-md w-full">
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 md:p-8">
                    <AuthHeader title={isLogin ? "Вход" : "Регистрация"} />

                    {isLogin ? (
                        <LoginForm onSuccess={onLogin} />
                    ) : (
                        <RegisterForm onSuccess={onRegister} />
                    )}

                    <SocialButtons />
                    <AuthFooter isLogin={isLogin} onSwitch={toggleMode} />
                </div>
            </div>
        </div>
    );
};

export default AuthPage;