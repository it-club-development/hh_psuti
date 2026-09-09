interface AuthFooterProps {
    isLogin: boolean;
    onSwitch: () => void;
}

const AuthFooter = ({ isLogin, onSwitch }: AuthFooterProps) => {
    return (
        <p className="mt-4 text-center text-sm text-gray-600">
            {isLogin ? "Нет аккаунта?" : "Уже есть аккаунт?"}{" "}
            <button onClick={onSwitch} className="text-blue-600 hover:underline font-medium">
                {isLogin ? "Зарегистрироваться" : "Войти"}
            </button>
        </p>
    );
};

export default AuthFooter;