interface BackHeaderProps {
    onLogout?: () => void;
}

const BackHeader = ({ onLogout }: BackHeaderProps) => {
    return (
        <header className="flex items-center justify-between mb-2">
            <div className="flex items-center gap-8">
                <button className="text-gray-500 hover:text-gray-800 font-medium transition-colors">
                    Назад
                </button>
                <h1 className="text-blue-900 font-medium text-lg">
                    Профиль компании
                </h1>
            </div>
            {onLogout && (
                <button
                    onClick={onLogout}
                    className="px-4 py-2 text-sm text-red-600 border border-red-300 rounded-md hover:bg-red-50 transition-colors"
                >
                    Выйти
                </button>
            )}
        </header>
    );
};

export default BackHeader;