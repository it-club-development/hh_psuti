const CompanyDetails = () => {
    return (
        <section className="bg-gray-200 rounded-2xl p-6 h-full min-h-[400px] flex flex-col relative border border-gray-200">
            <div className="flex flex-col items-end gap-1 mb-4 text-gray-600 text-sm z-10">
                <a href="#" className="hover:text-blue-600 hover:underline">О компании</a>
                <a href="#" className="hover:text-blue-600 hover:underline border-b border-gray-400 pb-0.5">Сайт компании</a>
            </div>

            <div className="flex-grow bg-gray-400/50 rounded-xl w-full h-full min-h-[300px] shadow-inner">
                {/* Здесь может быть карта или другой контент */}
            </div>
        </section>
    );
};

export default CompanyDetails;