import { useState } from "react";
import { HiOutlineMail, HiOutlineUser } from "react-icons/hi";
import InputField from "./InputField";
import PasswordField from "./PasswordField";

interface RegisterFormProps {
    onSuccess: () => void;
}

const RegisterForm = ({ onSuccess }: RegisterFormProps) => {
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [passwordError, setPasswordError] = useState("");
    const [agree, setAgree] = useState(false);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            setPasswordError("Пароли не совпадают");
            return;
        }
        setPasswordError("");
        console.log("Register data:", { name, email, password, confirmPassword, agree });
        onSuccess();
    };

    const handleAgreementClick = (e: React.MouseEvent) => {
        e.preventDefault();
        alert("Открыть пользовательское соглашение");
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
                id="register-name"
                label="Имя и фамилия"
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Иван Иванов"
                icon={<HiOutlineUser className="w-5 h-5 opacity-50" />}
                required
            />

            <InputField
                id="register-email"
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
                id="register-password"
                label="Пароль"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                error={passwordError && password !== confirmPassword ? passwordError : ""}
            />

            <PasswordField
                id="register-confirm-password"
                label="Подтвердите пароль"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
                error={passwordError && password !== confirmPassword ? passwordError : ""}
            />

            {passwordError && <p className="text-red-500 text-sm -mt-2">{passwordError}</p>}

            <div className="flex items-start">
                <input
                    id="agree"
                    type="checkbox"
                    checked={agree}
                    onChange={(e) => setAgree(e.target.checked)}
                    required
                    className="rounded border-gray-300 text-blue-600 focus:ring-blue-500 mr-2 mt-0.5"
                />
                <label htmlFor="agree" className="text-sm text-gray-700">
                    Принимаю{" "}
                    <button
                        type="button"
                        onClick={handleAgreementClick}
                        className="text-blue-600 hover:underline"
                    >
                        пользовательское соглашение
                    </button>
                </label>
            </div>

            <button
                type="submit"
                className="w-full py-2.5 bg-blue-900 text-white text-sm font-medium rounded-md hover:bg-blue-800 transition-colors shadow-sm"
            >
                Зарегистрироваться
            </button>
        </form>
    );
};

export default RegisterForm;