import { useState } from "react";
import {
    HiOutlineMail,
    HiOutlineLockClosed,
    HiOutlineEye,
    HiOutlineEyeOff,
} from "react-icons/hi";

interface LoginFormProps {
    onSuccess: () => void;
}

const LoginForm = ({ onSuccess }: LoginFormProps) => {
    const [showPassword, setShowPassword] = useState(false);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        // Здесь можно добавить реальную проверку, сейчас просто имитация
        onSuccess();
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-5">
            {/* поля такие же, как были */}
            <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                    Электронная почта
                </label>
                <div className="relative">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        <HiOutlineMail className="w-5 h-5 opacity-50" />
                    </div>
                    <input
                        type="email"
                        required
                        className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 text-sm"
                        placeholder="ivan@mail.ru"
                    />
                </div>
            </div>

            <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                    Пароль
                </label>
                <div className="relative">
                    <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                        <HiOutlineLockClosed className="w-5 h-5 opacity-50" />
                    </div>
                    <input
                        type={showPassword ? "text" : "password"}
                        required
                        className="block w-full pl-10 pr-10 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 text-sm"
                        placeholder="••••••••"
                    />
                    <button
                        type="button"
                        onClick={() => setShowPassword(!showPassword)}
                        className="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-400 hover:text-gray-600"
                    >
                        {showPassword ? (
                            <HiOutlineEyeOff className="w-5 h-5" />
                        ) : (
                            <HiOutlineEye className="w-5 h-5" />
                        )}
                    </button>
                </div>
            </div>

            <div className="flex items-center justify-between">
                <label className="flex items-center text-sm text-gray-700">
                    <input type="checkbox" className="rounded border-gray-300 text-blue-600 focus:ring-blue-500 mr-2" />
                    Запомнить меня
                </label>
                <a href="#" className="text-sm text-blue-600 hover:underline">
                    Забыли пароль?
                </a>
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