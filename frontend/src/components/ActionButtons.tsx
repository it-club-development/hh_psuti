const ActionButtons = () => {
    return (
        <div className="flex flex-wrap gap-4 justify-center sm:justify-start px-2">
            <button className="px-6 py-2.5 bg-blue-900 text-white text-sm font-medium rounded-md hover:bg-blue-800 transition-colors shadow-sm">
                Редактировать информацию
            </button>
            <button className="px-6 py-2.5 bg-white border border-gray-300 text-gray-700 text-sm font-medium rounded-md hover:bg-gray-50 transition-colors shadow-sm">
                Управлять вакансиями
            </button>
        </div>
    );
};

export default ActionButtons;