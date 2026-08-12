const BackHeader = () => {
    return (
        <header className="flex items-center gap-8 mb-2">
            <button className="text-gray-500 hover:text-gray-800 font-medium transition-colors">
                Назад
            </button>
            <h1 className="text-blue-900 font-medium text-lg">
                Профиль компании
            </h1>
        </header>
    );
};

export default BackHeader;