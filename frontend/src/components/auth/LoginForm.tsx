import { useState } from "react";
import { HiOutlineMail } from "react-icons/hi";
import InputField from "./InputField";
import PasswordField from "./PasswordField";

interface LoginFormProps {
    onSuccess: () => void;
}

const LoginForm = ({ onSuccess }: LoginFormProps) => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log("Login data:", { email, password });
        onSuccess();
    };

    const handleForgotPassword = (e: React.MouseEvent) => {
        e.preventDefault();
        alert("Переход на страницу восстановления пароля");
    };

    const handleEmailInvalid = (e: React.InvalidEvent<HTMLInputElement>) => {
        e.currentTarget.setCustomValidity("Электронная почта содержит недопустимые символы (например, ';')");
    };

    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        e.currentTarget.setCustomValidity("");
        setEmail(e.target.value);
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-5">
            <InputField
                id="login-email"
                label="Электронная почта"
                type="email"
                value={email}
                onChange={handleEmailChange}
                onInvalid={handleEmailInvalid}
                placeholder="ivan@mail.ru"
                icon={<HiOutlineMail className="w-5 h-5 opacity-50" />}
                required
            />

            <PasswordField
                id="login-password"
                label="Пароль"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
            />

            <div className="flex items-center justify-between">
                <label className="flex items-center text-sm text-gray-700">
                    <input
                        type="checkbox"
                        className="rounded border-gray-300 text-blue-600 focus:ring-blue-500 mr-2"
                    />
                    Запомнить меня
                </label>
                <button
                    type="button"
                    onClick={handleForgotPassword}
                    className="text-sm text-blue-600 hover:underline"
                >
                    Забыли пароль?
                </button>
            </div>

            <button
                type="submit"
                className="w-full py-2.5 bg-blue-900 text-white text-sm font-medium rounded-md hover:bg-blue-800 transition-colors shadow-sm"
            >
                Войти
            </button>
        </form>
    );
};

export default LoginForm;